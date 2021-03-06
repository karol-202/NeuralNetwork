package pl.karol202.neuralnetwork.activation;

import java.io.PrintWriter;

public interface Activation
{
	float calculate(float value);

	float calcDerivative(float value);

	void dumpActivation(PrintWriter pw);
}
