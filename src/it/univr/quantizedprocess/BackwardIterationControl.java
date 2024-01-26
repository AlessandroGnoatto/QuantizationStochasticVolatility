package it.univr.quantizedprocess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BackwardIterationControl {
	
	HashMap<Integer, BackwardIterationStepControl> backWardLattice;
	
	public BackwardIterationControl(QuantizedModel quantizedModel, int dimension) {
		
		this.backWardLattice = new HashMap<Integer, BackwardIterationStepControl>(quantizedModel.getNumberOfTimeSteps());
		
		
		
		for(Map.Entry<double[], Double> element : quantizedModel.getDistributionOf(quantizedModel.getNumberOfTimeSteps()).entrySet()) {
			double[] zero = new double[dimension];
			Arrays.fill(zero, 0.0);
			
			this.setValue(quantizedModel.getNumberOfTimeSteps(), element.getKey(), zero);
		}
	}
	
	public void setValue(int step, double[] quantizer, double[] value) {
		
		if(this.backWardLattice.get(step) == null) {
			this.backWardLattice.put(step, new BackwardIterationStepControl());
		}
		
		this.backWardLattice.get(step).put(quantizer, value);
	}
	
	public double[] getValue(int step, double[] quantizer) {
		
		return this.backWardLattice.get(step).getValue(quantizer);
	}
	
	public void replace(int step, double[] quantizer, double[] value) {
		this.backWardLattice.get(step).replace(quantizer, value);
	}
}

