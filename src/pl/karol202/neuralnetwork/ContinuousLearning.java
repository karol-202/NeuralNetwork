package pl.karol202.neuralnetwork;

import java.util.Collections;
import java.util.List;

public class ContinuousLearning implements Runnable
{
	public interface LearningListener
	{
		void onLearning(float[] errors);
		
		void onLearningEnded();
	}
	
	private Network network;
	private LearningListener listener;
	
	private List<? extends Vector> vectors;
	private float maxError;
	private boolean learning;
	private boolean stop;
	private Thread thread;
	
	public ContinuousLearning(Network network, LearningListener listener)
	{
		this.network = network;
		this.listener = listener;
	}
	
	public void learn(List<? extends Vector> vectors, float maxError)
	{
		if(learning) return;
		this.vectors = vectors;
		this.maxError = maxError;
		learning = true;
		stop = false;
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run()
	{
		while(learning && !stop)
		{
			float[][] errors = new float[vectors.size()][network.getOutputsLength()];
			learning = false;
			
			Collections.shuffle(vectors);
			for(int i = 0; i < vectors.size(); i++)
			{
				if(stop) break;
				errors[i] = network.learnVector(vectors.get(i));
				listener.onLearning(errors[i]);
				for(int j = 0; j < errors[i].length; j++)
					if(Math.abs(errors[i][j]) > maxError) learning = true;
			}
		}
		listener.onLearningEnded();
		vectors = null;
		maxError = -1f;
		learning = false;
		stop = false;
	}
	
	public void stopLearning()
	{
		stop = true;
		while(learning) Thread.yield();
	}
}