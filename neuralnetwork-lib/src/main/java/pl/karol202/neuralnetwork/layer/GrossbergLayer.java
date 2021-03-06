package pl.karol202.neuralnetwork.layer;

import pl.karol202.neuralnetwork.activation.Activation;
import pl.karol202.neuralnetwork.neuron.GrossbergNeuron;

import java.util.stream.Stream;

public class GrossbergLayer extends Layer<GrossbergNeuron>
{
	public GrossbergLayer(int neurons, int inputs, Activation activation)
	{
		super(new GrossbergNeuron[neurons]);
		for(int i = 0; i < neurons; i++) this.neurons[i] = new GrossbergNeuron(inputs, activation);
	}
	
	public void randomWeights(float minValue, float maxValue)
	{
		Stream.of(neurons).parallel().forEach(n -> n.randomWeights(minValue, maxValue));
	}
	
	public void setErrors(float[] errors)
	{
		if(neurons.length != errors.length) throw new RuntimeException("Nieprawidłowa ilość wartości błędów.");
		for(int i = 0; i < neurons.length; i++) neurons[i].setError(errors[i]);
	}
}