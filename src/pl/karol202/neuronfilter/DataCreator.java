package pl.karol202.neuronfilter;

import java.util.Random;

public class DataCreator
{
	public enum CurveType
	{
		SINUSOID("Sinusoida");

		private String name;

		CurveType(String name)
		{
			this.name = name;
		}

		public String toString()
		{
			return name;
		}
	}

	public static final float X_MIN = -7;
	public static final float X_MAX = 7;
	public static final float Y_MIN = -1.5f;
	public static final float Y_MAX = 1.5f;

	public static float[] generateCurve(int points, CurveType type)
	{
		Vector2f[] vectors = new Vector2f[points];
		for(int i = 0; i < points; i++)
		{
			float x = ((X_MAX - X_MIN) * (i / (points - 1.0f))) + X_MIN;
			vectors[i] = new Vector2f(x, 0);
		}

		switch(type)
		{
		case SINUSOID:
			for(int i = 0; i < points; i++)
			{
				float x = vectors[i].getX();
				double sin = (Math.sin(x * Math.toRadians(90)) / 2) + 0.5;
				System.out.println(x);
				System.out.println(sin);
				float y = (float) ((Y_MAX - Y_MIN) * sin) + Y_MIN;
				vectors[i].setY(y);
			}
			break;
		}

		float[] curve = new float[points];
		for(int i = 0; i < points; i++)
			curve[i] = vectors[i].getY();
		return curve;
	}

	public static float[] generateNoise(float[] pure, float amplitude)
	{
		float[] noise = new float[pure.length];
		Random rnd = new Random();
		for(int i = 0; i < pure.length; i++)
			noise[i] = pure[i] + (rnd.nextFloat() * amplitude * 2) - amplitude;
		return noise;
	}
}