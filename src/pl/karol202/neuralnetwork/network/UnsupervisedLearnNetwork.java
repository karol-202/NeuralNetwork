package pl.karol202.neuralnetwork.network;

import pl.karol202.neuralnetwork.layer.UnsupervisedLearnLayer;
import pl.karol202.neuralnetwork.output.OutputType;
import pl.karol202.neuralnetwork.vector.Vector;

public class UnsupervisedLearnNetwork<O, V extends Vector> extends Network<O, UnsupervisedLearnLayer, V>
{
	public UnsupervisedLearnNetwork(UnsupervisedLearnLayer layer, float learnRate, float momentum, OutputType<O> outputType)
	{
		super(new UnsupervisedLearnLayer[] { layer }, learnRate, momentum, outputType);
	}
	
	public float[][] getNeuronsWeights()
	{
		return layers[0].getNeuronsWeights();
	}
}