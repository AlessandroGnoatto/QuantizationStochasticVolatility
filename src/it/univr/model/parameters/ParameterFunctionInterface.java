package it.univr.model.parameters;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;

public interface ParameterFunctionInterface {

	public RealVector getDriftValue(double[] stateVariable, double time);
	
	public Array2DRowRealMatrix getDiffusionValue(double[] stateVariable, double time);
}
