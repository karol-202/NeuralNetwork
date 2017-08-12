package pl.karol202.kohonencolors;

import pl.karol202.neuralnetwork.network.KohonenNetwork;
import pl.karol202.neuralnetwork.neuron.NeuronPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

class PanelColors extends JPanel
{
	private KohonenNetwork<?> network;
	
	PanelColors(KohonenNetwork<?> network)
	{
		this.network = network;
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		
		drawColors(g);
	}
	
	private void drawColors(Graphics2D g)
	{
		g.drawImage(createImage(), 0, 0, null);
	}
	
	private BufferedImage createImage()
	{
		int[] pixels = new int[300 * 300];
		for(int x = 0; x < 300; x += 1)
		{
			for(int y = 0; y < 300; y += 1)
			{
				NeuronPosition position = new NeuronPosition(x / 10, y / 10);
				float[] weights = network.getNeuronWeights(position);
				if(weights.length != 3) throw new RuntimeException("Invalid weights.");
				for(int i = 0; i < weights.length; i++) weights[i] = Math.max(Math.min(weights[i], 1), 0);
				int color = new Color(weights[0], weights[1], weights[2]).getRGB();
				pixels[y * 300 + x] = color;
			}
		}
		
		BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
		WritableRaster raster = image.getRaster();
		raster.setDataElements(0, 0, 300, 300, pixels);
		return image;
	}
}