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
		BackwardIteration parse = new BackwardIteration(quantizedModel);
		
		for(Map.Entry<double[], Double> element : quantizedModel.getProductQuantizerDistribution(quantizedModel.getNumberOfTimeSteps()).entrySet()) {
			
			double value = Math.max(weights[0]*element.getKey()[0] + weights[1]*element.getKey()[1] - this.strike,0);
			parse.replace(quantizedModel.getNumberOfTimeSteps(), element.getKey(), value);
		}
		
		
		
		for(int i = quantizedModel.getNumberOfTimeSteps(); i >=  1; i--) {
			
			for(Map.Entry<double[], Double> quantizerk : quantizedModel.getProductQuantizerDistribution(i-1).entrySet()) {
				double payoff = 0;
				if(i == 1) {
					double finalValue = 0;
					for(Map.Entry<double[], Double> element : quantizedModel.getProductQuantizerDistribution(1).entrySet()) {
						finalValue = finalValue + parse.getValue(1, element.getKey())*
								quantizedModel.getTransitionProbability(0, quantizerk.getKey(), element.getKey());
					}
					return finalValue*Math.exp((-1.0/10)*0.04);
					}
					
				for(Map.Entry<double[], Double> quantizerk1 : quantizedModel.getProductQuantizerDistribution(i).entrySet()) {
					payoff = payoff + parse.getValue(i, quantizerk1.getKey())
							*quantizedModel.getTransitionProbability(i-1, quantizerk.getKey(), quantizerk1.getKey());
					
				}
				payoff = Math.max(payoff*Math.exp((-1.0/10)*0.04), Math.max(weights[0]*quantizerk.getKey()[0] + weights[1]*quantizerk.getKey()[1] - this.strike,0));
				parse.setValue(i-1, quantizerk.getKey(), payoff);
			}
			
			
		}
		
	
return 1.0;
	}

}

