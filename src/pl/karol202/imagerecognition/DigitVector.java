package pl.karol202.imagerecognition;

import pl.karol202.neuralnetwork.Vector;

class DigitVector extends Vector
{
	private DigitImage image;
	private int digit;
	
	DigitVector(DigitImage image)
	{
		super(image.getPixels(), new float[10]);
		this.image = image;
		this.digit = image.getDigit();
		getReqOutputs()[digit] = 1;
	}
	
	DigitImage getImage()
	{
		return image;
	}
	
	int getDigit()
	{
		return digit;
	}
}