package pl.karol202.neuralnetwork.neuron;

import pl.karol202.neuralnetwork.activation.Activation;

public class KohonenNeuronWithConvexCombination extends KohonenNeuron
{
	public KohonenNeuronWithConvexCombination(NeuronPosition position, int inputs, Activation activation)
	{
		super(position, inputs, activation);
	}
	
	public void initWeights()
	{
		float value = (float) Math.sqrt(1f / getInputsLength());
		for(int i = 0; i < weights.length - 1; i++) weights[i] = value;
	}
}