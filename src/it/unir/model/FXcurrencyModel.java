package it.univr.model;

import org.apache.commons.math3.linear.RealVector;

import it.univr.model.parameters.FXcurrencyParameterFunction;
import it.univr.model.parameters.ParameterFunctionInterface;

public class FXcurrencyModel extends AbstractModel{

	private ParameterFunctionInterface parameters;
	private int numberOfProcess = 3;
	private int numberOfRiskFactors = 3;
	private double riskFree = 0.0;
	
	public FXcurrencyModel(double kdom, double kfor, double thetadom, double thetafor, double sigmafx,
							double sigmadom, double sigmafor, double rhofxdom, double rhofxfor, double rhodomfor) {
		
		this.parameters = new FXcurrencyParameterFunction(kdom,kfor,thetadom,thetafor,sigmafx,sigmadom,sigmafor,rhofxdom,rhofxfor,rhodomfor);
	}
	
	public FXcurrencyModel(ParameterFunctionInterface parameters) {

		this.parameters = parameters;
	}

	@Override
	public double getDrift(double[] stateVariable, double time, int process) {

		return this.parameters.getDriftValue(stateVariable, time).getEntry(process-1);
	}

	@Override
	public double getDiffusion(double[] stateVariable, double time, int process, int riskFactor) {

		return this.parameters.getDiffusionValue(stateVariable, time).getEntry(process-1, riskFactor-1);
	}

	@Override
	public RealVector getDiffusionRow(double[] stateVariable, double time, int process) {

		return this.parameters.getDiffusionValue(stateVariable, time).getRowVector(process-1);
	}

	@Override
	public int getNumberOfProcess() {

		return this.numberOfProcess;
	}

	@Override
	public int getNumberOfRiskFactors() {

		return this.numberOfRiskFactors;
	}

	@Override
	public double getRiskFreeRate() {

		return this.riskFree;
	}


}

