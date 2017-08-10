package pl.karol202.selflearnexample;

import pl.karol202.neuralnetwork.activation.ActivationLinear;
import pl.karol202.neuralnetwork.layer.UnsupervisedLearnLayer;
import pl.karol202.neuralnetwork.network.UnsupervisedLearnNetwork;
import pl.karol202.neuralnetwork.neuron.UnsupervisedLearnNeuron;
import pl.karol202.neuralnetwork.vector.Vector;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class FrameMain extends JFrame
{
	private static final float INITIAL_LEARN_RATE = 0.05f;
	private static final float INITIAL_MOMENTUM = 0f;
	
	private UnsupervisedLearnNetwork<?, Vector> network;
	
	private PanelNeurons panelNeurons;
	private JButton buttonReset;
	private JButton buttonContinue;
	
	private FrameMain()
	{
		super("Sieć samoucząca");
		createNetwork();
		
		setFrameParams();
		initNeuronsPanel();
		initResetButton();
		initContinueButton();
	}
	
	private void createNetwork()
	{
		UnsupervisedLearnNeuron[] neurons = new UnsupervisedLearnNeuron[30];
		for(int i = 0; i < neurons.length; i++) neurons[i] = new UnsupervisedLearnNeuron(2, new ActivationLinear());
		UnsupervisedLearnLayer layer = new UnsupervisedLearnLayer(neurons);
		
		network = new UnsupervisedLearnNetwork<>(layer, INITIAL_LEARN_RATE, INITIAL_MOMENTUM, null);
		network.randomWeights(-1f, 1f);
	}
	
	private void setFrameParams()
	{
		setLayout(new GridBagLayout());
		setSize(600, 600);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void initNeuronsPanel()
	{
		panelNeurons = new PanelNeurons(network);
		add(panelNeurons, new GridBagConstraints(0, 0, 2, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	private void initResetButton()
	{
		buttonReset = new JButton("Resetuj");
		buttonReset.setFocusable(false);
		buttonReset.addActionListener(e -> reset());
		add(buttonReset, new GridBagConstraints(0, 1, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6, 6, 6, 3),
				0, 0));
	}
	
	private void initContinueButton()
	{
		buttonContinue = new JButton("Kontynuuj");
		buttonContinue.setFocusable(false);
		buttonContinue.addActionListener(e -> continueLearning());
		add(buttonContinue, new GridBagConstraints(1, 1, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6, 3, 6, 6),
				0, 0));
	}
	
	private void reset()
	{
		network.randomWeights(-1f, 1f);
		panelNeurons.setLearnVector(null);
		panelNeurons.repaint();
	}
	
	private void continueLearning()
	{
		Vector vector = createRandomVector();
		panelNeurons.update();
		network.learnVector(vector);
		panelNeurons.setLearnVector(vector);
		panelNeurons.repaint();
	}
	
	private Vector createRandomVector()
	{
		Random random = new Random();
		float[] inputs = new float[2];
		switch(random.nextInt(4))
		{
		case 0: inputs[0] = -2f; inputs[1] = -2f; break;
		case 1: inputs[0] = -2f; inputs[1] = 2f; break;
		case 2: inputs[0] = 2f; inputs[1] = 2f; break;
		case 3: inputs[0] = 2f; inputs[1] = -2f; break;
		}
		return new Vector(inputs);
	}
	
	public static void main(String[] args)
	{
		setLookAndFeel();
		SwingUtilities.invokeLater(FrameMain::new);
	}
	
	private static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}