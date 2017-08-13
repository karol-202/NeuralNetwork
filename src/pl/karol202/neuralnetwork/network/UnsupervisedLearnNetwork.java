package pl.karol202.neuralnetwork.network;

import pl.karol202.neuralnetwork.layer.UnsupervisedLearnLayer;
import pl.karol202.neuralnetwork.output.OutputType;
import pl.karol202.neuralnetwork.vector.Vector;

import java.util.stream.Stream;

public class UnsupervisedLearnNetwork<O, V extends Vector> extends Network<O, UnsupervisedLearnLayer, V>
{
	public UnsupervisedLearnNetwork(UnsupervisedLearnLayer layer, float learnRate, float momentum, OutputType<O> outputType)
	{
		super(new UnsupervisedLearnLayer[] { layer }, learnRate, momentum, outputType);
	}
	
	public void randomWeights(float minValue, float maxValue)
	{
		Stream.of(layers).parallel().forEach(l -> l.randomWeights(minValue, maxValue));
	}
	
	public float[][] getNeuronsWeights()
	{
		return layers[0].getNeuronsWeights();
	}
}