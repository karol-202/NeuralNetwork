package pl.karol202.neuralnetwork.layer;

import pl.karol202.neuralnetwork.neuron.SupervisedLearnNeuron;

public class SupervisedLearnLayer extends Layer<SupervisedLearnNeuron>
{
	public SupervisedLearnLayer(SupervisedLearnNeuron[] neurons)
	{
		super(neurons);
	}
	
	public float[] calcErrorsUsingBackpropagation(SupervisedLearnLayer nextLayer)
	{
		float[] errors = new float[neurons.length];
		for(int i = 0; i < neurons.length; i++)
		{
			float sum = 0f;
			for(int j = 0; j < nextLayer.getSize(); j++)
			{
				SupervisedLearnNeuron n = nextLayer.neurons[j];
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