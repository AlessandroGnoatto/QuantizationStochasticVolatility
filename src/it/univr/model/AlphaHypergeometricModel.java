package it.univr.model;

import org.apache.commons.math3.linear.RealVector;

import it.univr.model.parameters.AlphaHypergeometricParameterFunction;
import it.univr.model.parameters.ParameterFunctionInterface;

public class AlphaHypergeometricModel extends AbstractModel{

	private ParameterFunctionInterface parameters;
	private int numberOfProcess = 2;
	private int numberOfRiskFactors = 2;

	
	public AlphaHypergeometricModel(double a, double b, double alpha, double sigma, double rho) {
		
		this.parameters = new AlphaHypergeometricParameterFunction(a,b,alpha,sigma,rho);
	}
	
	public AlphaHypergeometricModel(ParameterFunctionInterface parameters) {

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

		return 0;
	}


}

