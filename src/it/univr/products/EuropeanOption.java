package it.univr.products;

import java.util.Map;

import it.univr.quantizedprocess.QuantizedModel;

public class EuropeanOption {

	public double strike;
	public char c;
	
	public EuropeanOption(double strike, char c) {
		
		this.c = c;
		this.strike = strike;
	}
	
	public double evaluate(QuantizedModel quantizedModel) {
		
		
		double value = 0;
		
		if(c == 'c') {
			for(Map.Entry<double[], Double> i: quantizedModel.getProductQuantizerDistribution(quantizedModel.getNumberOfTimeSteps()).entrySet()) {
				
				value = value + //Math.exp(-//quantizedModel.getDeltaT()*quantizedModel.getNumberOfTimeSteps()*
										//	quantizedModel.getRiskFreeRate())*
						(Math.max(i.getKey()[0] - this.strike,0))*i.getValue();
		}
			return  Math.exp(-quantizedModel.getRiskFreeRate())*value;
		
		}
		
		if(c == 'p') {
			for(Map.Entry<double[], Double> i: quantizedModel.getProductQuantizerDistribution(quantizedModel.getNumberOfTimeSteps()).entrySet()) {
				
				value = value + Math.exp(-quantizedModel.getDeltaT()*quantizedModel.getNumberOfTimeSteps()*
											quantizedModel.getRiskFreeRate())*
						(Math.max(this.strike - i.getKey()[0],0))*i.getValue();
		}
			return value;
		
	}
		
		return 0;
}
	
}
