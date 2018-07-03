package pl.karol202.neuralnetwork;

public class Utils
{
	public static float[] unboxFloatArray(Float[] boxed)
	{
		float[] unboxed = new float[boxed.length];
		for(int i = 0; i < boxed.length; i++) unboxed[i] = boxed[i];
		return unboxed;
	}
}