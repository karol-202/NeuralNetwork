package pl.karol202.neuralnetwork;

import java.io.PrintWriter;

public interface Activation
{
	float calculate(float value);

	float calcDerivative(float value);

	void dumpActivation(PrintWriter pw);
}
