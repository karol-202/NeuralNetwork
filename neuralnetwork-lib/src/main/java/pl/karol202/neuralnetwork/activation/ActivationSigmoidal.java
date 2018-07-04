package pl.karol202.neuralnetwork.activation;

import java.io.PrintWriter;

public class ActivationSigmoidal implements Activation
{
	private float alpha;

	public ActivationSigmoidal(float alpha)
	{
		this.alpha = alpha;
	}

	@Override
	public float calculate(float value)
	{
		return 1f / (1 + (float) Math.exp(-value * alpha));
	}

	@Override
	public float calcDerivative(float value)
	{
		return value * (1 - value) * alpha;
	}

	@Override
	public void dumpActivation(PrintWriter pw)
	{
		pw.println("    Sigmoidalna funkcja aktywacji:");
		pw.println("      Alpha:" + alpha);
	}
}
