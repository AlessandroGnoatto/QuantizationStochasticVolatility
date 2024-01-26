package it.univr.quantizedprocess;

import java.util.HashMap;

public class TransitionProbabilityLattice {

	
	public HashMap<Integer,TransitionProbability> transitionLattice = new HashMap<Integer,TransitionProbability>();
	
	
	public TransitionProbabilityLattice() {
		
		
	}
	
	public void setTransitionProbability(int timeStep, double[] conditionalQuantizer, double[] quantizer, double probability) {
		
		if(this.transitionLattice.get(timeStep) == null) {
			this.transitionLattice.put(timeStep, new TransitionProbability());
		}
		
		this.transitionLattice.get(timeStep).setTransitionProbability(conditionalQuantizer, quantizer, probability);
		
	}
	
	public double getTransitionProbability(int timeStep, double[] conditionalQuantizer, double[] quantizer) {
		
		return this.transitionLattice.get(timeStep).getTransitionProbability(conditionalQuantizer, quantizer);
	}
}
