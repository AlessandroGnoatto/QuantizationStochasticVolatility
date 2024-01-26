package it.univr.products;

import java.util.Map;

import it.univr.quantizedprocess.BackwardIteration;
import it.univr.quantizedprocess.QuantizedModel;

public class AmericanOption {


	public double strike;
	
	public AmericanOption(double strike) {
		this.strike = strike;
	}
	
	public double evaluate(QuantizedModel quantizedModel) {
		//Create an empty backward iteration grid
		BackwardIteration parse = new BackwardIteration(quantizedModel);
		
		/*
		 * Initialize the backward iteration by means of the payoff
		 */
		for(Map.Entry<double[], Double> element : quantizedModel.getDistributionOf(quantizedModel.getNumberOfTimeSteps()).entrySet()) {
			
			double value = Math.max(element.getKey()[0] - this.strike,0);
			parse.replace(quantizedModel.getNumberOfTimeSteps(), element.getKey(), value);
		}
		
		double riskFree = quantizedModel.getRiskFreeRate();
		double deltaT = quantizedModel.getDeltaT();
		
		//Backward induction step
		for(int i = quantizedModel.getNumberOfTimeSteps(); i >=  1; i--) {
			
			for(Map.Entry<double[], Double> quantizerk : quantizedModel.getDistributionOf(i-1).entrySet()) {
				
				double payoff = 0;
				if(i == 1) {
					double initialValue = 0;
					
					for(Map.Entry<double[], Double> element : quantizedModel.getDistributionOf(1).entrySet()) {
						initialValue = initialValue + parse.getValue(1, element.getKey())*
								quantizedModel.getTransitionProbabilityLattice().getTransitionProbability(0, quantizerk.getKey(), element.getKey());
					}
					
					return initialValue*Math.exp((-deltaT)*riskFree);
				}
					
				for(Map.Entry<double[], Double> quantizerk1 : quantizedModel.getDistributionOf(i).entrySet()) {
					payoff = payoff + parse.getValue(i, quantizerk1.getKey())
							*quantizedModel.getTransitionProbabilityLattice().getTransitionProbability(i-1, quantizerk.getKey(), quantizerk1.getKey());
					
				}
				
				payoff = Math.max(payoff*Math.exp((-deltaT)*riskFree), Math.max(quantizerk.getKey()[0] - this.strike,0));
				parse.setValue(i-1, quantizerk.getKey(), payoff);
			}	
			
		}
		
		return 1.0;
	}

}
