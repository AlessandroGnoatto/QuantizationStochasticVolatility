package it.univr.model.parameters;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class SteinSteinParameterFunction implements ParameterFunctionInterface {

	private double r;
	private double sigma;
	private double k;
	private double theta;
	private double rho;
	
	
	private ArrayRealVector drift = new ArrayRealVector(2);
	private Array2DRowRealMatrix diffusion = new Array2DRowRealMatrix(2,2);
	
	public SteinSteinParameterFunction(double r, double sigma, double k, double theta, double rho) {
		
		
		this.r = r;
		this.sigma = sigma;
		this.k = k;
		this.theta = theta;
		this.rho = rho;
	}

	@Override
	public RealVector getDriftValue(double[] stateVariable, double time) {
		this.drift.setEntry(0, this.r * stateVariable[0]);
		this.drift.setEntry(1, this.k*(this.theta - stateVariable[1]));
		
		return drift;
	}

	@Override
	public Array2DRowRealMatrix getDiffusionValue(double[] stateVariable, double time) {

		this.diffusion.setEntry(0, 0, stateVariable[0]*stateVariable[1]*this.rho);
		//this.diffusion.setEntry(0, 0, stateVariable[0]*Math.max(stateVariable[1],0)*this.rho);
		this.diffusion.setEntry(0, 1, Math.sqrt(1-Math.pow(this.rho, 2))*stateVariable[1]*stateVariable[0]);
		//this.diffusion.setEntry(0, 1, Math.sqrt(1-Math.pow(this.rho, 2))*Math.max(stateVariable[1],0)*stateVariable[0]);
		this.diffusion.setEntry(1, 0, this.sigma);
		this.diffusion.setEntry(1, 1, 0);
		return diffusion;
	}
	
	
	

}
