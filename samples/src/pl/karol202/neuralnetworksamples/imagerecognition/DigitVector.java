package pl.karol202.neuralnetworksamples.imagerecognition;

import pl.karol202.neuralnetwork.vector.SupervisedLearnVector;

class DigitVector extends SupervisedLearnVector
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