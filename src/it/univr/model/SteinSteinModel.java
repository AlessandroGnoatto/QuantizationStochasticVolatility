package it.univr.model;

import org.apache.commons.math3.linear.RealVector;

import it.univr.model.parameters.ParameterFunctionInterface;
import it.univr.model.parameters.SteinSteinParameterFunction;

public class SteinSteinModel implements ModelInterface {

	private ParameterFunctionInterface parameters;
	private int numberOfProcess = 2;
	private int numberOfRiskFactors = 2;
	private double riskFreeRate;
	
	
	
	public SteinSteinModel(double r, double sigma, double k, double theta, double rho) {
		this.riskFreeRate = r;
		this.parameters = new SteinSteinParameterFunction(r,sigma,k,theta,rho);
	}

	public SteinSteinModel(ParameterFunctionInterface parameters) {

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
		
		return this.riskFreeRate;
	}
	
	@Override
	public ParameterFunctionInterface getParameters() {
		return this.parameters;
	}

}

