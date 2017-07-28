package pl.karol202.neuralnetwork;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
	
	float[] calcErrorsUsingBackpropagation(Layer nextLayer)
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

	void setErrors(float[] errors)
	{
		if(neurons.length != errors.length) throw new RuntimeException("Nieprawidłowa ilość wartości błędów.");
		for(int i = 0; i < neurons.length; i++)
			neurons[i].setError(errors[i]);
	}

	void learn(float learnRate)
	{
		for(Neuron neuron : neurons) neuron.learn(learnRate);
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
	
	void parseLayer(Element elementLayer)
	{
		NodeList neuronsNodes = elementLayer.getChildNodes();
		for(int i = 0; i < neuronsNodes.getLength(); i++)
		{
			Element elementNeuron = (Element) neuronsNodes.item(i);
			parseNeuron(elementNeuron);
		}
	}
	
	private void parseNeuron(Element elementNeuron)
	{
		int id = Integer.parseInt(elementNeuron.getAttribute("id"));
		neurons[id].parseNeuron(elementNeuron);
	}
	
	Element saveLayer(Document document)
	{
		Element elementLayer = document.createElement("layer");
		for(int i = 0; i < neurons.length; i++) elementLayer.appendChild(saveNeuron(document, i));
		return elementLayer;
	}
	
	private Element saveNeuron(Document document, int neuron)
	{
		Element elementNeuron = neurons[neuron].saveNeuron(document);
		elementNeuron.setAttribute("id", String.valueOf(neuron));
		return elementNeuron;
	}
}