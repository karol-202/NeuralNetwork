package pl.karol202.neuralnetwork;

import pl.karol202.neuralnetwork.network.SupervisedLearnNetwork;
import pl.karol202.neuralnetwork.vector.SupervisedLearnVector;

import java.util.Collections;
import java.util.List;

public class ContinuousSupervisedLearning<V extends SupervisedLearnVector> implements Runnable
{
	public interface LearningListener
	{
		void onLearnedVector(float[] errors);
		
		void onLearnedEpoch(double meanSquareError, float highestError);
		
		void onLearningEnded();
	}
	
	private SupervisedLearnNetwork<?, V> network;
	private LearningListener listener;
	
	private List<V> vectors;
	private float maxError;
	private boolean learning;
	private boolean stop;
	
	public ContinuousSupervisedLearning(SupervisedLearnNetwork<?, V> network, LearningListener listener)
	{
		this.network = network;
		this.listener = listener;
	}
	
	public void learn(List<V> vectors, float maxError)
	{
		if(learning) return;
		this.vectors = vectors;
		this.maxError = maxError;
		learning = true;
		stop = false;
		new Thread(this).start();
	}
	
	@Override
	public void run()
	{
		//double lastMeanSquareError = -1f;
		while(learning && !stop)
		{
			long startTime = System.currentTimeMillis();
			float[][] errors = new float[vectors.size()][network.getOutputsLength()];
			float highestError = 0f;
			learning = false;
			
			Collections.shuffle(vectors);
			for(int i = 0; i < vectors.size(); i++)
			{
				if(stop) break;
				network.learnVector(vectors.get(i));
				errors[i] = network.getErrors();
				listener.onLearnedVector(errors[i]);
				for(int j = 0; j < errors[i].length; j++)
				{
					float abs = Math.abs(errors[i][j]);
					if(abs > highestError) highestError = abs;
				}
				if(highestError > maxError) learning = true;
			}
			if(stop) break;
			
			double meanSquareError = calculateMeanSquareError(errors);
			listener.onLearnedEpoch(meanSquareError, highestError);
			
			/*if(lastMeanSquareError != -1) //Zmienny learnRate został wyłączony z powodu niepewności co do
											  sposobu jego implementacji oraz niewielkiej korzyści podczas uczenia.
			{
				float difference = (float) (meanSquareError - lastMeanSquareError);
				if(difference > 0) network.setLearnRate(network.getLearnRate() + 0.05f);
				else if(difference < 0) network.setLearnRate(network.getLearnRate() * 0.9f);
			}
			lastMeanSquareError = meanSquareError;*/
			System.out.println("Czas: " + (System.currentTimeMillis() - startTime));
		}
		listener.onLearningEnded();
		vectors = null;
		maxError = -1f;
		learning = false;
		stop = false;
	}
	
	private double calculateMeanSquareError(float[][] errors)
	{
		float sum = 0f;
		for(float[] neurons : errors)
			for(float error : neurons)
				sum += error * error;
		return sum / (double) (errors.length * errors[0].length);
	}
	
	public void stopLearning()
	{
		stop = true;
		while(learning) Thread.yield();
	}
}