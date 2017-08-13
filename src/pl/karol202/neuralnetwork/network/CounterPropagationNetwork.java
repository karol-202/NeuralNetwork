package pl.karol202.neuralnetwork.network;

import pl.karol202.neuralnetwork.layer.GrossbergLayer;
import pl.karol202.neuralnetwork.layer.KohonenLayer;
import pl.karol202.neuralnetwork.layer.Layer;
import pl.karol202.neuralnetwork.output.OutputType;
import pl.karol202.neuralnetwork.vector.Vector;

public class CounterPropagationNetwork<O, V extends Vector> extends KohonenNetwork<O, Layer, V>
{
	private float grossbergLearnRate;
	private float grossbergLearnRateLoss;
	private float grossbergLearnRateMin;
	
	public CounterPropagationNetwork(KohonenLayer kohonenLayer, GrossbergLayer grossbergLayer, OutputType<O> outputType)
	{
		super(new Layer[] { kohonenLayer, grossbergLayer }, outputType);
	}
	
	@Override
	public KohonenLayer getKohonenLayer()
	{
		return (KohonenLayer) layers[0];
	}
	
	private GrossbergLayer getGrossbergLayer()
	{
		return (GrossbergLayer) layers[1];
	}
	
	public void initWeights(float grossbergMin, float grossbergMax)
	{
		getKohonenLayer().initWeights();
		getGrossbergLayer().randomWeights(grossbergMin, grossbergMax);
	}
	
	@Override
	void learn()
	{
		getKohonenLayer().learn(learnRate, momentum);
		getGrossbergLayer().learn(grossbergLearnRate, momentum);
		
		updateLearnRate();
		updateGrossbergLearnRate();
		outputs = null;
	}
	
	private void updateGrossbergLearnRate()
	{
		if(grossbergLearnRate <= grossbergLearnRateMin) return;
		grossbergLearnRate *= grossbergLearnRateLoss;
		grossbergLearnRate = Math.max(grossbergLearnRate, grossbergLearnRateMin);
	}
	
	public void setGrossbergLearnRate(float learnRate, float learnRateLoss, float learnRateMin)
	{
		this.grossbergLearnRate = learnRate;
		this.grossbergLearnRateLoss = learnRateLoss;
		this.grossbergLearnRateMin = learnRateMin;
	}
}