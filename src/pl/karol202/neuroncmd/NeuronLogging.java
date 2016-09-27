package pl.karol202.neuroncmd;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Consumer;
import java.util.logging.*;

public class NeuronLogging
{
	private static final String lineSeparator = System.lineSeparator();

	public static final Logger LOGGER = (Logger) Logger.getLogger(NeuronLogging.class.getName());

	public static void init(String fileName) throws IOException
	{
		File logFile = new File(fileName);
		if(logFile.exists()) logFile.delete();

		FileHandler fileHandler = new FileHandler(fileName);
		fileHandler.setFormatter(new LogFormatter());

		LOGGER.setUseParentHandlers(false);
		LOGGER.setLevel(Level.FINE);
		LOGGER.addHandler(fileHandler);
	}

	public static String[] readData(Consumer<PrintWriter> consumer)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		consumer.accept(pw);
		return sw.toString().split(lineSeparator);
	}

	public static String[] floatArrayToStringArray(float[] array)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		for(float value : array)
			pw.println(value);
		return sw.toString().split(lineSeparator);
	}

	public static String floatArrayToString(float[] array)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		for(int i = 0; i < array.length; i++)
			pw.print(array[i] + ((i != array.length - 1) ? ", " : ""));
		return sw.toString();
	}

	public static void log(Level level, String[] msg)
	{
		for(String string : msg) LOGGER.log(level, string);
	}

	public static void severe(String[] msg)
	{
		log(Level.SEVERE, msg);
	}

	public static void warning(String[] msg)
	{
		log(Level.WARNING, msg);
	}

	public static void info(String[] msg)
	{
		log(Level.INFO, msg);
	}

	public static void config(String[] msg)
	{
		log(Level.CONFIG, msg);
	}

	public static void fine(String[] msg)
	{
		log(Level.FINE, msg);
	}

	public static void finer(String[] msg)
	{
		log(Level.FINER, msg);
	}

	public static void finest(String[] msg)
	{
		log(Level.FINEST, msg);
	}
}
