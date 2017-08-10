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
		if(output < 0.2f) output *= 0.3f;
		if(output < 0) output *= 0.1f;
		for(int i = 0; i < inputs.length; i++)
		{
			float weightDelta = learnRate * output * (inputs[i] - weights[i]);
			float weightInertia = previousWeightsDelta[i] * momentum;
			weights[i] += weightDelta + weightInertia;
			previousWeightsDelta[i] = weightDelta + weightInertia;
		}
		
		clear();
	}
}