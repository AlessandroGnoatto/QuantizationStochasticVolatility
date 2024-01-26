package it.univr.quantizedprocess;

import java.util.HashMap;

public class BackwardIterationStepControl {

	public HashMap<double[],double[]> backwardStep = new HashMap<double[], double[]>();
	
	
	public BackwardIterationStepControl() {
		
	}
	
	public void put(double[] quantizer, double[] value) {
		backwardStep.put(quantizer, value);
	}
	
	public double[] getValue(double[] quantizer) {
		return this.backwardStep.get(quantizer);
	}
	
	public void replace(double[] quantizer, double[] value) {
		backwardStep.replace(quantizer, value);
	}
}
