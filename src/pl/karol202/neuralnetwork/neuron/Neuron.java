package pl.karol202.neuralnetwork.neuron;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pl.karol202.neuralnetwork.activation.Activation;

import java.io.PrintWriter;
import java.util.Random;

public abstract class Neuron
{
	float[] weights;
	Activation activation;
	
	float[] previousWeightsDelta;
	
	float[] inputs;
	float output;

	Neuron(int inputs, Activation activation)
	{
		this.weights = new float[inputs + 1];
		this.activation = activation;
		
		this.previousWeightsDelta = new float[weights.length];
	}

	public void randomWeights(float minValue, float maxValue)
	{
		Random random = new Random();
		float range = maxValue - minValue;
		for(int i = 0; i < weights.length; i++)
		{
			float weight;
			do weight = (random.nextFloat() * range) - (range / 2);
			while(weight == 0);
			weights[i] = weight;
		}
	}

	public float calc(float[] inputs)
	{
		if(inputs.length != (weights.length - 1)) throw new RuntimeException("Nieprawidłowa ilość wejść.");
		this.inputs = inputs;
		float sum = 0f;
		for(int i = 0; i < inputs.length; i++)
			sum += inputs[i] * weights[i];
		sum += weights[inputs.length];
		return output = activation.calculate(sum);
	}
	
	public abstract void learn(float learnRate, float momentum);
	
	void clear()
	{
		inputs = null;
		output = 0f;
	}
	
	public int getInputsLength()
	{
		return weights.length - 1;
	}

	public float getWeight(int weight)
	{
		return weights[weight];
	}
	
	public void dumpNeuron(PrintWriter pw, int layer, int neuron)
	{
		pw.println("    Neuron " + neuron + " w warstwie " + layer);
		activation.dumpActivation(pw);
		pw.println("    Wagi:");
		for(float weight : weights) pw.println("      " + weight);
	}
	
	public void parseNeuron(Element elementNeuron)
	{
		NodeList weightsNodes = elementNeuron.getChildNodes();
		for(int i = 0; i < weightsNodes.getLength(); i++)
		{
			Element elementWeight = (Element) weightsNodes.item(i);
			parseWeight(elementWeight);
		}
	}
	
	private void parseWeight(Element elementWeight)
	{
		int id = Integer.parseInt(elementWeight.getAttribute("id"));
		float value = Float.parseFloat(elementWeight.getAttribute("value"));
		weights[id] = value;
	}
	
	public Element saveNeuron(Document document)
	{
		Element elementNeuron = document.createElement("neuron");
		for(int i = 0; i < weights.length; i++) elementNeuron.appendChild(saveWeight(document, i));
		return elementNeuron;
	}
	
	private Element saveWeight(Document document, int weight)
	{
		Element elementWeight = document.createElement("weight");
		elementWeight.setAttribute("id", String.valueOf(weight));
		elementWeight.setAttribute("value", String.valueOf(weights[weight]));
		return elementWeight;
	}
}