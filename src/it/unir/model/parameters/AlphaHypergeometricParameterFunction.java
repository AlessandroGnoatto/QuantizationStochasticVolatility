package it.univr.model.parameters;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class AlphaHypergeometricParameterFunction implements ParameterFunctionInterface {

	private double a;
	private double b;
	private double alpha;
	private double sigma;
	private double rho;
	
	private ArrayRealVector drift = new ArrayRealVector(2);
	private Array2DRowRealMatrix diffusion = new Array2DRowRealMatrix(2,2);
	
	public AlphaHypergeometricParameterFunction(double a, double b, double alpha, double sigma, double rho) {
		
		this.a = a;
		this.b = b;
		this.alpha = alpha;
		this.sigma = sigma;
		this.rho = rho;
	}

	@Override
	public RealVector getDriftValue(double[] stateVariable, double time) {
		this.drift.setEntry(0, 0);
		this.drift.setEntry(1, (this.a - this.b*Math.exp(stateVariable[1]*this.alpha)));
		
		return drift;
	}

	@Override
	public Array2DRowRealMatrix getDiffusionValue(double[] stateVariable, double time) {

		this.diffusion.setEntry(0, 0, this.rho*Math.exp(stateVariable[1])*stateVariable[0]);
		this.diffusion.setEntry(0, 1, Math.sqrt(1-Math.pow(this.rho, 2))*Math.exp(stateVariable[1])*stateVariable[0]);
		this.diffusion.setEntry(1, 0, this.sigma);
		this.diffusion.setEntry(1, 1, 0);
		return diffusion;
	}
}
