package pl.karol202.neuralnetwork.network;

import pl.karol202.neuralnetwork.layer.KohonenLayer;
import pl.karol202.neuralnetwork.neuron.NeuronPosition;
import pl.karol202.neuralnetwork.output.WinnerPositionOutput;
import pl.karol202.neuralnetwork.vector.Vector;

import java.util.stream.Stream;

public class KohonenNetwork<V extends Vector> extends Network<NeuronPosition, KohonenLayer, V>
{
	private float learnRateLoss;
	private float learnRateMin;
	
	public KohonenNetwork(KohonenLayer layer, float learnRate, float momentum)
	{
		super(new KohonenLayer[] { layer }, learnRate, momentum, null);
		this.outputType = new WinnerPositionOutput(this);
		this.learnRateLoss = 1f;
	}
	
	public void initWeights()
	{
		Stream.of(layers).parallel().forEach(KohonenLayer::initWeights);
	}
	
	@Override
	void learn()
	{
		super.learn();
		updateLearnRate();
	}
	
	private void updateLearnRate()
	{
		if(learnRate <= learnRateMin) return;
		learnRate *= learnRateLoss;
		learnRate = Math.max(learnRate, learnRateMin);
	}
	
	public void setLearnRate(float learnRate, float learnRateLoss, float learnRateMin)
	{
		this.learnRate = learnRate;
		this.learnRateLoss = learnRateLoss;
		this.learnRateMin = learnRateMin;
	}
	
	public void setNeighbourhood(float neighbourhoodRadius, float neighbourhoodRadiusLoss, float neighbourhoodRadiusMin)
	{
		layers[0].setNeighbourhood(neighbourhoodRadius, neighbourhoodRadiusLoss, neighbourhoodRadiusMin);
	}
	
	public NeuronPosition getWinnerPosition()
	{
		return layers[0].getWinnerPosition();
	}
	
	public float[] getNeuronWeights(NeuronPosition position)
	{
		return layers[0].getNeuronWeights(position);
	}
}