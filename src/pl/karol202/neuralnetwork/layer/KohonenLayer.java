package pl.karol202.neuralnetwork.layer;

import pl.karol202.neuralnetwork.neuron.KohonenNeuron;
import pl.karol202.neuralnetwork.neuron.NeuronPosition;

import java.util.stream.Stream;

public abstract class KohonenLayer<N extends KohonenNeuron> extends Layer<N>
{
	private int[] dimensions;
	private float neighbourhoodRadius;
	private float neighbourhoodRadiusLoss;
	private float neighbourhoodRadiusMin;
	
	private NeuronPosition winnerPosition;
	
	public KohonenLayer(N[] neurons, int[] dimensions)
	{
		super(neurons);
		this.dimensions = dimensions;
	}
	
	static int calculateNeuronsAmount(int[] dimensions)
	{
		int neurons = 1;
		for(int dimension : dimensions) neurons *= dimension;
		return neurons;
	}
	
	NeuronPosition getPositionOfNeuron(int neuron)
	{
		int[] coords = new int[dimensions.length];
		for(int dimension = 0; dimension < coords.length; dimension++)
		{
			int d = 1;
			for(int i = dimension + 1; i < coords.length; i++) d *= dimensions[i];
			coords[dimension] = (int) Math.floor(neuron / d) % dimensions[dimension];
		}
		return new NeuronPosition(coords);
	}
	
	private KohonenNeuron getNeuronAtPosition(NeuronPosition position)
	{
		if(position.getDimensions() != dimensions.length) throw new IllegalArgumentException("Invalid dimensions amount");
		int neuron = 0;
		for(int dimension = 0; dimension < dimensions.length; dimension++)
		{
			int d = 1;
			for(int i = dimension + 1; i < dimensions.length; i++) d *= dimensions[i];
			neuron += position.getCoord(dimension) * d;
		}
		return neurons[neuron];
	}
	
	//Use initWeights() instead.
	@Deprecated
	@Override
	public void randomWeights(float minValue, float maxValue)
	{
		super.randomWeights(minValue, maxValue);
	}
	
	public abstract void initWeights();
	
	@Override
	public float[] calc(float[] inputs)
	{
		float[] rawOutput = super.calc(inputs);
		
		int winner = -1;
		float winnerOutput = 0f;
		for(int i = 0; i < rawOutput.length; i++)
		{
			if(rawOutput[i] <= winnerOutput) continue;
			winner = i;
			winnerOutput = rawOutput[i];
		}
		
		float[] output = new float[rawOutput.length];
		for(int i = 0; i < output.length; i++) output[i] = i == winner ? 1 : 0;
		
		winnerPosition = getPositionOfNeuron(winner);
		
		return output;
	}
	
	@Override
	public void learn(float learnRate, float momentum)
	{
		Stream.of(neurons).parallel().forEach(n -> n.updateNeighbourhood(winnerPosition, neighbourhoodRadius));
		super.learn(learnRate, momentum);
		updateNeighbourhood();
	}
	
	private void updateNeighbourhood()
	{
		if(neighbourhoodRadius <= neighbourhoodRadiusMin) return;
		neighbourhoodRadius *= neighbourhoodRadiusLoss;
		neighbourhoodRadius = Math.max(neighbourhoodRadius, neighbourhoodRadiusMin);
	}
	
	public void setNeighbourhood(float neighbourhoodRadius, float neighbourhoodRadiusLoss, float neighbourhoodRadiusMin)
	{
		this.neighbourhoodRadius = neighbourhoodRadius;
		this.neighbourhoodRadiusLoss = neighbourhoodRadiusLoss;
		this.neighbourhoodRadiusMin = neighbourhoodRadiusMin;
	}
	
	public NeuronPosition getWinnerPosition()
	{
		return winnerPosition;
	}
	
	public float[] getNeuronWeights(NeuronPosition position)
	{
		KohonenNeuron neuron = getNeuronAtPosition(position);
		float[] weights = new float[getInputsLength()];
		for(int i = 0; i < weights.length; i++) weights[i] = neuron.getWeight(i);
		return weights;
	}
}