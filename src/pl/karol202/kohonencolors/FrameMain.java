package pl.karol202.kohonencolors;

import pl.karol202.neuralnetwork.activation.ActivationLinear;
import pl.karol202.neuralnetwork.layer.StandardKohonenLayer;
import pl.karol202.neuralnetwork.network.BasicKohonenNetwork;
import pl.karol202.neuralnetwork.vector.Vector;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class FrameMain extends JFrame
{
	private class LearnRunnable implements Runnable
	{
		private boolean running;
		
		LearnRunnable()
		{
			running = true;
		}
		
		@Override
		public void run()
		{
			while(running)
			{
				continueLearning();
				waitAMoment();
			}
		}
		
		private void waitAMoment()
		{
			try
			{
				Thread.sleep(5);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		void stop()
		{
			running = false;
		}
	}
	
	private static final float INITIAL_LEARN_RATE = 0.05f;
	private static final float LEARN_RATE_LOSS = 0.9999f;
	private static final float MIN_LEARN_RATE = 0.001f;
	
	private static final float MOMENTUM = 0f;
	
	private static final float INITIAL_NEIGHBOURHOOD = 5f;
	private static final float NEIGHBOURHOOD_LOSS = 0.99999f;
	private static final float MIN_NEIGHBOURHOOD = 1f;
	
	private BasicKohonenNetwork<Vector> network;
	private LearnRunnable learnRunnable;
	
	private PanelColors panelColors;
	private JButton buttonReset;
	//private JButton buttonContinue;
	private JToggleButton buttonStart;
	
	private FrameMain() throws HeadlessException
	{
		super("Sieć Kohonena - kolory");
		createNetwork();
		
		setFrameParams();
		initColorsPanel();
		initResetButton();
		//initContinueButton();
		initStartButton();
	}
	
	private void createNetwork()
	{
		StandardKohonenLayer layer = new StandardKohonenLayer(new int[] { 30, 30 }, 3, new ActivationLinear());
		network = new BasicKohonenNetwork<>(layer);
		network.initWeights();
		network.setLearnRate(INITIAL_LEARN_RATE, LEARN_RATE_LOSS, MIN_LEARN_RATE);
		network.setMomentum(MOMENTUM);
		network.setNeighbourhood(INITIAL_NEIGHBOURHOOD, NEIGHBOURHOOD_LOSS, MIN_NEIGHBOURHOOD);
	}
	
	private void setFrameParams()
	{
		setLayout(new GridBagLayout());
		setSize(600, 600);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void initColorsPanel()
	{
		panelColors = new PanelColors(network);
		add(panelColors, new GridBagConstraints(0, 0, 2, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	private void initResetButton()
	{
		buttonReset = new JButton("Resetuj");
		buttonReset.setFocusable(false);
		buttonReset.addActionListener(e -> reset());
		add(buttonReset, new GridBagConstraints(0, 1, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	/*private void initContinueButton()
	{
		buttonContinue = new JButton("Kontynuuj");
		buttonContinue.setFocusable(false);
		buttonContinue.addActionListener(e -> continueLearning());
		add(buttonContinue, new GridBagConstraints(1, 1, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0));
	}*/
	
	private void initStartButton()
	{
		buttonStart = new JToggleButton("Start / Stop");
		buttonStart.setFocusable(false);
		buttonStart.addActionListener(e -> { if(buttonStart.isSelected()) start(); else stop(); });
		add(buttonStart, new GridBagConstraints(1, 1, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	private void reset()
	{
		network.initWeights();
		network.setLearnRate(INITIAL_LEARN_RATE, LEARN_RATE_LOSS, MIN_LEARN_RATE);
		network.setMomentum(MOMENTUM);
		network.setNeighbourhood(INITIAL_NEIGHBOURHOOD, NEIGHBOURHOOD_LOSS, MIN_NEIGHBOURHOOD);
		panelColors.repaint();
	}
	
	private void start()
	{
		if(learnRunnable != null) return;
		learnRunnable = new LearnRunnable();
		new Thread(learnRunnable).start();
	}
	
	private void stop()
	{
		if(learnRunnable == null) return;
		learnRunnable.stop();
		learnRunnable = null;
	}
	
	private void continueLearning()
	{
		network.learnVector(createRandomVector());
		panelColors.repaint();
		System.out.println(network.getWinnerPosition().getCoord(0) + "   " + network.getWinnerPosition().getCoord(1));
	}
	
	private Vector createRandomVector()
	{
		Random random = new Random();
		
		float[] inputs = new float[3];
		inputs[0] = random.nextFloat();
		inputs[1] = random.nextFloat();
		inputs[2] = random.nextFloat();
		float length = calcVectorLength(inputs);
		for(int i = 0; i < inputs.length; i++) inputs[i] = inputs[i] / length;
		
		return new Vector(inputs);
	}
	
	private float calcVectorLength(float[] vector)
	{
		float sum = 0;
		for(float value : vector) sum += value * value;
		return (float) Math.sqrt(sum);
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