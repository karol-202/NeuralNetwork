package pl.karol202.neuralnetwork.network;

import pl.karol202.neuralnetwork.layer.SupervisedLearnLayer;
import pl.karol202.neuralnetwork.output.OutputType;
import pl.karol202.neuralnetwork.vector.SupervisedLearnVector;

public class SupervisedLearnNetwork<O, V extends SupervisedLearnVector> extends Network<O, SupervisedLearnLayer, V>
{
	private float[] errors;
	
	public SupervisedLearnNetwork(SupervisedLearnLayer[] layers, float learnRate, float momentum, OutputType<O> outputType)
	{
		super(layers, learnRate, momentum, outputType);
	}
	
	private float[] calcErrors(float[] valid)
	{
		float[] networkErrors = new float[valid.length];
		SupervisedLearnLayer nextLayer = null;
		for(int i = layers.length - 1; i >= 0; i--)
		{
			SupervisedLearnLayer layer = layers[i];
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
	
	public void learnVector(V vector)
	{
		calc(vector.getInputs());
		errors = calcErrors(vector.getReqOutputs());
		learn();
	}
	
	public float[] getErrors()
	{
		return errors;
	}
}