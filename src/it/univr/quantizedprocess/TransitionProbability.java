package it.univr.quantizedprocess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TransitionProbability {

	private HashMap<List<double[]>,Double> transitionMap = new HashMap<List<double[]>,Double>();
	
	public TransitionProbability() {
		
	}
	
	public void setTransitionProbability(double[] conditionalQuantizer, double[] quantizer, double probability) {
		transitionMap.put(Arrays.asList(conditionalQuantizer,quantizer), probability);
		
	}
	
	public double getTransitionProbability(double[] conditionalQuantizer, double[] quantizer) {
		
		return this.transitionMap.get(Arrays.asList(conditionalQuantizer,quantizer));
	}
}
