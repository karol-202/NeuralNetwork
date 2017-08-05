package pl.karol202.neuralnetwork.activation;

import java.io.PrintWriter;

public class ActivationTangensoidal implements Activation
{
	private float alpha;
	
	public ActivationTangensoidal(float alpha)
	{
		this.alpha = alpha;
	}
	
	@Override
	public float calculate(float value)
	{
		return (float) Math.tanh(value * alpha);
	}
	
	@Override
	public float calcDerivative(float value)
	{
		return alpha * (1 - (value * value));
	}
	
	@Override
	public void dumpActivation(PrintWriter pw)
	{
		pw.println("    Tangens hiperboliczny - funkcja aktywacji:");
		pw.println("      Alpha:" + alpha);
	}
}