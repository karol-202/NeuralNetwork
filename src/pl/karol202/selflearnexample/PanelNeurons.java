package pl.karol202.selflearnexample;

import pl.karol202.neuralnetwork.network.HebbNetwork;
import pl.karol202.neuralnetwork.vector.Vector;

import javax.swing.*;
import java.awt.*;

public class PanelNeurons extends JPanel
{
	private static final float SCALE = 100;
	private static final int NEURON_SIZE = 4;
	private static final int VECTOR_SIZE = 6;
	
	private HebbNetwork<?, ?> network;
	private float[][] previousWeights;
	private Vector learnVector;
	
	PanelNeurons(HebbNetwork<?, ?> network)
	{
		this.network = network;
		
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		drawAxes(g);
		drawNeurons(g);
		drawLastLearnVector(g);
	}
	
	private void drawAxes(Graphics2D g)
	{
		g.setColor(Color.DARK_GRAY);
		g.setStroke(new BasicStroke());
		g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
		g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
	}
	
	private void drawNeurons(Graphics2D g)
	{
		float[][] weights = network.getNeuronsWeights();
		if(previousWeights != null) for(int i = 0; i < weights.length; i++) drawNeuronTrack(g, previousWeights[i], weights[i]);
		for(float[] weight : weights) drawNeuron(g, weight);
	}
	
	private void drawNeuronTrack(Graphics2D g, float[] oldWeights, float[] newWeights)
	{
		if(oldWeights.length != 2 || newWeights.length != 2) throw new IllegalArgumentException("Invalid weights length.");
		int oldX = (int) (oldWeights[0] * SCALE) + (getWidth() / 2);
		int oldY = (int) (oldWeights[1] * SCALE) + (getHeight() / 2);
		int newX = (int) (newWeights[0] * SCALE) + (getWidth() / 2);
		int newY = (int) (newWeights[1] * SCALE) + (getHeight() / 2);
		g.setColor(Color.GRAY);
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f,
									new float[] { 5f, 3f }, 0f));
		g.drawLine(oldX, oldY, newX, newY);
	}
	
	private void drawNeuron(Graphics2D g, float[] weights)
	{
		if(weights.length != 2) throw new IllegalArgumentException("Invalid weights length.");
		int x = (int) (weights[0] * SCALE) + (getWidth() / 2);
		int y = (int) (weights[1] * SCALE) + (getHeight() / 2);
		g.setColor(Color.BLACK);
		g.fillRect(x - (NEURON_SIZE / 2), y - (NEURON_SIZE / 2), NEURON_SIZE, NEURON_SIZE);
	}
	
	private void drawLastLearnVector(Graphics2D g)
	{
		if(learnVector == null) return;
		int x = (int) (learnVector.getInputs()[0] * SCALE) + (getWidth() / 2);
		int y = (int) (learnVector.getInputs()[1] * SCALE) + (getHeight() / 2);
		g.setColor(Color.RED);
		g.fillRect(x - (VECTOR_SIZE / 2), y - (VECTOR_SIZE / 2), VECTOR_SIZE, VECTOR_SIZE);
	}
	
	void update()
	{
		previousWeights = network.getNeuronsWeights();
	}
	
	void setLearnVector(Vector learnVector)
	{
		this.learnVector = learnVector;
	}
}