package pl.karol202.neuralnetworksamples.imagerecognition;

class RecognitionResult
{
	private DigitImage image;
	private int recognizedDigit;
	
	RecognitionResult(DigitImage image, int recognizedDigit)
	{
		this.image = image;
		this.recognizedDigit = recognizedDigit;
	}
	
	DigitImage getImage()
	{
		return image;
	}
	
	int getRecognizedDigit()
	{
		return recognizedDigit;
	}
}