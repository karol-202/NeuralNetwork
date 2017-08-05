package pl.karol202.neuralnetwork.output;

import java.util.function.Function;
import java.util.stream.IntStream;

public class NominalOutput<T> implements OutputType<T>
{
	private static class OutputPair<T> implements Comparable<OutputPair<T>>
	{
		private float value;
		private T output;
		
		OutputPair(float value, T output)
		{
			this.value = value;
			this.output = output;
		}
		
		@Override
		public int compareTo(OutputPair o)
		{
			return value == o.value ? 0 : (value > o.value ? -1 : 1);
		}
		
		private float getValue()
		{
			return value;
		}
		
		private T getOutput()
		{
			return output;
		}
	}
	
	private Function<Integer, T> outputFunction;
	private float threshold;
	private float minDistance;
	
	public NominalOutput(Function<Integer, T> outputFunction, float threshold)
	{
		this(outputFunction, threshold, 0f);
	}
	
	public NominalOutput(Function<Integer, T> outputFunction, float threshold, float minDistance)
	{
		this.outputFunction = outputFunction;
		this.threshold = threshold;
		this.minDistance = minDistance;
	}
	
	@Override
	public T transformOutput(float[] output)
	{
		return IntStream.range(0, output.length).mapToObj(i -> new OutputPair<>(output[i], outputFunction.apply(i)))
				.filter(p -> p.getValue() > threshold).sorted().limit(2)
				.reduce((p1, p2) -> p1.getValue() - minDistance > p2.getValue() ? p1 : new OutputPair<>(0f, null))
				.orElse(new OutputPair<>(0f, null)).getOutput();
	}
}