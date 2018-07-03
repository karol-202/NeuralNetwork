package pl.karol202.neuralnetwork.layer;

import pl.karol202.neuralnetwork.activation.Activation;
import pl.karol202.neuralnetwork.neuron.StandardKohonenNeuron;

public class StandardKohonenLayer extends KohonenLayer<StandardKohonenNeuron>
{
	public StandardKohonenLayer(int[] dimensions, int inputs, Activation activation)
	{
		super(new StandardKohonenNeuron[calculateNeuronsAmount(dimensions)], dimensions);
		for(int i = 0; i < neurons.length; i++)
			neurons[i] = new StandardKohonenNeuron(getPositionOfNeuron(i), inputs, activation);
	}
}