package it.univr.model;

import org.apache.commons.math3.linear.RealVector;

import it.univr.model.parameters.DoubleHestonParameterFunction;
import it.univr.model.parameters.ParameterFunctionInterface;

public class DoubleHestonModel extends AbstractModel{

	private double riskFree;
	private int numberOfProcess = 3;
	private int numberOfRiskFactors = 4;
	private ParameterFunctionInterface parameters;
	public DoubleHestonModel(ParameterFunctionInterface parameters) {
	
		this.parameters = parameters;
	}

	public DoubleHestonModel(double r, double k1, double k2, double theta1, double theta2, double sigma1,
							double sigma2, double rho1, double rho2) {
		this.riskFree = r;
		this.parameters = new DoubleHestonParameterFunction(r,k1,k2,theta1,theta2,sigma1,sigma2,rho1,rho2);
	}
	
	@Override
	public double getRiskFreeRate() {
	
		return this.riskFree;
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

}

