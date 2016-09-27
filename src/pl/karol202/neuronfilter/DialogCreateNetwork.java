package pl.karol202.neuronfilter;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class DialogCreateNetwork extends JDialog
{
	public interface NetworkCreateListener
	{
		void onNetworkCreating(int length, float learnRatio);
	}

	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JSpinner spinnerNetworkSize;
	private JLabel textNetworkLR;
	private JSpinner spinnerNetworkLR;
	private JLabel textNetworkParams;
	private JLabel textNetworkSize;

	private NetworkCreateListener listener;
	private int maxLength;

	public DialogCreateNetwork(NetworkCreateListener listener, int maxLength)
	{
		this.listener = listener;
		this.maxLength = maxLength;

		setContentPane(contentPane);
		setModalityType(DEFAULT_MODALITY_TYPE);
		setTitle("Tworzenie sieci");
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
		int length = (int) spinnerNetworkSize.getValue();
		float learnRatio = (float) (double) spinnerNetworkLR.getValue();
		listener.onNetworkCreating(length, learnRatio);
		dispose();
	}

	private void onCancel()
	{
		dispose();
	}

	private void createUIComponents()
	{
		SpinnerNumberModel modelNetworkSize = new SpinnerNumberModel(500, 0, maxLength, 1);
		spinnerNetworkSize = new JSpinner(modelNetworkSize);

		SpinnerNumberModel modelNetworkLR = new SpinnerNumberModel(0.1, 0, 1, 0.01);
		spinnerNetworkLR = new JSpinner(modelNetworkLR);
	}
}