package pl.karol202.imagerecognition;

import javax.swing.*;
import java.awt.*;

class RecognitionResultPanel extends JPanel
{
	private DigitImagePanel imagePanel;
	private JLabel label;
	
	RecognitionResultPanel()
	{
		setPanelParams();
		initImagePanel();
		initLabel();
	}
	
	private void setPanelParams()
	{
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
	}
	
	private void initImagePanel()
	{
		imagePanel = new DigitImagePanel();
		add(imagePanel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	private void initLabel()
	{
		label = new JLabel("");
		add(label, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	void setRecognitionResult(RecognitionResult result)
	{
		DigitImage digitImage = result.getImage();
		imagePanel.setDigitImage(digitImage);
		if(digitImage.getDigit() == result.getRecognizedDigit()) label.setText(String.valueOf(result.getRecognizedDigit()));
		else label.setText(String.format("<html><s>%d</s> / %d</html>", result.getRecognizedDigit(), digitImage.getDigit()));
	}
}