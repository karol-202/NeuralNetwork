package pl.karol202.neuralnetwork.neuron;

import pl.karol202.neuralnetwork.activation.Activation;

import java.util.Random;

public class StandardKohonenNeuron extends KohonenNeuron
{
	public StandardKohonenNeuron(NeuronPosition position, int inputs, Activation activation)
	{
		super(position, inputs, activation);
	}
	
	@Override
	public void initWeights()
	{
		Random random = new Random();
		for(int i = 0; i < weights.length - 1; i++)
		{
			float weight;
			do weight = random.nextFloat();
			while(weight == 0);
			weights[i] = weight;
		}
		float length = calcWeightsVectorLength();
		for(int i = 0; i < weights.length; i++) weights[i] = weights[i] / length;
	}
	
	private float calcWeightsVectorLength()
	{
		float sum = 0;
		for(float weight : weights) sum += weight * weight;
		return (float) Math.sqrt(sum);
	}
}