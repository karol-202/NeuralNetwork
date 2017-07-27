package pl.karol202.neuralnetwork;

import java.io.PrintWriter;
import java.util.Random;

public class Neuron
{
	private float[] weights;
	private Activation activation;

	private float[] inputs;
	private float output;
	private float transformedError;

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

	void randomWeights(float minValue, float maxValue)
	{
		Random random = new Random();
		float range = maxValue - minValue;
		for(int i = 0; i < weights.length; i++)
		{
			float weight;
			do weight = (random.nextFloat() * range) - (range / 2);
			while(weight == 0);
			weights[i] = weight;
		}
	}

	float calc(float[] inputs)
	{
		if(inputs.length != (weights.length - 1)) throw new RuntimeException("Nieprawidłowa ilość wejść.");
		this.inputs = inputs;
		float sum = 0f;
		for(int i = 0; i < inputs.length; i++)
			sum += inputs[i] * weights[i];
		sum += weights[inputs.length];
		return output = activation.calculate(sum);
	}

	void setError(float error)
	{
		transformedError = error * activation.calcDerivative(output);
	}

	void learn(float learnRate)
	{
		for(int i = 0; i < inputs.length; i++)
			weights[i] += learnRate * inputs[i] * transformedError;
		weights[inputs.length] += learnRate * transformedError;
		
		clear();
	}
	
	private void clear()
	{
		inputs = null;
		output = 0f;
		transformedError = 0f;
	}
	
	public int getInputsLength()
	{
		return weights.length - 1;
	}

	public float getWeight(int weight)
	{
		return weights[weight];
	}

	float getError()
	{
		return transformedError;
	}
	
	void dumpNeuron(PrintWriter pw, int layer, int neuron)
	{
		pw.println("    Neuron " + neuron + " w warstwie " + layer);
		activation.dumpActivation(pw);
		pw.println("    Wagi:");
		for(float weight : weights) pw.println("      " + weight);
	}
}