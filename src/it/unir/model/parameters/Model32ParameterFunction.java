package it.univr.model.parameters;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class Model32ParameterFunction implements ParameterFunctionInterface {

	private double r;
	private double k;
	private double theta;
	private double sigma;
	private double rho;
	
	private ArrayRealVector drift = new ArrayRealVector(2);
	private Array2DRowRealMatrix diffusion = new Array2DRowRealMatrix(2,2);
	
	public Model32ParameterFunction(double r, double k, double theta, double sigma, double rho) {
		
		this.r = r;
		this.k = k;
		this.theta = theta;
		this.sigma = sigma;
		this.rho = rho;
	}

	@Override
	public RealVector getDriftValue(double[] stateVariable, double time) {
		this.drift.setEntry(0, this.r*stateVariable[0]);
		this.drift.setEntry(1, this.k*Math.abs(stateVariable[1])*(this.theta - Math.abs(stateVariable[1])));
		
		return drift;
	}

	@Override
	public Array2DRowRealMatrix getDiffusionValue(double[] stateVariable, double time) {

		this.diffusion.setEntry(0, 0, stateVariable[0]*Math.sqrt(Math.abs(stateVariable[1])));
		this.diffusion.setEntry(0, 1, 0);
		this.diffusion.setEntry(1, 0, this.sigma*Math.pow(Math.abs(stateVariable[1]), 3.0/2.0)*this.rho);
		this.diffusion.setEntry(1, 1, this.sigma*Math.pow(Math.abs(stateVariable[1]), 3.0/2.0)*Math.sqrt(1 - Math.pow(this.rho, 2)));
		return diffusion;
	}
}
