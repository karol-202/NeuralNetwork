package pl.karol202.neuralnetwork.vector;

import java.io.PrintWriter;

public class Vector
{
	private float[] inputs;

	public Vector() {}

	public Vector(float[] inputs)
	{
		this.inputs = inputs;
	}

	public float[] getInputs()
	{
		return inputs;
	}

	public void setInputs(float[] inputs)
	{
		this.inputs = inputs;
	}

	public void dumpVector(PrintWriter pw)
	{
		pw.println("  Wektor uczący:");
		pw.println("    Wartości wejściowe:");
		for(float value : inputs) pw.println("      " + value);
	}
}
