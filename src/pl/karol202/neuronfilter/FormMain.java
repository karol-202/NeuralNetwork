package pl.karol202.neuronfilter;

import pl.karol202.neuralnetwork.Network;
import pl.karol202.neuronfilter.DataCreator.CurveType;
import pl.karol202.neuronfilter.DialogCreateNoise.NoiseCreatingListener;

import javax.swing.*;

import static pl.karol202.neuronfilter.DialogCreateData.*;
import static pl.karol202.neuronfilter.DialogCreateNetwork.*;

public class FormMain extends JFrame implements NetworkCreateListener, DataCreateListener, NoiseCreatingListener
{
	private JPanel root;
	private JButton buttonRun;
	private PanelChart chart;
	private JPanel panelControl;
	private JButton buttonStep;
	private JLabel textSteps;
	private JButton buttonLoadNetwork;
	private JButton buttonLoadData;
	private JButton buttonGenerateData;
	private JButton buttonGenerateNetwork;
	private JButton buttonGenerateNoise;
	private JLabel textNoise;
	private JCheckBox checkLearning;
	private JSlider sliderSpeed;
	private JLabel textSpeed;

	private NeuronFilter filter;

	public FormMain()
	{
		super("NeuronFilter");
		setContentPane(root);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(500, 200);
		setResizable(false);
		setVisible(true);

		buttonRun.addActionListener(e -> onRunClick());
		buttonStep.addActionListener(e -> onStepClick());
		buttonLoadNetwork.addActionListener(e -> onLoadNetworkClick());
		buttonLoadData.addActionListener(e -> onLoadDataClick());
		buttonGenerateData.addActionListener(e -> onGenerateDataClick());
		buttonGenerateNetwork.addActionListener(e -> onGenerateNetworkClick());
		buttonGenerateNoise.addActionListener(e -> onGenerateNoiseClick());

		buttonRun.setEnabled(false);
		buttonStep.setEnabled(false);
		buttonLoadData.setEnabled(false);
		buttonGenerateData.setEnabled(false);
		buttonGenerateNoise.setEnabled(false);
	}

	private void onRunClick()
	{
		if(filter.isRunning())
		{
			filter.stop();
			buttonRun.setText("Start");
			buttonStep.setEnabled(true);
			buttonLoadData.setEnabled(true);
			buttonGenerateData.setEnabled(true);
			buttonGenerateNoise.setEnabled(true);
		}
		else
		{
			filter.start(getSleepTime());
			buttonRun.setText("Stop");
			buttonStep.setEnabled(false);
			buttonLoadData.setEnabled(false);
			buttonGenerateData.setEnabled(false);
			buttonGenerateNoise.setEnabled(false);
		}
	}

	private void onStepClick()
	{
		filter.step();
	}

	private void onLoadNetworkClick()
	{

	}

	private void onLoadDataClick()
	{

	}

	private void onGenerateDataClick()
	{
		new DialogCreateData(this);
	}

	private void onGenerateNetworkClick()
	{
		new DialogCreateNetwork(this, NeuronFilter.MAX_SIZE);
	}

	private void onGenerateNoiseClick()
	{
		new DialogCreateNoise(this);
	}

	@Override
	public void onNetworkCreating(int length, float learnRatio)
	{
		Network network = NeuronFilter.createNetwork(length, learnRatio);
		filter = new NeuronFilter(network, chart);

		buttonLoadData.setEnabled(true);
		buttonGenerateData.setEnabled(true);
	}

	@Override
	public void onDataCreating(CurveType type)
	{
		float[] data = DataCreator.generateCurve(filter.getLength(), type);
		filter.setDataPure(data);

		buttonRun.setEnabled(true);
		buttonStep.setEnabled(true);
		buttonGenerateNoise.setEnabled(true);
	}

	@Override
	public void onNoiseCreating(float amplitude)
	{
		float[] data = DataCreator.generateNoise(filter.getDataPure(), amplitude);
		filter.setDataNoise(data);
	}

	private long getSleepTime()
	{
		int pos = sliderSpeed.getValue();
		float minPos = sliderSpeed.getMinimum();
		float maxPos = sliderSpeed.getMaximum();
		double minVal = Math.log(2);
		double maxVal = Math.log(1000);
		double scale = (maxVal - minVal) / (maxPos - minPos);
		return (long) Math.exp(minVal + scale * (pos - minPos));
	}

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(FormMain::new);
	}
}