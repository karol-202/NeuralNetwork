package pl.karol202.neuralnetwork.neuron;

import pl.karol202.neuralnetwork.activation.Activation;

public class HebbNeuron extends Neuron
{
	public HebbNeuron(int inputs, Activation activation)
	{
		super(inputs, activation);
	}
	
	@Override
	public void randomWeights(float minValue, float maxValue)
	{
		super.randomWeights(minValue, maxValue);
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
		/*float weightDelta = learnRate * output * (1 - weights[inputs.length]);
		float weightInertia = previousWeightsDelta[inputs.length] * momentum;
		weights[inputs.length] += weightDelta + weightInertia;
		previousWeightsDelta[inputs.length] = weightDelta + weightInertia;*/
		
		clear();
	}
}