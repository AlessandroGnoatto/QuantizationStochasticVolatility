package it.univr.products;


import java.util.Map;

import it.univr.quantizedprocess.BackwardIteration;
import it.univr.quantizedprocess.QuantizedModel;

public class AmericanBasketOption {

	public double[] weights;
	public double strike;
	
	public AmericanBasketOption(double strike, double... weights) {
		
		this.weights = weights;
		this.strike = strike;
	}
	
	
	public double evaluate(QuantizedModel quantizedModel) {
		//Create an empty backward iteration grid
		BackwardIteration parse = new BackwardIteration(quantizedModel);
		
		/*
		 * Initialize the backward iteration by means of the payoff
		 */
		for(Map.Entry<double[], Double> element : quantizedModel.getDistributionOf(quantizedModel.getNumberOfTimeSteps()).entrySet()) {
			
			double value = Math.max(weights[0]*element.getKey()[0] + weights[1]*element.getKey()[1] - this.strike,0);
			parse.replace(quantizedModel.getNumberOfTimeSteps(), element.getKey(), value);
		}
		
		double riskFree = quantizedModel.getRiskFreeRate();
		double deltaT = quantizedModel.getDeltaT();
	
		//Backward induction step
		for(int i = quantizedModel.getNumberOfTimeSteps(); i >=  1; i--) {
			
			for(Map.Entry<double[], Double> quantizerk : quantizedModel.getDistributionOf(i-1).entrySet()) {
				double value = 0.0;
				double contnuationValue = 0;
				if(i == 1) {
					double finalValue = 0;
					for(Map.Entry<double[], Double> element : quantizedModel.getDistributionOf(1).entrySet()) {
						finalValue = finalValue + parse.getValue(1, element.getKey())*
								quantizedModel.getTransitionProbabilityLattice().getTransitionProbability(0, quantizerk.getKey(), element.getKey());
					}
					return finalValue*Math.exp(-deltaT*riskFree);
					}
					
				for(Map.Entry<double[], Double> quantizerk1 : quantizedModel.getDistributionOf(i).entrySet()) {
					contnuationValue = contnuationValue + parse.getValue(i, quantizerk1.getKey())
							*quantizedModel.getTransitionProbabilityLattice().getTransitionProbability(i-1, quantizerk.getKey(), quantizerk1.getKey());
					
				}
				value = Math.max(contnuationValue*Math.exp(-deltaT*riskFree), Math.max(weights[0]*quantizerk.getKey()[0] + weights[1]*quantizerk.getKey()[1] - this.strike,0));
				parse.setValue(i-1, quantizerk.getKey(), value);
			}
			
			
		}
		
	
return 1.0;
	}

}

