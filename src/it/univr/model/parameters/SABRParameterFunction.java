package it.univr.model.parameters;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class SABRParameterFunction implements ParameterFunctionInterface{
	
	private double alpha;
	private double beta;
	private double rho;
	
	private ArrayRealVector drift = new ArrayRealVector(2);
	private Array2DRowRealMatrix diffusion = new Array2DRowRealMatrix(2,2);
	
	public SABRParameterFunction(double alpha, double beta, double rho) {
		
		this.alpha = alpha;
		this.beta = beta;
		this.rho = rho;
	}

	@Override
	public RealVector getDriftValue(double[] stateVariable, double time) {
		this.drift.setEntry(0, 0);
		this.drift.setEntry(1, 0);
		
		return drift;
	}

	@Override
	public Array2DRowRealMatrix getDiffusionValue(double[] stateVariable, double time) {

		this.diffusion.setEntry(0, 0, this.rho*Math.pow(stateVariable[0],this.beta)*stateVariable[1]);
		this.diffusion.setEntry(0, 1, Math.sqrt(1-Math.pow(this.rho, 2))*stateVariable[1]*Math.pow(stateVariable[0],this.beta));
		this.diffusion.setEntry(1, 0, this.alpha*stateVariable[1]);
		this.diffusion.setEntry(1, 1, 0);
		return diffusion;
	}
	
	
	
}