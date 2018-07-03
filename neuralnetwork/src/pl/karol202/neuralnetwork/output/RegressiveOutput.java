package pl.karol202.neuralnetwork.output;

public class RegressiveOutput implements OutputType<Float>
{
	@Override
	public Float transformOutput(float[] output)
	{
		if(output.length != 1) throw new IllegalArgumentException("Too many outputs.");
		return output[0];
	}
}