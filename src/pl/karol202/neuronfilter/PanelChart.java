package pl.karol202.neuronfilter;

import pl.karol202.neuronfilter.NeuronFilter.UpdateListener;

import javax.swing.*;
import java.awt.*;

import static pl.karol202.neuronfilter.DataCreator.X_MAX;
import static pl.karol202.neuronfilter.DataCreator.X_MIN;

public class PanelChart extends JPanel implements UpdateListener
{
	private float[] dataNetwork;
	private float[] dataPure;
	private float[] dataNoise;

	public PanelChart()
	{
		setPreferredSize(new Dimension(1000, 500));
	}

	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		if(dataNetwork != null) drawData(g, dataNetwork, Color.GREEN);
		if(dataPure != null) drawData(g, dataPure, Color.RED);
		if(dataNoise != null) drawData(g, dataNoise, Color.GRAY);
	}

	@Override
	public void onNetworkUpdate(float[] data)
	{
		this.dataNetwork = data;
		repaint();
	}

	@Override
	public void onDataPureUpdate(float[] data)
	{
		this.dataPure = data;
		repaint();
	}

	@Override
	public void onDataNoiseUpdate(float[] data)
	{
		this.dataNoise = data;
		repaint();
	}

	private void drawData(Graphics2D g, float[] data, Color color)
	{
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
											   RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHints(rh);
		g.setColor(color);
		g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		float length = data.length;
		float scale = getWidth() / (X_MAX - X_MIN);
		int lastX = 0;
		int lastY = Math.round(data[0] * scale + (getHeight() / 2));
		for(int i = 1; i < length; i++)
		{
			int x = Math.round(getWidth() * (i / (length - 1)));
			int y = Math.round(data[i] * scale + (getHeight() / 2));
			g.drawLine(lastX, lastY, x, y);
			lastX = x;
			lastY = y;
		}
	}
}
