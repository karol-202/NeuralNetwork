package pl.karol202.neuralnetwork.layer;

import pl.karol202.neuralnetwork.neuron.UnsupervisedLearnNeuron;

public class UnsupervisedLearnLayer extends Layer<UnsupervisedLearnNeuron>
{
	public UnsupervisedLearnLayer(UnsupervisedLearnNeuron[] neurons)
	{
		super(neurons);
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