package pl.karol202.neuralnetwork.network;

import pl.karol202.neuralnetwork.layer.KohonenLayer;
import pl.karol202.neuralnetwork.layer.Layer;
import pl.karol202.neuralnetwork.neuron.NeuronPosition;
import pl.karol202.neuralnetwork.output.OutputType;
import pl.karol202.neuralnetwork.vector.Vector;

public abstract class KohonenNetwork<O, L extends Layer, V extends Vector> extends Network<O, L, V>
{
	private float learnRateLoss;
	private float learnRateMin;
	
	KohonenNetwork(L[] layers, OutputType<O> outputType)
	{
		super(layers, 0f, 0f, outputType);
	}
	
	public abstract KohonenLayer getKohonenLayer();
	
	@Override
	void learn()
	{
		super.learn();
		updateLearnRate();
	}
	
	void updateLearnRate()
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
	
	public void setMomentum(float momentum)
	{
		this.momentum = momentum;
	}
	
	public void setNeighbourhood(float neighbourhoodRadius, float neighbourhoodRadiusLoss, float neighbourhoodRadiusMin)
	{
		getKohonenLayer().setNeighbourhood(neighbourhoodRadius, neighbourhoodRadiusLoss, neighbourhoodRadiusMin);
	}
	
	public NeuronPosition getWinnerPosition()
	{
		return getKohonenLayer().getWinnerPosition();
	}
	
	public float[] getNeuronWeights(NeuronPosition position)
	{
		return getKohonenLayer().getNeuronWeights(position);
	}
}