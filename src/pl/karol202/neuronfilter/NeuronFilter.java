package pl.karol202.neuronfilter;

import pl.karol202.neuralnetwork.*;

public class NeuronFilter
{
	public interface UpdateListener
	{
		void onNetworkUpdate(float[] data);

		void onDataPureUpdate(float[] data);

		void onDataNoiseUpdate(float[] data);
	}

	public static final int MAX_SIZE = 1000;

	private UpdateListener listener;
	private int length;
	private Network network;
	private float[] dataPure;
	private float[] dataNoise;
	private boolean running;
	private long sleepTime;

	public NeuronFilter(Network network, UpdateListener listener)
	{
		this.listener = listener;
		this.length = network.getInputsLength();
		this.network = network;
		this.dataPure = null;
		this.dataNoise = null;
	}

	public static Network createNetwork(int length, float learnRatio)
	{
		Neuron[] neurons = new Neuron[length];
		for(int i = 0; i < length; i++)
			neurons[i] = new Neuron(length, new ActivationLinear());
		Layer layer = new Layer(neurons);
		Network network = new Network(new Layer[] { layer }, learnRatio);
		return network;
	}

	public void start(long sleepTime)
	{
		this.sleepTime = sleepTime;
		new Thread(this::run);
	}

	public void stop()
	{
		running = false;
	}

	public void step()
	{
		Vector vector = new Vector(new float[length], new float[length]);
		for(int i = 0; i < length; i++)
		{
			vector.getInputs()[i] = dataNoise[i];
			vector.getReqOutputs()[i] = dataPure[i];
		}
		float[] output = network.learnSingle(vector);
		listener.onNetworkUpdate(output);
	}

	private void run()
	{
		try
		{
			running = true;
			while(running)
			{
				step();
				Thread.sleep(sleepTime);
			}
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public int getLength()
	{
		return length;
	}

	public void setDataNoise(float[] dataNoise)
	{
		this.dataNoise = dataNoise;
		listener.onDataNoiseUpdate(dataNoise);
	}

	public float[] getDataPure()
	{
		return dataPure;
	}

	public void setDataPure(float[] dataPure)
	{
		this.dataPure = dataPure;
		listener.onDataPureUpdate(dataPure);
	}

	public boolean isRunning()
	{
		return running;
	}
}