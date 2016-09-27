package pl.karol202.neuralnetwork;

import java.io.PrintWriter;

public class Vector
{
	private float[] inputs;
	private float[] reqOutputs;

	public Vector() {}

	public Vector(float[] inputs, float[] reqOutputs)
	{
		this.inputs = inputs;
		this.reqOutputs = reqOutputs;
	}

	public float[] getInputs()
	{
		return inputs;
	}

	public void setInputs(float[] inputs)
	{
		this.inputs = inputs;
	}

	public float[] getReqOutputs()
	{
		return reqOutputs;
	}

	public void setReqOutputs(float[] reqOutputs)
	{
		this.reqOutputs = reqOutputs;
	}

	public void dumpVector(PrintWriter pw)
	{
		pw.println("  Wektor uczący:");
		pw.println("    Wartości wejściowe:");
		for(float value : inputs) pw.println("      " + value);
		pw.println("    Oczekiwane wartości wyjściowe:");
		for(float value : reqOutputs) pw.println("      " + value);
	}
}
