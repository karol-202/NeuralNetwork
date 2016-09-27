package pl.karol202.neuralnetwork;

import java.io.PrintWriter;

public class ActivationLinear implements Activation
{
	@Override
	public float calculate(float value)
	{
		return value;
	}

	@Override
	public float calcDerivative(float value)
	{
		return 1;
	}

	@Override
	public void dumpActivation(PrintWriter pw)
	{
		pw.println("    Liniowa funkcja aktywacji");
	}
}
