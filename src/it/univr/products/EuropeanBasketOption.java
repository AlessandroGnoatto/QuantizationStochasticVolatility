package it.univr.products;

import java.util.Map;

import it.univr.quantizedprocess.QuantizedModel;

public class EuropeanBasketOption {

	public double[] weights;
	public double strike;
	
	public EuropeanBasketOption(double strike, double... weights) {
		
		this.weights = weights;
		this.strike = strike;
	}
	
	public double evaluate(QuantizedModel quantizedModel) {
		//Create an empty backward iteration grid
		double value = 0;
		/*
		 * Initialize the backward iteration by means of the payoff
		 */
		for(Map.Entry<double[], Double> element : quantizedModel.getDistributionOf(quantizedModel.getNumberOfTimeSteps()).entrySet()) {
			
			value = value +  Math.exp(-quantizedModel.getDeltaT()*quantizedModel.getNumberOfTimeSteps()*
					quantizedModel.getRiskFreeRate())*
					Math.max(weights[0]*element.getKey()[0] + weights[1]*element.getKey()[1] - this.strike,0);
		}
	
		
	
		return value;
	}

}
