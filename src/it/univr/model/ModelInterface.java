package it.univr.model;

import org.apache.commons.math3.linear.RealVector;

import it.univr.model.parameters.ParameterFunctionInterface;

public interface ModelInterface {
	/**
	 * Returns the drift of the Ito SDE.
	 * @param stateVariable
	 * @param time
	 * @param process
	 * @return
	 */
	public double getDrift(double[] stateVariable, double time, int process);
	
	/**
	 * Returns the diffusion of the Ito SDE.
	 * @param stateVariable
	 * @param time
	 * @param process
	 * @param riskFactor
	 * @return
	 */
	public double getDiffusion(double[] stateVariable, double time, int process, int riskFactor);
	
	/**
	 * Returns an array with the iffusion coefficients for a sub-process $\ell$  of the multi-dimensional process.
	 * This method is useful for the computation of transition probabilities in the multi-dimensional case.
	 * @param stateVariable
	 * @param time
	 * @param process
	 * @return
	 */
	public RealVector getDiffusionRow(double[] stateVariable, double time, int process);
	 
	public int getNumberOfProcess();
	
	public int getNumberOfRiskFactors();

	public double getRiskFreeRate();
	
	public ParameterFunctionInterface getParameters();
}
