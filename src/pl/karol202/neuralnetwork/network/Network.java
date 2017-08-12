package pl.karol202.neuralnetwork.network;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pl.karol202.neuralnetwork.vector.Vector;
import pl.karol202.neuralnetwork.layer.Layer;
import pl.karol202.neuralnetwork.output.OutputType;

import java.io.PrintWriter;
import java.util.stream.Stream;

public abstract class Network<O, L extends Layer, V extends Vector>
{
	L[] layers;
	float learnRate;
	float momentum;
	OutputType<O> outputType;

	float[] outputs;

	Network(L[] layers, float learnRate, float momentum, OutputType<O> outputType)
	{
		this.layers = layers;
		this.learnRate = learnRate;
		this.momentum = momentum;
		this.outputType = outputType;
	}

	public void randomWeights(float minValue, float maxValue)
	{
		Stream.of(layers).parallel().forEach(l -> l.randomWeights(minValue, maxValue));
	}

	float[] calc(float[] inputs)
	{
		outputs = inputs;
		for(Layer layer : layers) outputs = layer.calc(outputs);
		return outputs;
	}

	void learn()
	{
		Stream.of(layers).parallel().forEach(l -> l.learn(learnRate, momentum));
		
		outputs = null;
	}
	
	public O testVector(V vector)
	{
		if(outputType == null) throw new IllegalStateException("Cannot convert output without output type specified.");
		float[] output = calc(vector.getInputs());
		return outputType.transformOutput(output);
	}

	public void learnVector(V vector)
	{
		calc(vector.getInputs());
		learn();
	}

	public int getInputsLength()
	{
		return layers[0].getInputsLength();
	}

	public int getOutputsLength()
	{
		return layers[layers.length - 1].getSize();
	}
	
	/*public float getLearnRate()
	{
		return learnRate;
	}*/
	
	public void setLearnRate(float learnRate)
	{
		this.learnRate = learnRate;
	}
	
	public void dumpNetwork(PrintWriter pw)
	{
		pw.println("Sieć neuronowa, współczynnik nauki: " + learnRate);
		pw.println("Warstwy:");
		for(int i = 0; i < layers.length; i++)
			layers[i].dumpLayer(pw, i);
	}
	
	public void parseNetwork(Element elementNetwork)
	{
		learnRate = Float.parseFloat(elementNetwork.getAttribute("learnRate"));
		NodeList layersNodes = elementNetwork.getChildNodes();
		for(int i = 0; i < layersNodes.getLength(); i++)
		{
			Element elementLayer = (Element) layersNodes.item(i);
			parseLayer(elementLayer);
		}
	}
	
	private void parseLayer(Element elementLayer)
	{
		int id = Integer.parseInt(elementLayer.getAttribute("id"));
		layers[id].parseLayer(elementLayer);
	}
	
	public Element saveNetwork(Document document)
	{
		Element elementNetwork = document.createElement("network");
		elementNetwork.setAttribute("learnRate", String.valueOf(learnRate));
		for(int i = 0; i < layers.length; i++) elementNetwork.appendChild(saveLayer(document, i));
		return elementNetwork;
	}
	
	private Element saveLayer(Document document, int layer)
	{
		Element elementLayer = layers[layer].saveLayer(document);
		elementLayer.setAttribute("id", String.valueOf(layer));
		return elementLayer;
	}
}