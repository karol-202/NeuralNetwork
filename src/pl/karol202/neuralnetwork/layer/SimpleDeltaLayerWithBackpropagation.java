package pl.karol202.neuralnetwork.layer;

import pl.karol202.neuralnetwork.activation.Activation;
import pl.karol202.neuralnetwork.neuron.SimpleDeltaNeuron;

import java.util.stream.Stream;

public class SimpleDeltaLayerWithBackpropagation extends Layer<SimpleDeltaNeuron>
{
	public SimpleDeltaLayerWithBackpropagation(SimpleDeltaNeuron[] neurons)
	{
		super(neurons);
	}
	
	public SimpleDeltaLayerWithBackpropagation(int neurons, int inputs, Activation activation)
	{
		super(new SimpleDeltaNeuron[neurons]);
		for(int i = 0; i < neurons; i++) this.neurons[i] = new SimpleDeltaNeuron(inputs, activation);
	}
	
	public void randomWeights(float minValue, float maxValue)
	{
		Stream.of(neurons).parallel().forEach(n -> n.randomWeights(minValue, maxValue));
	}
	
	public float[] calcErrorsUsingBackpropagation(SimpleDeltaLayerWithBackpropagation nextLayer)
	{
		float[] errors = new float[neurons.length];
		for(int i = 0; i < neurons.length; i++)
		{
			float sum = 0f;
			for(int j = 0; j < nextLayer.getSize(); j++)
			{
				SimpleDeltaNeuron n = nextLayer.neurons[j];
				sum += n.getError() * n.getWeight(i);
			}
			errors[i] = sum;
		}
		return errors;
	}
	
	public void setErrors(float[] errors)
	{
		if(neurons.length != errors.length) throw new RuntimeException("Nieprawidłowa ilość wartości błędów.");
		for(int i = 0; i < neurons.length; i++)
			neurons[i].setError(errors[i]);
	}
}