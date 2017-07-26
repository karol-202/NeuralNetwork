package pl.karol202.lettersrecognition;

import pl.karol202.neuralnetwork.*;
import pl.karol202.neuralnetwork.Vector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class Main implements Network.OnLearningListener
{
	private static final String PATH_LETTERS = "res/Litery";
	
	private Scanner scanner;
	private Network network;
	private List<LetterVector> vectors;
	
	public Main() throws IOException
	{
		scanner = new Scanner(System.in);
		
		network = createNetwork();
		network.randomWeights(-0.1f, 0.1f);
		System.out.println("Sieć utworzona.");
		vectors = createVectors();
		System.out.println("Wektory załadowane.");
		waitForInput();
	}
	
	private Network createNetwork()
	{
		Neuron[] neurons = new Neuron[12];
		for(int i = 0; i < neurons.length; i++)
			neurons[i] = new Neuron(15, new ActivationSigmoidal(1f));
		Layer layer = new Layer(neurons);
		return new Network(new Layer[] { layer }, 0.2f);
	}
	
	private List<LetterVector> createVectors() throws IOException
	{
		List<LetterVector> vectors = new ArrayList<>();
		File directory = new File(PATH_LETTERS);
		File[] files = directory.listFiles();
		if(files == null) return vectors;
		
		for(File file : files)
		{
			char ch = file.getName().charAt(0);
			if(ch >= 'a' && ch <= 'z') vectors.add(createVectorForLetter(ch, file.getPath()));
		}
		return vectors;
	}
	
	private LetterVector createVectorForLetter(char letter, String path) throws IOException
	{
		BufferedImage image = ImageIO.read(new File(path));
		
		float[] vectorIn = getPixelsFromImage(image);
		float[] vectorOut = new float[12];
		vectorOut[letter - 'a'] = 1;
		
		return new LetterVector(letter, vectorIn, vectorOut);
	}
	
	private float[] getPixelsFromImage(BufferedImage image)
	{
		float[] pixels = new float[image.getWidth() * image.getHeight()];
		for(int x = 0; x < image.getWidth(); x++)
			for(int y = 0; y < image.getHeight(); y++)
			{
				int pixel = image.getRGB(x, y);
				int red = pixel & 0xFF0000 >> 16;
				int green = pixel & 0xFF00 >> 8;
				int blue = pixel & 0xFF;
				int value = Math.max(red, Math.max(green, blue));
				pixels[y * image.getWidth() + x] = map(value, 0, 100, 1, -1);
			}
		return pixels;
	}
	
	private float map(float src, float srcMin, float srcMax, float dstMin, float dstMax)
	{
		float srcPoint = (src - srcMin) / (srcMax - srcMin);
		return lerp(srcPoint, dstMin, dstMax);
	}
	
	private float lerp(float value, float v1, float v2)
	{
		return v1 + value * (v2 - v1);
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
		network.learnContinuous(vectors, 0.03f, this);
		
		scanner.next();
		network.stopLearning();
	}
	
	private void recognizeMode() throws IOException
	{
		scanner.nextLine();
		while(true)
		{
			System.out.println("------------------------");
			System.out.println("Podaj nazwę pliku, lub wciśnij Enter, aby wyjść do menu: ");
			String fileName = scanner.nextLine();
			if(fileName.isEmpty()) break;
			else recognize(fileName);
		}
	}
	
	private void recognize(String fileName) throws IOException
	{
		String path = String.format("%s/%s", PATH_LETTERS, fileName);
		BufferedImage image = ImageIO.read(new File(path));
		
		float[] pixels = getPixelsFromImage(image);
		Vector vector = new Vector(pixels, null);
		
		float[] output = network.test(vector);
		System.out.println("Wyjście: " + outputToStringArray(output));
	}
	
	private String outputToStringArray(float[] output)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		for(int i = 0; i < output.length; i++)
			pw.printf("%c: %f\n         ", i + 'a', output[i]);
		return sw.toString();
	}
	
	private void resetWeights()
	{
		network.randomWeights(-0.1f, 0.1f);
	}
	
	@Override
	public void onLearning(Vector vector, float[] errors, boolean learning, boolean stop)
	{
		if(learning && !stop) System.out.println("Błąd: " + errorsToStringArray(errors));
		else System.out.println("Uczenie zakończone");
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