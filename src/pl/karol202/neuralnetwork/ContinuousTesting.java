package pl.karol202.neuralnetwork;

import pl.karol202.neuralnetwork.network.Network;
import pl.karol202.neuralnetwork.vector.Vector;

import java.util.List;

public class ContinuousTesting<V extends Vector, O> implements Runnable
{
	public interface TestingListener<V extends Vector, O>
	{
		void onTesting(V vector, O output);
		
		void onTestingEnded();
	}
	
	private Network<O, ?, V> network;
	private TestingListener<V, O> listener;
	
	private List<V> vectors;
	private boolean testing;
	private boolean stop;
	private Thread thread;
	
	public ContinuousTesting(Network<O, ?, V> network, TestingListener<V, O> listener)
	{
		this.network = network;
		this.listener = listener;
	}
	
	public void test(List<V> vectors)
	{
		if(testing) return;
		this.vectors = vectors;
		testing = true;
		stop = false;
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run()
	{
		for(V vector : vectors)
		{
			if(stop) break;
			O output = network.testVector(vector);
			listener.onTesting(vector, output);
		}
		listener.onTestingEnded();
		testing = false;
		stop = false;
		thread = null;
	}
	
	public void stopLearning()
	{
		stop = true;
		while(testing) Thread.yield();
	}
}