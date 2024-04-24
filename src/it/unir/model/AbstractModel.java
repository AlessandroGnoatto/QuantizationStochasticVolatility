package it.univr.model;

import org.apache.commons.math3.linear.RealVector;


public abstract class AbstractModel {	
	
	public abstract double getRiskFreeRate();

	public abstract int getNumberOfProcess();

	public abstract int getNumberOfRiskFactors();

	public abstract double getDrift(double[] stateVariable, double time, int process);
	
	public abstract double getDiffusion(double[] stateVariable, double time, int process, int riskFactor);
	
	public abstract RealVector getDiffusionRow(double[] stateVariable, double time, int process);
	
}
