package pl.karol202.neuralnetwork.network;

import pl.karol202.neuralnetwork.layer.SimpleDeltaLayerWithBackpropagation;
import pl.karol202.neuralnetwork.output.OutputType;
import pl.karol202.neuralnetwork.vector.SupervisedLearnVector;

import java.util.stream.Stream;

public class SimpleDeltaNetworkWithBackpropagation<O, V extends SupervisedLearnVector>
		extends Network<O, SimpleDeltaLayerWithBackpropagation, V> implements DeltaNetwork<V>
{
	private float[] errors;
	
	public SimpleDeltaNetworkWithBackpropagation(SimpleDeltaLayerWithBackpropagation[] layers, float learnRate, float momentum, OutputType<O> outputType)
	{
		super(layers, learnRate, momentum, outputType);
	}
	
	public void randomWeights(float minValue, float maxValue)
	{
		Stream.of(layers).parallel().forEach(l -> l.randomWeights(minValue, maxValue));
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
		SimpleDeltaLayerWithBackpropagation nextLayer = null;
		for(int i = layers.length - 1; i >= 0; i--)
		{
			SimpleDeltaLayerWithBackpropagation layer = layers[i];
			float[] errors = new float[layer.getSize()];
			if(i == layers.length - 1)
			{
				if(valid.length != errors.length) throw new RuntimeException("Nieprawidłowa ilość wartości oczekiwanych.");
				for(int j = 0; j < errors.length; j++)
					errors[j] = networkErrors[j] = valid[j] - outputs[j];
			}
			else errors = layer.calcErrorsUsingBackpropagation(nextLayer);
			layer.setErrors(errors);
			nextLayer = layer;
		}
		return networkErrors;
	}
	
	public float[] getErrors()
	{
		return errors;
	}
}