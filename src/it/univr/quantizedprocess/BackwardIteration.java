package it.univr.quantizedprocess;

import java.util.HashMap;
import java.util.Map;

public class BackwardIteration {
	
	HashMap<Integer, BackwardIterationStep> backWardLattice;
	
	public BackwardIteration(QuantizedModel quantizedModel) {
		
		this.backWardLattice = new HashMap<Integer, BackwardIterationStep>(quantizedModel.getNumberOfTimeSteps());
		
		for(Map.Entry<double[], Double> element : quantizedModel.getDistributionOf(quantizedModel.getNumberOfTimeSteps()).entrySet()) {
		this.setValue(quantizedModel.getNumberOfTimeSteps(), element.getKey(), 0);
		}
	}
	
	public void setValue(int step, double[] quantizer, double value) {
		
		if(this.backWardLattice.get(step) == null) {
			this.backWardLattice.put(step, new BackwardIterationStep());
		}
		
		this.backWardLattice.get(step).put(quantizer, value);
	}
	
	public double getValue(int step, double[] quantizer) {
		
		return this.backWardLattice.get(step).getValue(quantizer);
	}
	
	public void replace(int step, double[] quantizer, double value) {
		this.backWardLattice.get(step).replace(quantizer, value);
	}
}
