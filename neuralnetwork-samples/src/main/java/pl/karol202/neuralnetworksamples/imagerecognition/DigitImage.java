package pl.karol202.neuralnetworksamples.imagerecognition;

class DigitImage
{
	private int width;
	private int height;
	private float[] pixels;
	private int digit;
	
	DigitImage(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.pixels = new float[width * height];
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	float[] getPixels()
	{
		return pixels;
	}
	
	void setPixel(int x, int y, float pixel)
	{
		pixels[y * width + x] = pixel;
	}
	
	int getDigit()
	{
		return digit;
	}
	
	void setDigit(int digit)
	{
		this.digit = digit;
	}
}