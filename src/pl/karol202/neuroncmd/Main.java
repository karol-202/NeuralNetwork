package pl.karol202.neuroncmd;

import pl.karol202.neuralnetwork.Network;
import pl.karol202.neuralnetwork.Vector;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Main implements Network.OnLearningListener
{
	private static final float DST_ERROR = 0.01f;
	private static final Logger LOGGER = NeuronLogging.LOGGER;

	private static final String PATH_LOG = "res/log.txt";
	private static final String PATH_NETWORK = "res/neurons.dat";
	private static final String PATH_VECTORS = "res/vectors.dat";
	private static final String PATH_DATA = "res/data.dat";

	private Network network;
	private BufferedReader ir;

	private Main()
	{
		try
		{
			NeuronLogging.init(PATH_LOG);
			network = NeuronSave.loadNetwork(PATH_NETWORK);
			NeuronSave.loadData(PATH_DATA, network);
			ir = new BufferedReader(new InputStreamReader(System.in));

			while(true)
			{
				System.out.println("--------------------");
				System.out.println("Sztuczna sieć neuronowa");
				System.out.println("Podaj typ operacji:");
				System.out.println("1. Wektory uczące z pliku");
				System.out.println("2. Reczne wprowadzanie danych");
				System.out.println("3. Wypisanie treści sieci");
				System.out.println("4. Resetowanie wag");
				System.out.println("5. Wyjście z programu");
				int choice = 0;
				while(choice == 0)
				{
					choice = (int) getNumber(false);
					switch(choice)
					{
					case 1:
						vectorsMode();
						break;
					case 2:
						manualMode();
						break;
					case 3:
						PrintWriter pw = new PrintWriter(System.out);
						network.dumpNetwork(pw);
						pw.flush();
						break;
					case 4:
						network.randomWeights(-0.1f, 0.1f);
						NeuronSave.saveData(PATH_DATA, network);
						break;
					case 5:
						break;
					default:
						System.out.println("Nieprawidłowy wybór");
						choice = 0;
					}
				}
				if(choice == 5) break;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void vectorsMode() throws Exception
	{
		while(true)
		{
			System.out.println("--------------------");
			System.out.println("Tryb wektorów z pliku");
			System.out.println("Podaj typ operacji:");
			System.out.println("1. Tryb ciągły (uczenie do skutku)");
			System.out.println("2. Wypisanie wektorów");
			System.out.println("3. Wyjście do menu głównego");
			int choice = 0;
			while(choice == 0)
			{
				choice = (int) getNumber(false);
				switch(choice)
				{
				case 1:
					vectorContinuousMode();
					break;
				case 2:
					dumpVectors();
					break;
				case 3:
					return;
				default:
					System.out.println("Nieprawidłowy wybór");
					choice = 0;
				}
			}
		}
	}

	private void vectorContinuousMode() throws Exception
	{
		LOGGER.info("-----------------------------------------");
		LOGGER.info("Rozpoczęcie uczenia w trybie ciągłym");
		System.out.println("Uczenie w trybie ciągłym...");

		ArrayList<Vector> vectors = NeuronSave.loadVector(PATH_VECTORS);
		network.learnContinuous(vectors, DST_ERROR, this);

		ir.readLine();
		network.stopLearning();
	}

	private void dumpVectors() throws FileNotFoundException, XMLStreamException
	{
		ArrayList<Vector> vectors = NeuronSave.loadVector(PATH_VECTORS);
		PrintWriter pw = new PrintWriter(System.out);
		for(Vector vector : vectors) vector.dumpVector(pw);
		pw.flush();
	}

	private void manualMode() throws Exception
	{
		while(true)
		{
			System.out.println("--------------------");
			System.out.println("Tryb ręcznego wprowadzania danych");
			System.out.println("Podaj typ operacji:");
			System.out.println("1. Uczenie sieci");
			System.out.println("2. Testowanie sieci");
			System.out.println("3. Wyjście do menu głównego");
			int choice = 0;
			while(choice == 0)
			{
				choice = (int) getNumber(false);
				switch(choice)
				{
				case 1:
					manualModeRun(true);
					break;
				case 2:
					manualModeRun(false);
					break;
				case 3: return;
				default:
					System.out.println("Nieprawidłowy wybór");
					choice = 0;
				}
			}
		}
	}

	private void manualModeRun(boolean learning) throws Exception
	{
		float[] inputs = new float[network.getInputsLength()];
		System.out.println("Podaj wartości wejść(" + inputs.length + "):");
		for(int i = 0; i < inputs.length; i++)
			inputs[i] = getNumber(false);
		if(learning)
		{
			System.out.println("Podaj oczekiwane wartości wyjściowe:");
			float[] reqOutput = new float[network.getOutputsLength()];
			for(int i = 0; i < reqOutput.length; i++)
				reqOutput[i] = getNumber(false);
			Vector vector = new Vector(inputs, reqOutput);

			LOGGER.info("-----------------------------------------");
			LOGGER.info("Uczenie sieci z wartościami wejściowymi:");
			NeuronLogging.info(NeuronLogging.floatArrayToStringArray(inputs));
			LOGGER.info("i oczekiwanymi wartościami wyjściowymi:");
			NeuronLogging.info(NeuronLogging.floatArrayToStringArray(reqOutput));

			float[] outputs = network.test(vector);

			LOGGER.info("Wartości wyjściowe");
			NeuronLogging.info(NeuronLogging.floatArrayToStringArray(outputs));
			System.out.println("Wynik: ");
			for(float output : outputs)
				System.out.println(output);

			float[] errors = network.learnSingle(vector);

			LOGGER.info("Błąd: ");
			NeuronLogging.info(NeuronLogging.floatArrayToStringArray(errors));
			System.out.println("Błąd: ");
			for(float error : errors)
				System.out.println(error);
			LOGGER.info("Uczenie zakończone");
			LOGGER.info("-----------------------------------------");
		}
		else
		{
			Vector vector = new Vector(inputs, null);

			LOGGER.info("-----------------------------------------");
			LOGGER.info("Testowanie sieci z wartościami wejściowymi:");
			NeuronLogging.info(NeuronLogging.floatArrayToStringArray(inputs));

			float[] outputs = network.test(vector);

			LOGGER.info("Wartości wyjściowe");
			NeuronLogging.info(NeuronLogging.floatArrayToStringArray(outputs));
			System.out.println("Wynik: ");
			for(float output : outputs)
				System.out.println(output);
			LOGGER.info("-----------------------------------------");
		}
		NeuronSave.saveData(PATH_DATA, network);
	}

	private float getNumber(boolean range) throws IOException
	{
		do
		{
			String line = ir.readLine();
			float number;
			try
			{
				number = Float.parseFloat(line);
				if((number >= -1 && number <= 1) || !range) return number;
			}
			catch(NumberFormatException e) {}
			System.out.println("Podaj liczbę" + (range ? " z przedziału od -1 do 1" : ""));
		}
		while(true);
	}

	@Override
	public void onLearning(Vector vector, float[] errors, boolean learning, boolean stop)
	{
		try
		{
			if(learning)
			{
				if(!stop)
				{
					LOGGER.info("Błąd: " + NeuronLogging.floatArrayToString(errors));
					System.out.println("Błąd: " + NeuronLogging.floatArrayToString(errors));
				}
				NeuronSave.saveData(PATH_DATA, network);
			}
			else
			{
				LOGGER.info("Uczenie zakończone");
				LOGGER.info("-----------------------------------------");
				System.out.println("Uczenie zakończone");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		new Main();
	}
}