package pl.karol202.imagerecognition;

import pl.karol202.neuralnetwork.ContinuousSupervisedLearning;
import pl.karol202.neuralnetwork.ContinuousSupervisedLearning.LearningListener;
import pl.karol202.neuralnetwork.ContinuousTesting;
import pl.karol202.neuralnetwork.NetworkLoader;
import pl.karol202.neuralnetwork.activation.ActivationSigmoidal;
import pl.karol202.neuralnetwork.layer.SupervisedLearnLayer;
import pl.karol202.neuralnetwork.network.SupervisedLearnNetwork;
import pl.karol202.neuralnetwork.neuron.SupervisedLearnNeuron;
import pl.karol202.neuralnetwork.output.NominalOutput;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main implements LearningListener, ContinuousTesting.TestingListener<DigitVector, Integer>
{
	private static final String PATH_TRAIN_IMAGES = "res/imagerecognition/Cyfry/train.images";
	private static final String PATH_TRAIN_LABELS = "res/imagerecognition/Cyfry/train.labels";
	private static final String PATH_TEST_IMAGES = "res/imagerecognition/Cyfry/test.images";
	private static final String PATH_TEST_LABELS = "res/imagerecognition/Cyfry/test.labels";
	private static final String PATH_NETWORK_DATA = "res/imagerecognition/network.dat";
	
	private static final int MAX_TRAIN_IMAGES = 60000;
	private static final int MAX_TEST_IMAGES = 10000;
	
	private static final float INITIAL_LEARN_RATE = 0.3f;
	private static final float INITIAL_MOMENTUM = 0.3f;
	
	private DigitImageLoader trainImageLoader;
	private DigitImageLoader testImageLoader;
	private List<DigitVector> trainVectors;
	private List<DigitVector> testVectors;
	
	private SupervisedLearnNetwork<Integer, DigitVector> network;
	private ContinuousSupervisedLearning learning;
	private ContinuousTesting<DigitVector, Integer> testing;
	
	private File networkFile;
	private NetworkLoader networkLoader;
	
	private FrameMain frameMain;
	
	private Scanner scanner;
	
	private List<RecognitionResult> recognitionResults;
	private int recognizedCorrectly;
	private int recognizedIncorrectly;
	private int notRecognized;
	
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
		learning = new ContinuousSupervisedLearning(network, this);
		testing = new ContinuousTesting<>(network, this);
		
		networkFile = new File(PATH_NETWORK_DATA);
		networkLoader = new NetworkLoader(network);
		networkLoader.tryToLoadNetworkData(networkFile);
		System.out.println("Sieć utworzona.");
		
		frameMain = new FrameMain();
		
		scanner = new Scanner(System.in);
		waitForInput();
	}
	
	private SupervisedLearnNetwork<Integer, DigitVector> createNetwork()
	{
		int inputs = trainImageLoader.getWidth() * trainImageLoader.getHeight();
		
		SupervisedLearnNeuron[] hiddenNodes = new SupervisedLearnNeuron[300];
		for(int i = 0; i < hiddenNodes.length; i++)
			hiddenNodes[i] = new SupervisedLearnNeuron(inputs, new ActivationSigmoidal(1.2f));
		SupervisedLearnLayer hiddenLayer = new SupervisedLearnLayer(hiddenNodes);
		
		SupervisedLearnNeuron[] outputNodes = new SupervisedLearnNeuron[10];
		for(int i = 0; i < outputNodes.length; i++)
			outputNodes[i] = new SupervisedLearnNeuron(300, new ActivationSigmoidal(1.2f));
		SupervisedLearnLayer outputLayer = new SupervisedLearnLayer(outputNodes);
		
		return new SupervisedLearnNetwork<>(new SupervisedLearnLayer[] { hiddenLayer, outputLayer }, INITIAL_LEARN_RATE, INITIAL_MOMENTUM,
							 new NominalOutput<>(i -> i, 0.7f));
	}
	
	private List<DigitVector> createTrainVectors() throws IOException
	{
		return Stream.of(trainImageLoader.loadImages(MAX_TRAIN_IMAGES))
					 .map(DigitVector::new)
					 .collect(Collectors.toList());
	}
	
	private List<DigitVector> createTestVectors() throws IOException
	{
		return Stream.of(testImageLoader.loadImages(MAX_TEST_IMAGES))
					 .map(DigitVector::new)
					 .collect(Collectors.toList());
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
		recognitionResults = new ArrayList<>();
		recognizedCorrectly = 0;
		recognizedIncorrectly = 0;
		notRecognized = 0;
		testing.test(testVectors);
		
		scanner.nextLine();
		testing.stopLearning();
	}
	
	private void resetWeights()
	{
		network.randomWeights(-0.35f, 0.35f);
		network.setLearnRate(INITIAL_LEARN_RATE);
		networkLoader.tryToSaveNetworkData(networkFile);
	}
	
	@Override
	public void onLearnedVector(float[] errors)
	{
		//System.out.println("Błąd: " + errorsToStringArray(errors));
	}
	
	@Override
	public void onLearnedEpoch(double meanSquareError, float highestError)
	{
		System.out.printf("Błąd średniokwadratowy: %f\n", meanSquareError);
		System.out.printf("Największy błąd: %f\n\n", highestError);
	}
	
	@Override
	public void onLearningEnded()
	{
		System.out.println("Uczenie zakończone");
		networkLoader.tryToSaveNetworkData(networkFile);
	}
	
	@Override
	public void onTesting(DigitVector vector, Integer output)
	{
		if(output == null) System.out.println("Nierozpoznano.");
		else System.out.printf("Rozpoznano: %d, oczekiwano: %d. %s\n", output, vector.getDigit(),
							   output == vector.getDigit() ? "Rozpoznano poprawnie" : "Błąd");
		
		if(output == null) notRecognized++;
		else if(output == vector.getDigit()) recognizedCorrectly++;
		else recognizedIncorrectly++;
		
		recognitionResults.add(new RecognitionResult(vector.getImage(), output != null ? output : -1));
	}
	
	@Override
	public void onTestingEnded()
	{
		System.out.println("Testowanie zakończone");
		System.out.println("Rozpoznano poprawnie: " + recognizedCorrectly);
		System.out.println("Rozpoznano błędnie: " + recognizedIncorrectly);
		System.out.println("Nierozpoznano: " + notRecognized);
		frameMain.update(recognitionResults);
	}
	
	/*private String errorsToStringArray(float[] errors)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		for(float value : errors)
			pw.printf("%f\n      ", value);
		return sw.toString();
	}*/
	
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