package pl.karol202.neuronfilter;

import pl.karol202.neuronfilter.DataCreator.CurveType;

import javax.swing.*;
import java.awt.event.*;

public class DialogCreateData extends JDialog
{
	public interface DataCreateListener
	{
		void onDataCreating(CurveType type);
	}

	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JLabel textDataParams;
	private JLabel textDataType;
	private JComboBox<CurveType> comboDataType;

	private DataCreateListener listener;

	public DialogCreateData(DataCreateListener listener)
	{
		this.listener = listener;

		setContentPane(contentPane);
		setModalityType(DEFAULT_MODALITY_TYPE);
		setTitle("Generowanie krzywej");
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
		CurveType type = (CurveType) comboDataType.getSelectedItem();
		listener.onDataCreating(type);
		dispose();
	}

	private void onCancel()
	{
		dispose();
	}

	private void createUIComponents()
	{
		comboDataType = new JComboBox<CurveType>(CurveType.values());
	}
}
