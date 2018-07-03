package pl.karol202.neuralnetwork.network;

import pl.karol202.neuralnetwork.vector.SupervisedLearnVector;

public interface DeltaNetwork<V extends SupervisedLearnVector>
{
	void learnVector(V vector);
	
	float[] getErrors();
}