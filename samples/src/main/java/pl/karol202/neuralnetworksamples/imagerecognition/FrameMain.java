package pl.karol202.neuralnetworksamples.imagerecognition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class FrameMain extends JFrame
{
	private JPanel panelRoot;
	private JScrollPane scrollPaneRoot;
	
	private List<RecognitionResultPanel> panels;
	
	FrameMain()
	{
		super("Rozpoznawanie cyfr - podgląd");
		setLookAndFeel();
		setFrameParams();
		initRootPanel();
		
		panels = new ArrayList<>();
	}
	
	private void setLookAndFeel()
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
	
	private void setFrameParams()
	{
		setSize(new Dimension(300, 300));
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private void initRootPanel()
	{
		panelRoot = new JPanel();
		panelRoot.setLayout(new GridLayout(0, 5));
		
		scrollPaneRoot = new JScrollPane(panelRoot);
		add(scrollPaneRoot);
	}
	
	void update(List<RecognitionResult> results)
	{
		if(results.size() != panels.size()) setPanels(results.size());
		for(int i = 0; i < panels.size(); i++) panels.get(i).setRecognitionResult(results.get(i));
		repaint();
	}
	
	private void setPanels(int target)
	{
		if(target > panels.size())
			for(int i = 0; i < target - panels.size(); i++)
			{
				RecognitionResultPanel panel = new RecognitionResultPanel();
				panels.add(panel);
				panelRoot.add(panel);
			}
		else
			for(int i = 0; i < panels.size() - target; i++)
			{
				RecognitionResultPanel panel = panels.get(0);
				panels.remove(0);
				panelRoot.remove(panel);
			}
	}
}