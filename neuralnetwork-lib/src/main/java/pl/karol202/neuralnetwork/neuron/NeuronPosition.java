package pl.karol202.neuralnetwork.neuron;

import java.util.Arrays;

public class NeuronPosition
{
	private int[] coords;
	
	public NeuronPosition(int... coords)
	{
		this.coords = coords;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		
		NeuronPosition that = (NeuronPosition) o;
		
		return Arrays.equals(coords, that.coords);
	}
	
	@Override
	public int hashCode()
	{
		return Arrays.hashCode(coords);
	}
	
	float getDistance(NeuronPosition other)
	{
		if(other.coords.length != coords.length) throw new IllegalArgumentException("Invalid dimensions amount.");
		float sum = 0f;
		for(int i = 0; i < coords.length; i++) sum += Math.pow(other.coords[i] - coords[i], 2);
		return (float) Math.sqrt(sum);
	}
	
	public int getDimensions()
	{
		return coords.length;
	}
	
	public int getCoord(int dimension)
	{
		return coords[dimension];
	}
}