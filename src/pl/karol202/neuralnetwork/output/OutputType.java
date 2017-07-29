package pl.karol202.neuralnetwork.output;

public interface OutputType<T>
{
	T transformOutput(float[] output);
}