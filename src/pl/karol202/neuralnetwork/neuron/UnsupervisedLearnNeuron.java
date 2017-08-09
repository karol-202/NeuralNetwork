package pl.karol202.neuralnetwork.neuron;

import pl.karol202.neuralnetwork.activation.Activation;

public class UnsupervisedLearnNeuron extends Neuron
{
	public UnsupervisedLearnNeuron(int inputs, Activation activation)
	{
		super(inputs, activation);
	}
	
	@Override
	public void learn(float learnRate, float momentum)
	{
		for(int i = 0; i < inputs.length; i++)
		{
			float weightDelta = learnRate * inputs[i] * output;
			float weightInertia = previousWeightsDelta[i] * momentum;
			weights[i] += weightDelta + weightInertia;
			previousWeightsDelta[i] = weightDelta + weightInertia;
		}
		float weightDelta = learnRate * output;
		float weightInertia = previousWeightsDelta[inputs.length] * momentum;
		weights[inputs.length] += weightDelta + weightInertia;
		previousWeightsDelta[inputs.length] = weightDelta + weightInertia;
	}
}