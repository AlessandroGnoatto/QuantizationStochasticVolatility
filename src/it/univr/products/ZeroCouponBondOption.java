package it.univr.products;

import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import static java.util.Arrays.asList;

import java.io.IOException;

import it.univr.productquantizer.ProductQuantizer;
import it.univr.quantizedprocess.BackwardIteration;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;

public class ZeroCouponBondOption {

	private double optionMaturity;
	private double bondMaturity;
	private double strike;
	
	
	public ZeroCouponBondOption(double optionMaturity, double bondMaturity, double strike) {
		
		this.optionMaturity = optionMaturity;
		this.bondMaturity = bondMaturity;
		this.strike = strike;

	}
	
	
	public double evaluate(QuantizedModel quantizedModel) {
		
	BackwardIteration parse = new BackwardIteration(quantizedModel);
	int optMatInTimeStep = (int) (this.optionMaturity/quantizedModel.getDeltaT());
	int bondMatInTimeStep = (int) (this.bondMaturity/quantizedModel.getDeltaT());
	
	
	//Cambio payoff bond
	for(Map.Entry<double[], Double> element : quantizedModel.getProductQuantizerDistribution(bondMatInTimeStep).entrySet()) {
		
			double value = 1;
			parse.replace(bondMatInTimeStep, element.getKey(), value);
		}
		
	
	//Sconto bond
	for(int i = bondMatInTimeStep; i >=  optMatInTimeStep +1 ; i--) {
		
		for(Map.Entry<double[], Double> quantizerk : quantizedModel.getProductQuantizerDistribution(i-1).entrySet()) {
			double payoff = 0;
			
			if(i == optMatInTimeStep + 1) {
				double bondValue = 0;
				for(Map.Entry<double[], Double> element : quantizedModel.getProductQuantizerDistribution(optMatInTimeStep+1).entrySet()) {
					bondValue = bondValue + parse.getValue(i, element.getKey())*
							quantizedModel.getTransitionProbability(i-1, quantizerk.getKey(), element.getKey())*
							Math.exp(-quantizedModel.getDeltaT()*quantizerk.getKey()[0]);
				}
			parse.setValue(i-1, quantizerk.getKey(), Math.max(bondValue - this.strike, 0));
			//System.out.println( Math.max(bondValue - this.strike, 0)+" " + quantizerk.getValue());
				}
			
			
			if(i > optMatInTimeStep +1) {
			for(Map.Entry<double[], Double> quantizerk1 : quantizedModel.getProductQuantizerDistribution(i).entrySet()) {
				payoff = payoff + parse.getValue(i, quantizerk1.getKey())
						*quantizedModel.getTransitionProbability(i-1, quantizerk.getKey(), quantizerk1.getKey())
						*Math.exp(-quantizedModel.getDeltaT()*quantizerk.getKey()[0]);;
			}
			
			parse.setValue(i-1, quantizerk.getKey(), payoff);
			
		}
		
		}
	}

for(int i = optMatInTimeStep; i >= 1 ; i--) {

		for(Map.Entry<double[], Double> quantizerk : quantizedModel.getProductQuantizerDistribution(i-1).entrySet()) {
			double payoff = 0;
			
			if(i == 1) {
				double finalValue = 0;
				for(Map.Entry<double[], Double> element : quantizedModel.getProductQuantizerDistribution(1).entrySet()) {
					finalValue = finalValue + parse.getValue(i, element.getKey())*
							quantizedModel.getTransitionProbability(0, quantizerk.getKey(), element.getKey())*
							Math.exp(-quantizedModel.getDeltaT()*quantizerk.getKey()[0]);
				}
			return finalValue;
				}
			
			for(Map.Entry<double[], Double> quantizerk1 : quantizedModel.getProductQuantizerDistribution(i).entrySet()) {
				payoff = payoff + parse.getValue(i, quantizerk1.getKey())
						*quantizedModel.getTransitionProbability(i-1, quantizerk.getKey(), quantizerk1.getKey())
						*Math.exp(-quantizedModel.getDeltaT()*quantizerk.getKey()[0]);
			}
			
			parse.setValue(i-1, quantizerk.getKey(), payoff);
			
			}
		}
	
	
		return 1.0;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public double evaluate2(QuantizedModel quantizedModel) throws InvalidFormatException, IOException {
		
		
		double value = 1;
		int optMatInTimeStep = (int) (this.optionMaturity/quantizedModel.getDeltaT());
		int bondMatInTimeStep = (int) (this.bondMaturity/quantizedModel.getDeltaT());
		int differenceTime = bondMatInTimeStep - optMatInTimeStep;
		
		for(Map.Entry<double[], Double> element : quantizedModel.getProductQuantizerDistribution(optMatInTimeStep).entrySet()) {
	
		ModelQuantizer util = new ModelQuantizer(quantizedModel.getModel(),
												differenceTime,
												quantizedModel.getDeltaT(),
												ProductQuantizer.buildWithCostantValues(asList(element.getKey()[0]),
								asList(quantizedModel.getProductQuantizerDistribution(1).entrySet().size())));
		QuantizedModel util2 = util.quantize();
			
		BackwardIteration parse = new BackwardIteration(quantizedModel);
		
			//Cambio payoff bond
			for(Map.Entry<double[], Double> endTime : util2.getProductQuantizerDistribution(differenceTime).entrySet()) {
			
			parse.replace(differenceTime, endTime.getKey(), value);
			}
		
		//Sconto Bond
		for(int i = differenceTime; i > 0 ; i--) {
			
			for(Map.Entry<double[], Double> quantizerk : quantizedModel.getProductQuantizerDistribution(i-1).entrySet()) {
				double payoff = 0;

				if(i > 0) {
					for(Map.Entry<double[], Double> quantizerk1 : quantizedModel.getProductQuantizerDistribution(i).entrySet()) {
					payoff = payoff + parse.getValue(i, quantizerk1.getKey())
							*util2.getTransitionProbability(i-1, quantizerk.getKey(), quantizerk1.getKey())
							*Math.exp(-util2.getDeltaT()*quantizerk.getKey()[0]);;
					}
				
					parse.setValue(i-1, quantizerk.getKey(), payoff);
				
					}
		
				
			
			
			}
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return 1.0;
	}
	return 1.0;
	}
}
