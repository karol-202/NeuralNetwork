package pl.karol202.neuralnetwork.neuron;

import pl.karol202.neuralnetwork.activation.Activation;

public class GrossbergNeuron extends SupervisedLearnNeuron
{
	public GrossbergNeuron(int inputs, Activation activation)
	{
		super(inputs, activation);
	}
	
	@Override
	public float calc(float[] inputs)
	{
		if(inputs.length != (weights.length - 1)) throw new RuntimeException("Nieprawidłowa ilość wejść.");
		float sum = 0f;
		for(int i = 0; i < inputs.length; i++) if(inputs[i] == 1) sum += weights[i];
		return sum;
	}
	
	@Override
	public void learn(float learnRate, float momentum)
	{
		for(int i = 0; i < inputs.length; i++)
		{
			if(inputs[i] != 1) continue;
			weights[i] += learnRate * getError() * inputs[i];
		}
		
		clear();
	}
}