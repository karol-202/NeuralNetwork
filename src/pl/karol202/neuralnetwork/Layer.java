package pl.karol202.neuralnetwork;

import java.io.PrintWriter;

public class Layer
{
	private Neuron[] neurons;

	public Layer(Neuron[] neurons)
	{
		this.neurons = neurons;
	}

	void randomWeights(float minValue, float maxValue)
	{
		for(Neuron neuron : neurons) neuron.randomWeights(minValue, maxValue);
	}

	float[] calc(float[] inputs)
	{
		float[] outputs = new float[neurons.length];
		for(int i = 0; i < neurons.length; i++)
			outputs[i] = neurons[i].calc(inputs);
		return outputs;
	}

	void calcWeights(float[] errors, float learnRatio)
	{
		if(neurons.length != errors.length) throw new RuntimeException("Nieprawidłowa ilość wartości błędów.");
		for(int i = 0; i < neurons.length; i++)
			neurons[i].calcWeights(errors[i], learnRatio);
	}

	float[] calcErrors(Layer nextLayer)
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

	void learn()
	{
		for(Neuron neuron : neurons) neuron.learn();
	}

	int getSize()
	{
		return neurons.length;
	}

	int getInputsLength()
	{
		return neurons[0].getInputsLength();
	}

	public Neuron[] getNeurons()
	{
		return neurons;
	}

	void dumpLayer(PrintWriter pw, int layer)
	{
		pw.println("  Warstwa " + layer);
		for(int i = 0; i < neurons.length; i++)
			neurons[i].dumpNeuron(pw, layer, i);
	}
}