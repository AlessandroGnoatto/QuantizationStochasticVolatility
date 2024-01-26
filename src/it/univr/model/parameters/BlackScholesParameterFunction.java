package it.univr.model.parameters;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class BlackScholesParameterFunction implements ParameterFunctionInterface{

	double riskFreeRate;
	double sigma;
	
	private ArrayRealVector drift = new ArrayRealVector(1);
	private Array2DRowRealMatrix diffusion = new Array2DRowRealMatrix(1,1);

	public BlackScholesParameterFunction(double r, double sigma) {
		this.riskFreeRate = r;
		this.sigma = sigma;
		
	}


	@Override
	public RealVector getDriftValue(double[] stateVariable, double time) {
		this.drift.setEntry(0, this.riskFreeRate*stateVariable[0]);
		
		return drift;
	}


	@Override
	public Array2DRowRealMatrix getDiffusionValue(double[] stateVariable, double time) {
		this.diffusion.setEntry(0, 0, this.sigma*stateVariable[0] );
		
		return diffusion;
	}
	
}
