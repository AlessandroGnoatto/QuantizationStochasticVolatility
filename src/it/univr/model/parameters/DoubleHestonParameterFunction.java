package it.univr.model.parameters;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class DoubleHestonParameterFunction implements ParameterFunctionInterface {

	private double r;
	private double k1;
	private double k2;
	private double theta1;
	private double theta2;
	private double sigma1;
	private double sigma2;
	private double rho1;
	private double rho2;
	
	private ArrayRealVector drift = new ArrayRealVector(3);
	private Array2DRowRealMatrix diffusion = new Array2DRowRealMatrix(3,4);
	
	public DoubleHestonParameterFunction(double r, double k1, double k2, double theta1, double theta2, double sigma1, double sigma2,
			double rho1, double rho2) {
		
		this.r = r;
		this.k1 = k1;
		this.k2 = k2;
		this.theta1 = theta2;
		this.theta2 = theta2;
		this.sigma1 = sigma1;
		this.sigma2 = sigma2;
		this.rho1 = rho1;
		this.rho2 = rho2;
	}

	@Override
	public RealVector getDriftValue(double[] stateVariable, double time) {
		this.drift.setEntry(0, r*stateVariable[0]);
		this.drift.setEntry(1, this.k1*(this.theta1 - Math.abs(stateVariable[1])));
		this.drift.setEntry(2, this.k2*(this.theta2 - Math.abs(stateVariable[2])));
		
		return drift;
	}

	@Override
	public Array2DRowRealMatrix getDiffusionValue(double[] stateVariable, double time) {

		this.diffusion.setEntry(0, 0, Math.sqrt(Math.abs(stateVariable[1]))*this.rho1*stateVariable[0]);
		this.diffusion.setEntry(0, 1, Math.sqrt(Math.abs(stateVariable[1]))*Math.sqrt(1-Math.pow(this.rho1,2))*stateVariable[0]);
		this.diffusion.setEntry(0, 2, Math.sqrt(Math.abs(stateVariable[2]))*this.rho2*stateVariable[0]);
		this.diffusion.setEntry(0, 3, Math.sqrt(Math.abs(stateVariable[2]))*Math.sqrt(1-Math.pow(this.rho2,2))*stateVariable[0]);
		
		this.diffusion.setEntry(1, 0, this.sigma1*Math.sqrt(Math.abs(stateVariable[1])));
		this.diffusion.setEntry(1, 1, 0.0);
		this.diffusion.setEntry(1, 2, 0.0);
		this.diffusion.setEntry(1, 3, 0.0);
		
		this.diffusion.setEntry(2, 0, 0.0);
		this.diffusion.setEntry(2, 1, 0.0);
		this.diffusion.setEntry(2, 2, this.sigma2*Math.sqrt(Math.abs(stateVariable[2])));
		this.diffusion.setEntry(2, 3, 0.0);
		
		return diffusion;
	}
}
