package pl.karol202.neuralnetwork.layer;

import pl.karol202.neuralnetwork.activation.Activation;
import pl.karol202.neuralnetwork.neuron.UnsupervisedLearnNeuron;

import java.util.stream.Stream;

public class UnsupervisedLearnLayer extends Layer<UnsupervisedLearnNeuron>
{
	public UnsupervisedLearnLayer(int neurons, int inputs, Activation activation)
	{
		super(new UnsupervisedLearnNeuron[neurons]);
		for(int i = 0; i < neurons; i++) this.neurons[i] = new UnsupervisedLearnNeuron(inputs, activation);
	}
	
	public void randomWeights(float minValue, float maxValue)
	{
		Stream.of(neurons).parallel().forEach(n -> n.randomWeights(minValue, maxValue));
	}
	
	public float[][] getNeuronsWeights()
	{
		float[][] weights = new float[getSize()][getInputsLength()];
		for(int i = 0; i < weights.length; i++)
			for(int j = 0; j < weights[0].length; j++)
				weights[i][j] = neurons[i].getWeight(j);
		return weights;
	}
}