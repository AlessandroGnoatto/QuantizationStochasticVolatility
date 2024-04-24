package it.univr.model.parameters;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class CorrelatedBlackScholesParameterFunction implements ParameterFunctionInterface {

	private double r;
	private double sigma1;
	private double sigma2;
	private double rho;
	
	private ArrayRealVector drift = new ArrayRealVector(2);
	private Array2DRowRealMatrix diffusion = new Array2DRowRealMatrix(2,2);

	@Override
	public RealVector getDriftValue(double[] stateVariable, double time) {
		this.drift.setEntry(0, this.r*stateVariable[0]);
		this.drift.setEntry(1, this.r*stateVariable[1]);
		
		return drift;
	}

	public CorrelatedBlackScholesParameterFunction(double r, double sigma1, double sigma2, double rho) {

		this.r = r;
		this.sigma1 = sigma1;
		this.sigma2 = sigma2;
		this.rho = rho;

	}

	@Override
	public Array2DRowRealMatrix getDiffusionValue(double[] stateVariable, double time) {
		this.diffusion.setEntry(0, 0, this.rho*this.sigma1*stateVariable[0] );
		this.diffusion.setEntry(0, 1, Math.sqrt(1-Math.pow(this.rho, 2))*this.sigma1*stateVariable[0]);
		this.diffusion.setEntry(1, 0, this.sigma2*stateVariable[1]);
		this.diffusion.setEntry(1, 1, 0);
		
		return diffusion;
	}

}
