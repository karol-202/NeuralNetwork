package pl.karol202.neuralnetwork.network;

import pl.karol202.neuralnetwork.layer.UnsupervisedLearnLayer;
import pl.karol202.neuralnetwork.output.OutputType;
import pl.karol202.neuralnetwork.vector.Vector;

public class UnsupervisedLearnNetwork<O, V extends Vector> extends Network<O, UnsupervisedLearnLayer, V>
{
	public UnsupervisedLearnNetwork(UnsupervisedLearnLayer[] layers, float learnRate, float momentum, OutputType<O> outputType)
	{
		super(layers, learnRate, momentum, outputType);
	}
}