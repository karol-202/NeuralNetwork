package pl.karol202.neuralnetwork;

import java.io.PrintWriter;
import java.util.stream.Stream;

public class Network
{
	private Layer[] layers;
	private float learnRate;

	private float[] outputs;

	public Network(Layer[] layers, float learnRate)
	{
		this.layers = layers;
		this.learnRate = learnRate;
	}

	public void randomWeights(float minValue, float maxValue)
	{
		Stream.of(layers).forEach(l -> l.randomWeights(minValue, maxValue));
	}

	private float[] calc(float[] inputs)
	{
		outputs = inputs;
		for(Layer layer : layers)
			outputs = layer.calc(outputs);
		return outputs;
	}

	private float[] calcErrors(float[] valid)
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
			else errors = layer.calcErrorsUsingBackpropagation(nextLayer);
			layer.setErrors(errors);
			nextLayer = layer;
		}
		return networkErrors;
	}

	private float[] learn(float[] valid)
	{
		float[] errors = calcErrors(valid);
		for(Layer layer : layers) layer.learn(learnRate);
		
		outputs = null;
		return errors;
	}
	
	public float[] testVector(Vector vector)
	{
		return calc(vector.getInputs());
	}

	public float[] learnVector(Vector vector)
	{
		calc(vector.getInputs());
		return learn(vector.getReqOutputs());
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
	
	public void dumpNetwork(PrintWriter pw)
	{
		pw.println("Sieć neuronowa, współczynnik nauki: " + learnRate);
		pw.println("Warstwy:");
		for(int i = 0; i < layers.length; i++)
			layers[i].dumpLayer(pw, i);
	}
}