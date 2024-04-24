package it.univr.model.parameters;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class FXcurrencyParameterFunction implements ParameterFunctionInterface {

	private double kdom;
	private double kfor;
	private double thetadom;
	private double thetafor;
	private double sigmafx;
	private double sigmadom;
	private double sigmafor;
	private double rhofxdom;
	private double rhofxfor;
	private double rhodomfor;
	
	private ArrayRealVector drift = new ArrayRealVector(3);
	private Array2DRowRealMatrix diffusion = new Array2DRowRealMatrix(3,3);
	
	public FXcurrencyParameterFunction(double kdom, double kfor, double thetadom, double thetafor, double sigmafx, double sigmadom, double sigmafor,
			double rhofxdom, double rhofxfor, double rhodomfor) {
		
		this.kdom = kdom;
		this.kfor = kfor;
		this.thetadom = thetadom;
		this.thetafor = thetafor;
		this.sigmafx = sigmafx;
		this.sigmadom = sigmadom;
		this.sigmafor = sigmafor;
		this.rhofxdom = rhofxdom;
		this.rhofxfor = rhofxfor;
		this.rhodomfor = rhodomfor;
	}

	@Override
	public RealVector getDriftValue(double[] stateVariable, double time) {
		this.drift.setEntry(0, (stateVariable[1] - stateVariable[2])*stateVariable[0]);
		this.drift.setEntry(1, (this.thetadom - this.kdom*stateVariable[1]));
		this.drift.setEntry(2, (this.thetafor - this.kfor*stateVariable[2] - this.rhofxfor*this.sigmafor*this.sigmafx));
		
		return drift;
	}

	@Override
	public Array2DRowRealMatrix getDiffusionValue(double[] stateVariable, double time) {

		this.diffusion.setEntry(0, 0, this.sigmafx*stateVariable[0]*this.rhofxfor);
		this.diffusion.setEntry(0, 1, this.sigmafx*stateVariable[0]*(this.rhofxdom - this.rhofxfor*this.rhodomfor)/Math.sqrt(1 - Math.pow(this.rhodomfor, 2)));
		this.diffusion.setEntry(0, 2, this.sigmafx*stateVariable[0]*Math.sqrt(1 - Math.pow(this.rhofxfor, 2) - Math.pow(this.rhofxdom - this.rhofxfor*this.rhodomfor, 2)/(1 - Math.pow(this.rhodomfor, 2))));
	
		this.diffusion.setEntry(1, 0, this.sigmadom*this.rhodomfor);
		this.diffusion.setEntry(1, 1, this.sigmadom*Math.sqrt(1 - Math.pow(this.rhodomfor, 2)));
		this.diffusion.setEntry(1, 2, 0.0);
		
		this.diffusion.setEntry(2, 0, this.sigmafor);
		this.diffusion.setEntry(2, 1, 0.0);
		this.diffusion.setEntry(2, 2, 0.0);

		
		return diffusion;
	}
}
