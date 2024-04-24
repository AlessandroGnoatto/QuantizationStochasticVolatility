package it.univr.model;

import org.apache.commons.math3.linear.RealVector;

import it.univr.model.parameters.ParameterFunctionInterface;
import it.univr.model.parameters.PseudoCEVParameterFunction;

public class PseudoCEVModel extends AbstractModel {

	private ParameterFunctionInterface parameters;
	private int numberOfProcess = 1;
	private int numberOfRiskFactors = 1;
	private double riskfree;
	
	
	public PseudoCEVModel(double r, double theta, double delta) {

		this.riskfree = r;
		this.parameters = new PseudoCEVParameterFunction(r,theta,delta);
	}

	public PseudoCEVModel(ParameterFunctionInterface parameters) {

		this.parameters = parameters;
	}

	@Override
	public double getDrift(double[] stateVariable, double time, int process) {

		return this.parameters.getDriftValue(stateVariable, time).getEntry(0);
	}

	@Override
	public double getDiffusion(double[] stateVariable, double time, int process, int riskFactor) {

		return this.parameters.getDiffusionValue(stateVariable, time).getEntry(0,0);
	}

	@Override
	public RealVector getDiffusionRow(double[] stateVariable, double time, int process) {

		return this.parameters.getDiffusionValue(stateVariable, time).getRowVector(0);
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
		
		return this.riskfree;
	}


}
