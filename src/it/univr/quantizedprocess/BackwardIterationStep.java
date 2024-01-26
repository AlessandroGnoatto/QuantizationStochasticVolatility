package it.univr.quantizedprocess;

import java.util.HashMap;

public class BackwardIterationStep {

	public HashMap<double[],Double> backwardStep = new HashMap<double[], Double>();
	
	
	public BackwardIterationStep() {
		
	}
	
	public void put(double[] quantizer, double value) {
		backwardStep.put(quantizer, value);
	}
	
	public double getValue(double[] quantizer) {
		return this.backwardStep.get(quantizer);
	}
	
	public void replace(double[] quantizer, double value) {
		backwardStep.replace(quantizer, value);
	}
}
