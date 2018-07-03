package pl.karol202.neuralnetwork.network;

import pl.karol202.neuralnetwork.layer.KohonenLayer;
import pl.karol202.neuralnetwork.neuron.NeuronPosition;
import pl.karol202.neuralnetwork.output.WinnerPositionOutput;
import pl.karol202.neuralnetwork.vector.Vector;

public class BasicKohonenNetwork<V extends Vector> extends KohonenNetwork<NeuronPosition, KohonenLayer, V>
{
	public BasicKohonenNetwork(KohonenLayer layer)
	{
		super(new KohonenLayer[] { layer }, null);
		this.outputType = new WinnerPositionOutput(this);
	}
	
	@Override
	public KohonenLayer getKohonenLayer()
	{
		return layers[0];
	}
	
	public void initWeights()
	{
		getKohonenLayer().initWeights();
	}
}