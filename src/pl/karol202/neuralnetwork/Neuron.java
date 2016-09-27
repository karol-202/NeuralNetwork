package pl.karol202.neuralnetwork;

import java.io.PrintWriter;
import java.util.Random;

public class Neuron
{
	private float[] weights;
	private Activation activation;

	private float[] inputs;
	private float output;
	private float error;
	private float[] newWeights;

	public Neuron(int inputs, Activation activation)
	{
		this.weights = new float[inputs + 1];
		this.activation = activation;
	}

	public void setWeights(float[] weights)
	{
		if(weights.length != this.weights.length) throw new RuntimeException("Nieprawidłowa ilość wag");
		this.weights = weights;
	}

	public void randomWeights(float minValue, float maxValue)
	{
		Random random = new Random();
		float range = maxValue - minValue;
		for(int i = 0; i < weights.length; i++)
		{
			float w;
			do w = (random.nextFloat() * range) - (range / 2);
			while(w == 0);
			weights[i] = w;
		}
	}

	public float calc(float[] inputs)
	{
		if(inputs.length != (weights.length - 1)) throw new RuntimeException("Nieprawidłowa ilość wejść.");
		this.inputs = inputs;
		float sum = 0f;
		for(int i = 0; i < inputs.length; i++)
			sum += inputs[i] * weights[i];
		sum += weights[inputs.length];
		return output = activation.calculate(sum);
	}

	public void calcWeights(float error, float learnRatio)
	{
		this.error = error * activation.calcDerivative(output);
		newWeights = new float[weights.length];
		for(int i = 0; i < inputs.length; i++)
			newWeights[i] = weights[i] + learnRatio * inputs[i] * this.error;
		newWeights[inputs.length] = weights[inputs.length] + learnRatio * this.error;
	}

	public void learn()
	{
		weights = newWeights;
		inputs = null;
		output = 0f;
		error = 0f;
		newWeights = null;
	}

	public float getWeight(int weight)
	{
		return weights[weight];
	}

	public float getError()
	{
		return error;
	}

	public int getInputsLength()
	{
		return weights.length - 1;
	}

	public void dumpNeuron(PrintWriter pw, int layer, int neuron)
	{
		pw.println("    Neuron " + neuron + " w warstwie " + layer);
		activation.dumpActivation(pw);
		pw.println("    Wagi:");
		for(int i = 0; i < weights.length; i++)
			pw.println("      " + weights[i]);
	}
}