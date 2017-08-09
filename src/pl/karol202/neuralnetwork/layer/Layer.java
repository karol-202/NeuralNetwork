package pl.karol202.neuralnetwork.layer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pl.karol202.neuralnetwork.neuron.Neuron;

import java.io.PrintWriter;

public abstract class Layer<N extends Neuron>
{
	N[] neurons;

	public Layer(N[] neurons)
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

	public void learn(float learnRate, float momentum)
	{
		for(Neuron neuron : neurons) neuron.learn(learnRate, momentum);
	}

	public int getSize()
	{
		return neurons.length;
	}

	public int getInputsLength()
	{
		return neurons[0].getInputsLength();
	}

	public void dumpLayer(PrintWriter pw, int layer)
	{
		pw.println("  Warstwa " + layer);
		for(int i = 0; i < neurons.length; i++)
			neurons[i].dumpNeuron(pw, layer, i);
	}
	
	public void parseLayer(Element elementLayer)
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
	
	public Element saveLayer(Document document)
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