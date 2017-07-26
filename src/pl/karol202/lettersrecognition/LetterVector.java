package pl.karol202.lettersrecognition;

import pl.karol202.neuralnetwork.Vector;

public class LetterVector extends Vector
{
	private char letter;
	
	public LetterVector(char letter, float[] inputs, float[] reqOutputs)
	{
		super(inputs, reqOutputs);
		this.letter = letter;
	}
	
	public char getLetter()
	{
		return letter;
	}
}