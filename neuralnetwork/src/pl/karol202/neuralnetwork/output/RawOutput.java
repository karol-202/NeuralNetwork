package pl.karol202.neuralnetwork.output;

public class RawOutput implements OutputType<float[]>
{
	@Override
	public float[] transformOutput(float[] output)
	{
		return output;
	}
}