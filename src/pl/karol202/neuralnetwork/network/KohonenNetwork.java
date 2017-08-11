package pl.karol202.neuralnetwork.network;

import pl.karol202.neuralnetwork.layer.KohonenLayer;
import pl.karol202.neuralnetwork.output.OutputType;
import pl.karol202.neuralnetwork.vector.Vector;

public class KohonenNetwork<O, V extends Vector> extends Network<O, KohonenLayer, V>
{
	private float learnRateLoss;
	private float learnRateMin;
	
	KohonenNetwork(KohonenLayer layer, float learnRate, float momentum, OutputType<O> outputType)
	{
		super(new KohonenLayer[] { layer }, learnRate, momentum, outputType);
		this.learnRateLoss = 1f;
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
	
	public void setLearnRateLoss(float learnRateLoss, float learnRateMin)
	{
		this.learnRateLoss = learnRateLoss;
		this.learnRateMin = learnRateMin;
	}
	
	public void setNeighbourhood(float neighbourhoodRadius, float neighbourhoodRadiusLoss, float neighbourhoodRadiusMin)
	{
		layers[0].setNeighbourhood(neighbourhoodRadius, neighbourhoodRadiusLoss, neighbourhoodRadiusMin);
	}
}