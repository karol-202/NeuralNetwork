package pl.karol202.neuralnetwork.layer;

import pl.karol202.neuralnetwork.activation.Activation;
import pl.karol202.neuralnetwork.neuron.KohonenNeuron;
import pl.karol202.neuralnetwork.neuron.NeuronPosition;

import java.util.stream.Stream;

public class KohonenLayer extends Layer<KohonenNeuron>
{
	private int[] dimensions;
	private float neighbourhoodRadius;
	private float neighbourhoodRadiusLoss;
	private float neighbourhoodRadiusMin;
	
	private int winner;
	
	public KohonenLayer(int[] dimensions, int inputs, Activation activation)
	{
		super(new KohonenNeuron[calculateNeuronsAmount(dimensions)]);
		this.dimensions = dimensions;
		for(int i = 0; i < neurons.length; i++) neurons[i] = new KohonenNeuron(getPositionOfNeuron(i), inputs, activation);
	}
	
	private static int calculateNeuronsAmount(int[] dimensions)
	{
		int neurons = 1;
		for(int dimension : dimensions) neurons *= dimension;
		return neurons;
	}
	
	private NeuronPosition getPositionOfNeuron(int neuron)
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
	
	@Override
	public float[] calc(float[] inputs)
	{
		float[] rawOutput = super.calc(inputs);
		
		winner = -1;
		float winnerOutput = 0f;
		for(int i = 0; i < rawOutput.length; i++)
		{
			if(rawOutput[i] <= winnerOutput) continue;
			winner = i;
			winnerOutput = rawOutput[i];
		}
		
		float[] output = new float[rawOutput.length];
		for(int i = 0; i < output.length; i++) output[i] = i == winner ? 1 : 0;
		
		return output;
	}
	
	@Override
	public void learn(float learnRate, float momentum)
	{
		NeuronPosition winnerPosition = getPositionOfNeuron(winner);
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
}