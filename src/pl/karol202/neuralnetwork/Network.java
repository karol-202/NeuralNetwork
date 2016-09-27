package pl.karol202.neuralnetwork;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Network implements Runnable
{
	public interface OnLearningListener
	{
		void onLearning(Vector vector, float[] errors, boolean learning, boolean stop);
	}

	private Layer[] layers;
	private float learnRatio;

	private float[] outputs;

	private ArrayList<Vector> vectors;
	private float dstError;
	private OnLearningListener listener;
	private boolean stop;
	private Thread thread;

	public Network(Layer[] layers, float learnRatio)
	{
		this.layers = layers;
		this.learnRatio = learnRatio;
	}

	public void randomWeights(float minValue, float maxValue)
	{
		for(Layer layer : layers)
			layer.randomWeights(minValue, maxValue);
	}

	private float[] calc(float[] inputs)
	{
		outputs = inputs;
		for(Layer layer : layers)
			outputs = layer.calc(outputs);
		return outputs;
	}

	private float[] calcWeights(float[] valid)
	{
		float[] networkErrors = new float[valid.length];
		Layer nextLayer = null;
		for(int i = layers.length - 1; i >= 0; i--)
		{
			Layer layer = layers[i];
			float[] errors = new float[layer.getSize()];
			if(i == layers.length - 1)
			{
				if(valid.length != errors.length) throw new RuntimeException("Nieprawidłowa ilość wartości oczekiwanych.");
				for(int j = 0; j < errors.length; j++)
				{
					errors[j] = networkErrors[j] = valid[j] - outputs[j];
				}
			}
			else errors = layer.calcErrors(nextLayer);
			layer.calcWeights(errors, learnRatio);
			nextLayer = layer;
		}
		return networkErrors;
	}

	private void correctWeights()
	{
		for(Layer layer : layers)
			layer.learn();
		outputs = null;
	}

	private float[] learn(float[] valid)
	{
		float[] errors = calcWeights(valid);
		correctWeights();
		return errors;
	}

	public void dumpNetwork(PrintWriter pw)
	{
		pw.println("Sieć neuronowa, współczynnik nauki: " + learnRatio);
		pw.println("Warstwy:");
		for(int i = 0; i < layers.length; i++)
			layers[i].dumpLayer(pw, i);
	}

	public float[] test(Vector vector)
	{
		return calc(vector.getInputs());
	}

	public float[] learnSingle(Vector vector)
	{
		test(vector);
		return learn(vector.getReqOutputs());
	}

	public void learnContinuous(ArrayList<Vector> vectors, float dstError, OnLearningListener listener)
	{
		this.vectors = vectors;
		this.dstError = dstError;
		this.listener = listener;
		this.stop = false;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run()
	{
		boolean learning = true;
		while(learning && !stop)
		{
			float[][] errors = new float[vectors.size()][getOutputsLength()];
			learning = false;
			Collections.shuffle(vectors);
			for(int i = 0; i < vectors.size(); i++)
			{
				errors[i] = learnSingle(vectors.get(i));
				listener.onLearning(vectors.get(i), errors[i], true, stop);
				for(int j = 0; j < errors[i].length; j++)
					if(Math.abs(errors[i][j]) > dstError) learning = true;
			}
		}
		if(!stop) listener.onLearning(null, null, false, stop);
		this.vectors = null;
		this.dstError = -1f;
		this.listener = null;
	}

	public void stopLearning()
	{
		stop = true;
	}

	public int getInputsLength()
	{
		return layers[0].getInputsLength();
	}

	public int getOutputsLength()
	{
		return layers[layers.length - 1].getSize();
	}

	public Layer[] getLayers()
	{
		return layers;
	}
}