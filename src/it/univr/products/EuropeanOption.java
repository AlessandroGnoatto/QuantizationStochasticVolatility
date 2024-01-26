package it.univr.products;

import java.util.Map;

import it.univr.quantizedprocess.QuantizedModel;

public class EuropeanOption {

	private double strike;
	private char callOrPut;

	public EuropeanOption(double strike, char callOrPut) {

		this.callOrPut = callOrPut;
		this.strike = strike;
	}

	public double evaluate(QuantizedModel quantizedModel) {


		double value = 0;

		if(callOrPut == 'c') {
			for(Map.Entry<Double, Double> i: quantizedModel.getMarginalDistributionOf(1).entrySet()) {

				value = value + Math.exp(-quantizedModel.getDeltaT()*quantizedModel.getNumberOfTimeSteps()*
						quantizedModel.getRiskFreeRate())*
						(Math.max(i.getKey() - this.strike,0))*i.getValue();
			}
			return value;

		}

		if(callOrPut == 'p') {
			for(Map.Entry<Double, Double> i: quantizedModel.getMarginalDistributionOf(1).entrySet()) {

				value = value + Math.exp(-quantizedModel.getDeltaT()*quantizedModel.getNumberOfTimeSteps()*
						quantizedModel.getRiskFreeRate())*
						(Math.max(this.strike - i.getKey(),0))*i.getValue();
				System.out.println(i.getValue());
			}
			return value;

		}

		return 0;
	}

}
