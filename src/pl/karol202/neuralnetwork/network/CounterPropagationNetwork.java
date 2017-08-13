package pl.karol202.neuralnetwork.network;

import pl.karol202.neuralnetwork.layer.GrossbergLayer;
import pl.karol202.neuralnetwork.layer.KohonenLayer;
import pl.karol202.neuralnetwork.layer.Layer;
import pl.karol202.neuralnetwork.output.OutputType;
import pl.karol202.neuralnetwork.vector.SupervisedLearnVector;

public class CounterPropagationNetwork<O, V extends SupervisedLearnVector> extends KohonenNetwork<O, Layer, V> implements DeltaNetwork<V>
{
	private float grossbergLearnRate;
	private float grossbergLearnRateLoss;
	private float grossbergLearnRateMin;
	
	private float[] errors;
	
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
	
	@Override
	public void learnVector(V vector)
	{
		calc(vector.getInputs());
		errors = calcErrors(vector.getReqOutputs());
		learn();
	}
	
	private float[] calcErrors(float[] valid)
	{
		float[] networkErrors = new float[valid.length];
		
		GrossbergLayer layer = getGrossbergLayer();
		float[] errors = new float[layer.getSize()];
		
		if(valid.length != errors.length) throw new RuntimeException("Nieprawidłowa ilość wartości oczekiwanych.");
		for(int j = 0; j < errors.length; j++)
			errors[j] = networkErrors[j] = valid[j] - outputs[j];
			
		layer.setErrors(errors);
		
		return networkErrors;
	}
	
	public void setGrossbergLearnRate(float learnRate, float learnRateLoss, float learnRateMin)
	{
		this.grossbergLearnRate = learnRate;
		this.grossbergLearnRateLoss = learnRateLoss;
		this.grossbergLearnRateMin = learnRateMin;
	}
	
	public float[] getErrors()
	{
		return errors;
	}
}