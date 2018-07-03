package pl.karol202.neuralnetwork.vector;

import java.io.PrintWriter;

public class SupervisedLearnVector extends Vector
{
	private float[] reqOutputs;
	
	public SupervisedLearnVector() { }
	
	public SupervisedLearnVector(float[] inputs, float[] reqOutputs)
	{
		super(inputs);
		this.reqOutputs = reqOutputs;
	}
	
	public float[] getReqOutputs()
	{
		return reqOutputs;
	}
	
	public void setReqOutputs(float[] reqOutputs)
	{
		this.reqOutputs = reqOutputs;
	}
	
	@Override
	public void dumpVector(PrintWriter pw)
	{
		super.dumpVector(pw);
		pw.println("    Oczekiwane wartości wyjściowe:");
		for(float value : reqOutputs) pw.println("      " + value);
	}
}