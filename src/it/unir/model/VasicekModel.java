package it.univr.model;

import org.apache.commons.math3.linear.RealVector;

import it.univr.model.parameters.VasicekParameterFunction;
import it.univr.model.parameters.ParameterFunctionInterface;

public class VasicekModel extends AbstractModel {

	private ParameterFunctionInterface parameters;
	private int numberOfProcess = 1;
	private int numberOfRiskFactors = 1;
	
	public VasicekModel(double k, double theta, double sigma) {

		this.parameters = new VasicekParameterFunction(k,theta,sigma);
	}
	
	public VasicekModel(ParameterFunctionInterface parameters) {
		this.parameters = parameters;
	}

	@Override
	public double getDrift(double[] stateVariable, double time, int process) {
		return this.parameters.getDriftValue(stateVariable, time).getEntry(0);
	}

	@Override
	public double getDiffusion(double[] stateVariable, double time, int process, int riskFactor) {

		return this.parameters.getDiffusionValue(stateVariable, time).getEntry(0, 0);
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

		return 0;
	}

}
