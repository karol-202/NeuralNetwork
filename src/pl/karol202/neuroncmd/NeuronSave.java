package pl.karol202.neuroncmd;

import pl.karol202.neuralnetwork.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class NeuronSave
{
	private enum NetworkRS
	{
		NONE, NETWORK, LAYER, NEURON, ACTIVATION, ACTIVATION_PARAM
	}

	private enum VectorsRS
	{
		NONE, VECTORS, VECTOR, INPUT, REQ_OUTPUT
	}

	private enum DataRS
	{
		NONE, DATA, WEIGHT
	}
	
	static Network loadNetwork(String file) throws XMLStreamException, FileNotFoundException
	{
		FileInputStream fis = new FileInputStream(file);
		XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(fis);

		NetworkRS state = NetworkRS.NONE;
		float learnRatio = 0f;
		ArrayList<Layer> layers = null;
		int layerInputs = 0;
		ArrayList<Neuron> layerNeurons = null;
		int neuronInputs = 0;
		String activationType = "";
		Activation activation = null;
		HashMap<String, String> activationParams = null;
		String activationParamName = "";
		String activationParamValue = "";

		while(reader.hasNext())
		{
			XMLEvent event = reader.nextEvent();
			if(event.isStartElement())
			{
				StartElement startElement = event.asStartElement();
				if(startElement.getName().toString().equals("network"))
				{
					if(state != NetworkRS.NONE)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu rozpoczynającego: network");
					state = NetworkRS.NETWORK;

					layerInputs = -1;
					learnRatio = -1;

					Iterator<Attribute> attrs = startElement.getAttributes();
					while(attrs.hasNext())
					{
						Attribute attr = attrs.next();
						if(attr.getName().toString().equals("inputs"))
							layerInputs = Integer.parseInt(attr.getValue());
						else if(attr.getName().toString().equals("learnRatio"))
							learnRatio = Float.parseFloat(attr.getValue());
					}
					if(layerInputs == -1)
						throw new RuntimeException("Błąd parsowania pliku: brak atrybutu: inputs");
					if(learnRatio == -1)
						throw new RuntimeException("Błąd parsowania pliku: brak atrybutu: learnRatio");
					layers = new ArrayList<Layer>();
				}
				else if(startElement.getName().toString().equals("layer"))
				{
					if(state != NetworkRS.NETWORK)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu rozpoczynającego: layer");
					state = NetworkRS.LAYER;
					layerNeurons = new ArrayList<Neuron>();
				}
				else if(startElement.getName().toString().equals("neuron"))
				{
					if(state != NetworkRS.LAYER)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu rozpoczynającego: neuron");
					state = NetworkRS.NEURON;

					neuronInputs = -1;

					Iterator<Attribute> attrs = startElement.getAttributes();
					while(attrs.hasNext())
					{
						Attribute attr = attrs.next();
						if(attr.getName().toString().equals("inputs"))
							neuronInputs = Integer.parseInt(attr.getValue());
					}
					if(neuronInputs == -1)
						throw new RuntimeException("Błąd parsowania pliku: brak atrybutu: inputs");
				}
				else if(startElement.getName().toString().equals("activation"))
				{
					if(state != NetworkRS.NEURON)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu rozpoczynającego: activation");
					state = NetworkRS.ACTIVATION;

					activationType = "";

					Iterator<Attribute> attrs = startElement.getAttributes();
					while(attrs.hasNext())
					{
						Attribute attr = attrs.next();
						if(attr.getName().toString().equals("type"))
							activationType = attr.getValue();
					}
					if(activationType == "")
						throw new RuntimeException("Błąd parsowania pliku: brak atrybutu: type");
					activationParams = new HashMap<String, String>();
				}
				else if(state == NetworkRS.ACTIVATION)
				{
					state = NetworkRS.ACTIVATION_PARAM;
					activationParamName = startElement.getName().toString();
				}
			}
			else if(event.isEndElement())
			{
				EndElement endElement = event.asEndElement();
				if(endElement.getName().toString().equals("network"))
				{
					if(state != NetworkRS.NETWORK)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: network");
					Layer[] layerArray = layers.toArray(new Layer[layers.size()]);
					return new Network(layerArray, learnRatio);
				}
				else if(endElement.getName().toString().equals("layer"))
				{
					if(state != NetworkRS.LAYER)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: layer");
					state = NetworkRS.NETWORK;
					Neuron[] neurons = layerNeurons.toArray(new Neuron[layerNeurons.size()]);
					Layer layer = new Layer(neurons);
					layers.add(layer);
					layerInputs = neurons.length;
				}
				else if(endElement.getName().toString().equals("neuron"))
				{
					if(state != NetworkRS.NEURON)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: neuron");
					state = NetworkRS.LAYER;
					if(neuronInputs != layerInputs)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowa ilość wag neuronu");
					Neuron neuron = new Neuron(neuronInputs, activation);
					layerNeurons.add(neuron);
				}
				else if(endElement.getName().toString().equals("activation"))
				{
					if(state != NetworkRS.ACTIVATION)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: activation");
					state = NetworkRS.NEURON;
					switch(activationType)
					{
					case "sigmoidal":
						if(!activationParams.containsKey("alpha"))
							throw new RuntimeException("Błąd parsowania pliku: brak atrybutu: alpha");
						float alpha = Float.parseFloat(activationParams.get("alpha"));
						activation = new ActivationSigmoidal(alpha);
						break;
					case "linear":
						activation = new ActivationLinear();
						break;
					}
				}
				else if(endElement.getName().toString().equals(activationParamName))
				{
					if(state != NetworkRS.ACTIVATION_PARAM)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: " + activationParamName);
					state = NetworkRS.ACTIVATION;
					activationParams.put(activationParamName, activationParamValue);
				}
			}
			else if(event.isCharacters())
			{
				String chars = event.asCharacters().getData();
				if(state == NetworkRS.ACTIVATION_PARAM)
					activationParamValue = chars;
			}
		}
		reader.close();
		return null;
	}

	static ArrayList<Vector> loadVector(String file) throws XMLStreamException, FileNotFoundException
	{
		FileInputStream fis = new FileInputStream(file);
		XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(fis);

		VectorsRS level = VectorsRS.NONE;
		ArrayList<Vector> vectors = null;
		Vector vector = null;
		ArrayList<Float> inputs = null;
		ArrayList<Float> outputs = null;
		float input = 0;
		float output = 0;

		while(reader.hasNext())
		{
			XMLEvent event = reader.nextEvent();
			if(event.isStartElement())
			{
				StartElement startElement = event.asStartElement();
				if(startElement.getName().toString().equals("vectors"))
				{
					if(level != VectorsRS.NONE)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu rozpoczynającego: vectors");
					level = VectorsRS.VECTORS;

					vectors = new ArrayList<Vector>();
				}
				else if(startElement.getName().toString().equals("vector"))
				{
					if(level != VectorsRS.VECTORS)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu rozpoczynającego: vector");
					level = VectorsRS.VECTOR;

					vector = new Vector();
					inputs = new ArrayList<Float>();
					outputs = new ArrayList<Float>();
				}
				else if(startElement.getName().toString().equals("input"))
				{
					if(level != VectorsRS.VECTOR)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu rozpoczynającego: input");
					level = VectorsRS.INPUT;
				}
				else if(startElement.getName().toString().equals("reqOutput"))
				{
					if(level != VectorsRS.VECTOR)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu rozpoczynającego: reqOutput");
					level = VectorsRS.REQ_OUTPUT;
				}
			}
			else if(event.isEndElement())
			{
				EndElement endElement = event.asEndElement();
				if(endElement.getName().toString().equals("vectors"))
				{
					if(level != VectorsRS.VECTORS)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: vectors");
					break;
				}
				else if(endElement.getName().toString().equals("vector"))
				{
					if(level != VectorsRS.VECTOR)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: vector");
					level = VectorsRS.VECTORS;

					//if(inputs.size() != network.getInputsAmount())
					// throw new RuntimeException("Bład parsowania pliku: nieprawidłowa ilość wejść wektora.");
					float[] inputsArray = new float[inputs.size()];
					for(int i = 0; i < inputs.size(); i++) inputsArray[i] = inputs.get(i);
					vector.setInputs(inputsArray);

					//if(outputs.size() != network.getOutputsAmount())
					// throw new RuntimeException("Bład parsowania pliku: nieprawidłowa ilość wyjść wektora.");
					float[] outputsArray = new float[outputs.size()];
					for(int i = 0; i < outputs.size(); i++) outputsArray[i] = outputs.get(i);
					vector.setReqOutputs(outputsArray);

					vectors.add(vector);
					vector = null;
					inputs = null;
					outputs = null;
				}
				else if(endElement.getName().toString().equals("input"))
				{
					if(level != VectorsRS.INPUT)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: input");
					level = VectorsRS.VECTOR;
					inputs.add(input);
					input = 0;
				}
				else if(endElement.getName().toString().equals("reqOutput"))
				{
					if(level != VectorsRS.REQ_OUTPUT)
						throw new RuntimeException("Błąd parsowania pliku: nieprawidłowe położenie elementu końcowego: reqOutput");
					level = VectorsRS.VECTOR;
					outputs.add(output);
					output = 0;
				}
			}
			else if(event.isCharacters())
			{
				Characters chars = event.asCharacters();
				if(level == VectorsRS.INPUT) input = Float.parseFloat(chars.getData());
				if(level == VectorsRS.REQ_OUTPUT) output = Float.parseFloat(chars.getData());
			}
		}
		reader.close();
		return vectors;
	}
}