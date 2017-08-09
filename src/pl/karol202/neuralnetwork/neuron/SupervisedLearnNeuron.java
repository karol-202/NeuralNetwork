package pl.karol202.neuralnetwork.neuron;

import pl.karol202.neuralnetwork.activation.Activation;

public class SupervisedLearnNeuron extends Neuron
{
	private float transformedError;
	
	public SupervisedLearnNeuron(int inputs, Activation activation)
	{
		super(inputs, activation);
	}
	
	@Override
	public void learn(float learnRate, float momentum)
	{
		for(int i = 0; i < inputs.length; i++)
		{
			float weightDelta = learnRate * inputs[i] * transformedError;
			float weightInertia = previousWeightsDelta[i] * momentum;
			weights[i] += weightDelta + weightInertia;
			previousWeightsDelta[i] = weightDelta + weightInertia;
		}
		float weightDelta = learnRate * transformedError;
		float weightInertia = previousWeightsDelta[inputs.length] * momentum;
		weights[inputs.length] += weightDelta + weightInertia;
		previousWeightsDelta[inputs.length] = weightDelta + weightInertia;
		
		clear();
	}
	
	@Override
	protected void clear()
	{
		super.clear();
		transformedError = 0f;
	}
	
	public float getError()
	{
		return transformedError;
	}
	
	public void setError(float error)
	{
		transformedError = error * activation.calcDerivative(output);
	}
}