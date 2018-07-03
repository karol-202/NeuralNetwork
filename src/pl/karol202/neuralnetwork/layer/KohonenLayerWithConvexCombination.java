package pl.karol202.neuralnetwork.layer;

import pl.karol202.neuralnetwork.activation.Activation;
import pl.karol202.neuralnetwork.neuron.KohonenNeuronWithConvexCombination;

public class KohonenLayerWithConvexCombination extends KohonenLayer<KohonenNeuronWithConvexCombination>
{
	private float convexCombinationRate;
	private float combinationRateStep;
	private float initialInputValue;
	
	public KohonenLayerWithConvexCombination(int[] dimensions, int inputs, Activation activation, float combinationRateStep)
	{
		super(new KohonenNeuronWithConvexCombination[calculateNeuronsAmount(dimensions)], dimensions);
		for(int i = 0; i < neurons.length; i++)
			neurons[i] = new KohonenNeuronWithConvexCombination(getPositionOfNeuron(i), inputs, activation);
		this.convexCombinationRate = 0;
		this.combinationRateStep = combinationRateStep;
		this.initialInputValue = (float) Math.sqrt(1f / inputs);
	}
	
	@Override
	public float[] calc(float[] inputs)
	{
		return super.calc(transformInput(inputs));
	}
	
	private float[] transformInput(float[] inputs)
	{
		float[] result = new float[inputs.length];
		for(int i = 0; i < result.length; i++) result[i] = (convexCombinationRate * inputs[i]) + ((1 - convexCombinationRate) * initialInputValue);
		return result;
	}
	
	@Override
	public void learn(float learnRate, float momentum)
	{
		super.learn(learnRate, momentum);
		if(convexCombinationRate >= 1f) return;
		convexCombinationRate += combinationRateStep;
		if(convexCombinationRate > 1f) convexCombinationRate = 1f;
	}
}