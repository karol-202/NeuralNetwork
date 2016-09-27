package pl.karol202.neuronfilter;

import javax.swing.*;
import java.awt.event.*;

public class DialogCreateNoise extends JDialog
{
	public interface NoiseCreatingListener
	{
		void onNoiseCreating(float amplitude);
	}

	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JLabel textNoiseParams;
	private JLabel textNoiseAmplitude;
	private JSpinner spinnerNoiseAmplitude;

	private NoiseCreatingListener listener;

	public DialogCreateNoise(NoiseCreatingListener listener)
	{
		this.listener = listener;

		setContentPane(contentPane);
		setModalityType(DEFAULT_MODALITY_TYPE);
		setTitle("Generowanie szumu");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getRootPane().setDefaultButton(buttonOK);
		pack();

		buttonOK.addActionListener(e -> onOK());
		buttonCancel.addActionListener(e -> onCancel());

		contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		setVisible(true);
	}

	private void onOK()
	{
		float amplitude = (float) (double) spinnerNoiseAmplitude.getValue();
		listener.onNoiseCreating(amplitude);
		dispose();
	}

	private void onCancel()
	{
		dispose();
	}

	private void createUIComponents()
	{
		SpinnerNumberModel modelNoiseAmplitude = new SpinnerNumberModel(0.1, 0, 1, 0.01);
		spinnerNoiseAmplitude = new JSpinner(modelNoiseAmplitude);
	}
}
