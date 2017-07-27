package pl.karol202.lettersrecognition;

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