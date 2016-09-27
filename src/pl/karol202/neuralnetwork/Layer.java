package pl.karol202.neuralnetwork;

import java.io.PrintWriter;

public class Layer
{
	private Neuron[] neurons;

	public Layer(Neuron[] neurons)
	{
		this.neurons = neurons;
	}

	public void randomWeights(float minValue, float maxValue)
	{
		for(Neuron neuron : neurons) neuron.randomWeights(minValue, maxValue);
	}

	public float[] calc(float[] inputs)
	{
		float[] outputs = new float[neurons.length];
		for(int i = 0; i < neurons.length; i++)
			outputs[i] = neurons[i].calc(inputs);
		return outputs;
	}

	public void calcWeights(float[] errors, float learnRatio)
	{
		if(neurons.length != errors.length) throw new RuntimeException("Nieprawidłowa ilość wartości błędów.");
		for(int i = 0; i < neurons.length; i++)
			neurons[i].calcWeights(errors[i], learnRatio);
	}

	public float[] calcErrors(Layer nextLayer)
	{
		float[] errors = new float[neurons.length];
		for(int i = 0; i < neurons.length; i++)
		{
			float sum = 0f;
			for(int j = 0; j < nextLayer.getSize(); j++)
			{
				Neuron n = nextLayer.neurons[j];
				sum += n.getError() * n.getWeight(i);
			}
			errors[i] = sum;
		}
		return errors;
	}

	public void learn()
	{
		for(Neuron neuron : neurons) neuron.learn();
	}

	public int getSize()
	{
		return neurons.length;
	}

	public int getInputsLength()
	{
		return neurons[0].getInputsLength();
	}

	public Neuron[] getNeurons()
	{
		return neurons;
	}

	public void dumpLayer(PrintWriter pw, int layer)
	{
		pw.println("  Warstwa " + layer);
		for(int i = 0; i < neurons.length; i++)
			neurons[i].dumpNeuron(pw, layer, i);
	}
}