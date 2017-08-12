package pl.karol202.neuralnetwork.output;

import pl.karol202.neuralnetwork.network.KohonenNetwork;
import pl.karol202.neuralnetwork.neuron.NeuronPosition;

public class WinnerPositionOutput implements OutputType<NeuronPosition>
{
	private KohonenNetwork<?> kohonenNetwork;
	
	public WinnerPositionOutput(KohonenNetwork<?> kohonenNetwork)
	{
		this.kohonenNetwork = kohonenNetwork;
	}
	
	@Override
	public NeuronPosition transformOutput(float[] output)
	{
		return kohonenNetwork.getWinnerPosition();
	}
}