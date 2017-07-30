package pl.karol202.imagerecognition;

import java.io.*;
import java.nio.ByteBuffer;

class DigitImageLoader
{
	private InputStream imageStream;
	private InputStream labelStream;
	private int width;
	private int height;
	
	DigitImageLoader(File imagesFile, File labelsFile) throws FileNotFoundException
	{
		imageStream = new FileInputStream(imagesFile);
		labelStream = new FileInputStream(labelsFile);
	}
	
	DigitImage[] loadImages(int maxAmount) throws IOException
	{
		if(readInt(imageStream) != 2051) throw new RuntimeException("Invalid magic number.");
		
		DigitImage[] images = new DigitImage[readAmount(imageStream, maxAmount)];
		width = readInt(imageStream);
		height = readInt(imageStream);
		for(int i = 0; i < images.length; i++) images[i] = loadImage();
		labelImages(images, maxAmount);
		return images;
	}
	
	private DigitImage loadImage() throws IOException
	{
		DigitImage image = new DigitImage(width, height);
		for(int y = 0; y < width; y++)
			for(int x = 0; x < height; x++)
				image.setPixel(x, y, imageStream.read() / 255f);
		return image;
	}
	
	private void labelImages(DigitImage[] images, int maxAmount) throws IOException
	{
		if(readInt(labelStream) != 2049) throw new RuntimeException("Invalid magic number.");
		
		int labels = readAmount(labelStream, maxAmount);
		if(labels != images.length) throw new RuntimeException("Images amount mismatch.");
		for(DigitImage image : images) image.setDigit(labelStream.read());
	}
	
	private int readAmount(InputStream is, int max) throws IOException
	{
		return Math.min(readInt(is), max);
	}
	
	private int readInt(InputStream is) throws IOException
	{
		byte[] bytes = new byte[4];
		is.read(bytes, 0, 4);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		return buffer.getInt();
	}
	
	int getWidth()
	{
		return width;
	}
	
	int getHeight()
	{
		return height;
	}
}