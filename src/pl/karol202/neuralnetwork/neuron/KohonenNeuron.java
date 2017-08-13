package pl.karol202.neuralnetwork.neuron;

import pl.karol202.neuralnetwork.activation.Activation;

public abstract class KohonenNeuron extends Neuron
{
	private NeuronPosition position;
	
	private float neighbourhoodMultiplier;
	
	KohonenNeuron(NeuronPosition position, int inputs, Activation activation)
	{
		super(inputs, activation);
		this.position = position;
	}
	
	public abstract void initWeights();
	
	@Override
	public void learn(float learnRate, float momentum)
	{
		if(neighbourhoodMultiplier < 0.001f) return;
		for(int i = 0; i < inputs.length; i++)
		{
			float weightDelta = learnRate * (inputs[i] - weights[i]) * neighbourhoodMultiplier;
			float weightInertia = previousWeightsDelta[i] * momentum;
			weights[i] += weightDelta + weightInertia;
			previousWeightsDelta[i] = weightDelta + weightInertia;
		}
		
		clear();
	}
	
	public void updateNeighbourhood(NeuronPosition winner, float neighbourhoodRadius)
	{
		neighbourhoodMultiplier = (float) Math.exp(-Math.pow(position.getDistance(winner), 2) / (2 * Math.pow(neighbourhoodRadius, 2)));
	}
}