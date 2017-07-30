package pl.karol202.imagerecognition;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

class DigitImagePanel extends JPanel
{
	private BufferedImage image;
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
	
	void setDigitImage(DigitImage digitImage)
	{
		byte[] pix = new byte[digitImage.getPixels().length];
		for(int i = 0; i < pix.length; i++)
		{
			pix[i] = (byte) (digitImage.getPixels()[i] * 255);
		}
		
		image = new BufferedImage(digitImage.getWidth(), digitImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image.getRaster();
		raster.setDataElements(0, 0, digitImage.getWidth(), digitImage.getHeight(), pix);
		
		setPreferredSize(new Dimension(digitImage.getWidth(), digitImage.getHeight()));
	}
}