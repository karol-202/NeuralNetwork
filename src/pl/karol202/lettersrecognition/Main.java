package pl.karol202.lettersrecognition;

import pl.karol202.neuralnetwork.*;
import pl.karol202.neuralnetwork.ContinuousLearning.LearningListener;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main implements LearningListener
{
	private static final String PATH_TRAIN_IMAGES = "res/Cyfry/train.images";
	private static final String PATH_TRAIN_LABELS = "res/Cyfry/train.labels";
	private static final String PATH_TEST_IMAGES = "res/Cyfry/test.images";
	private static final String PATH_TEST_LABELS = "res/Cyfry/test.labels";
	
	private static final int MAX_TRAIN_IMAGES = 20000;
	private static final int MAX_TEST_IMAGES = 10000;
	
	private DigitImageLoader trainImageLoader;
	private DigitImageLoader testImageLoader;
	private List<DigitVector> trainVectors;
	private List<DigitVector> testVectors;
	
	private Network network;
	private ContinuousLearning learning;
	
	private Scanner scanner;
	
	public Main() throws IOException
	{
		trainImageLoader = new DigitImageLoader(new File(PATH_TRAIN_IMAGES), new File(PATH_TRAIN_LABELS));
		testImageLoader = new DigitImageLoader(new File(PATH_TEST_IMAGES), new File(PATH_TEST_LABELS));
		
		System.out.println("Ładowanie wektorów...");
		trainVectors = createTrainVectors();
		testVectors = createTestVectors();
		System.out.println("Wektory załadowane.");
		
		network = createNetwork();
		network.randomWeights(-0.1f, 0.1f);
		learning = new ContinuousLearning(network, this);
		System.out.println("Sieć utworzona.");
		
		scanner = new Scanner(System.in);
		waitForInput();
	}
	
	private Network createNetwork()
	{
		int inputs = trainImageLoader.getWidth() * trainImageLoader.getHeight();
		
		Neuron[] hiddenNodes = new Neuron[300];
		for(int i = 0; i < hiddenNodes.length; i++)
			hiddenNodes[i] = new Neuron(inputs, new ActivationSigmoidal(1f));
		Layer hiddenLayer = new Layer(hiddenNodes);
		
		Neuron[] outputNodes = new Neuron[10];
		for(int i = 0; i < outputNodes.length; i++)
			outputNodes[i] = new Neuron(300, new ActivationSigmoidal(1f));
		Layer outputLayer = new Layer(outputNodes);
		
		return new Network(new Layer[] { hiddenLayer, outputLayer }, 0.1f);
	}
	
	private List<DigitVector> createTrainVectors() throws IOException
	{
		return Stream.of(trainImageLoader.loadImages(MAX_TRAIN_IMAGES))
					 .map(this::tryToCreateVectorFromImage)
					 .collect(Collectors.toList());
	}
	
	private List<DigitVector> createTestVectors() throws IOException
	{
		return Stream.of(testImageLoader.loadImages(MAX_TEST_IMAGES))
					 .map(this::tryToCreateVectorFromImage)
					 .collect(Collectors.toList());
	}
	
	private DigitVector tryToCreateVectorFromImage(DigitImage image)
	{
		try
		{
			return createVectorFromImage(image);
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
			return null;
		}
	}
	
	private DigitVector createVectorFromImage(DigitImage image) throws IOException
	{
		float[] vectorIn = image.getPixels();
		float[] vectorOut = new float[10];
		vectorOut[image.getDigit()] = 1;
		
		return new DigitVector(image.getDigit(), vectorIn, vectorOut);
	}
	
	private void waitForInput() throws IOException
	{
		while(true)
		{
			System.out.println("------------------------");
			System.out.println("Rozpoznawanie liter");
			System.out.println("Wybierz czynność:");
			System.out.println("1. Tryb uczenia");
			System.out.println("2. Tryb rozpoznawania");
			System.out.println("3. Bez żadnego trybu!");
			System.out.println("4. Resetowanie wag");
			System.out.println("5. Wyjście z programu");
			
			int choice = 0;
			while(choice == 0)
			{
				choice = scanner.nextInt();
				scanner.nextLine();
				switch(choice)
				{
				case 1: learnMode(); break;
				case 2: recognizeMode(); break;
				case 4: resetWeights(); break;
				case 5: System.exit(0); break;
				default:
					System.out.println("Nieprawidłowy wybór.");
					choice = 0;
				}
			}
		}
	}
	
	private void learnMode()
	{
		System.out.println("Uczenie...");
		learning.learn(trainVectors, 0.05f);
		
		scanner.nextLine();
		learning.stopLearning();
	}
	
	private void recognizeMode() throws IOException
	{
		System.out.println("Rozpoznawanie...");
		
		int positive = 0;
		int negative = 0;
		for(DigitVector vector : testVectors)
		{
			float[] output = network.testVector(vector);
			
			float highest = 0f;
			int highestsIndex = -1;
			for(int i = 0; i < output.length; i++)
			{
				float value = output[i];
				if(value < 0.7f || value <= highest) continue;
				highest = value;
				highestsIndex = i;
			}
			
			if(highestsIndex == -1) System.out.println("Nierozpoznano");
			else System.out.printf("Rozpoznano: %d  Prawidłowo: %d\n", highestsIndex, vector.getDigit());
			
			if(highestsIndex == vector.getDigit()) positive++;
			else negative++;
		}
		System.out.println(positive + "    " + negative);
	}
	
	private void resetWeights()
	{
		network.randomWeights(-0.1f, 0.1f);
	}
	
	@Override
	public void onLearning(float[] errors)
	{
		System.out.println("Błąd: " + errorsToStringArray(errors));
	}
	
	@Override
	public void onLearningEnded()
	{
		System.out.println("Uczenie zakończone");
	}
	
	private String errorsToStringArray(float[] errors)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		for(float value : errors)
			pw.printf("%f\n      ", value);
		return sw.toString();
	}
	
	public static void main(String[] args)
	{
		try
		{
			new Main();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}