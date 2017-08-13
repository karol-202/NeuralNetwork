package pl.karol202.cptest;

import pl.karol202.neuralnetwork.ContinuousSupervisedLearning;
import pl.karol202.neuralnetwork.activation.ActivationLinear;
import pl.karol202.neuralnetwork.layer.GrossbergLayer;
import pl.karol202.neuralnetwork.layer.StandardKohonenLayer;
import pl.karol202.neuralnetwork.network.CounterPropagationNetwork;
import pl.karol202.neuralnetwork.output.NominalOutput;
import pl.karol202.neuralnetwork.vector.SupervisedLearnVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main implements ContinuousSupervisedLearning.LearningListener
{
	private CounterPropagationNetwork<Integer, SupervisedLearnVector> network;
	private ContinuousSupervisedLearning<CounterPropagationNetwork<?, SupervisedLearnVector>, SupervisedLearnVector> learning;
	private List<SupervisedLearnVector> vectors;
	
	private Scanner scanner;
	
	public Main()
	{
		createNetwork();
		createVectors();
		
		scanner = new Scanner(System.in);
		run();
	}
	
	private void createNetwork()
	{
		StandardKohonenLayer kohonenLayer = new StandardKohonenLayer(new int[]{ 5 }, 2, new ActivationLinear());
		GrossbergLayer grossbergLayer = new GrossbergLayer(5, 5, new ActivationLinear());
		network = new CounterPropagationNetwork<>(kohonenLayer, grossbergLayer, new NominalOutput<>(i -> i, 0.1f));
		network.initWeights(-0.1f, 0.1f);
		network.setLearnRate(0.05f, 0.9999f, 0.001f);
		network.setGrossbergLearnRate(0.1f, 0.999f, 0.01f);
		network.setMomentum(0f);
		network.setNeighbourhood(0f, 1f, 0f);
		
		learning = new ContinuousSupervisedLearning<>(network, this);
	}
	
	private void createVectors()
	{
		vectors = new ArrayList<>();
		vectors.add(new SupervisedLearnVector(new float[] { 0,    0    }, new float[] { 1, 0, 0, 0, 0 }));
		vectors.add(new SupervisedLearnVector(new float[] { 0.5f, 0    }, new float[] { 1, 0, 0, 0, 0 }));
		vectors.add(new SupervisedLearnVector(new float[] { 0,    0.5f }, new float[] { 0, 1, 0, 0, 0 }));
		vectors.add(new SupervisedLearnVector(new float[] { 1,    1    }, new float[] { 0, 0, 1, 0, 0 }));
		vectors.add(new SupervisedLearnVector(new float[] { 0.5f, 1    }, new float[] { 0, 0, 0, 1, 0 }));
		vectors.add(new SupervisedLearnVector(new float[] { 1,    0.5f }, new float[] { 0, 0, 0, 0, 1 }));
	}
	
	private void run()
	{
		while(true) if(!runOneOption()) break;
	}
	
	private boolean runOneOption()
	{
		System.out.println("Wybierz operację:");
		System.out.println("1. Uczenie ciągłe");
		System.out.println("2. Testowanie ręczne");
		System.out.println("3. Bez żadnego trybu");
		System.out.println("4. Wyjście z programu");
		int choice;
		do
		{
			choice = scanner.nextInt();
			scanner.nextLine();
		}
		while(choice < 1 || choice > 4);
		switch(choice)
		{
		case 1: learn(); break;
		case 2: test(); break;
		case 4: return false;
		}
		return true;
	}
	
	private void learn()
	{
		learning.learn(vectors, 0.01f);
		scanner.nextLine();
		learning.stopLearning();
	}
	
	private void test()
	{
		float[] input = new float[2];
		System.out.println("Podaj wartości wejściowe(2):");
		input[0] = scanner.nextFloat();
		input[1] = scanner.nextFloat();
		SupervisedLearnVector vector = new SupervisedLearnVector(input, null);
		System.out.println("Wyjście: " + network.testVector(vector));
	}
	
	@Override
	public void onLearnedVector(float[] errors)
	{
		
	}
	
	@Override
	public void onLearnedEpoch(double meanSquareError, float highestError)
	{
		System.out.println(meanSquareError);
	}
	
	@Override
	public void onLearningEnded()
	{
		System.out.println("Koniec uczenia");
	}
	
	public static void main(String[] args)
	{
		new Main();
	}
}