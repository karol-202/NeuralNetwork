package pl.karol202.imagerecognition;

import pl.karol202.neuralnetwork.Vector;

class DigitVector extends Vector
{
	private int digit;
	
	DigitVector(int numberChar, float[] inputs, float[] reqOutputs)
	{
		super(inputs, reqOutputs);
		this.digit = numberChar;
	}
	
	public int getDigit()
	{
		return digit;
	}
}