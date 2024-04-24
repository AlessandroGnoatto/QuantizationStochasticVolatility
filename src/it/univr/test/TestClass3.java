package it.univr.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.CorrelatedBlackScholesModel;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.AmericanBasketOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;
import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloMultiAssetBlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.BasketOption;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

public class TestClass3 {

	public static void main(String[] args) throws InvalidFormatException, IOException, CalculationException {


		
		int numberOfTimeSteps = 10;
		 int quantizerSize = 10;
		 
		 
		 CorrelatedBlackScholesModel x = new CorrelatedBlackScholesModel(0.04,0.3,0.4,0.5);
		  
		  
		  ProductQuantizer t = ProductQuantizer.buildWithCostantValues(asList(100.0,100.0), asList(quantizerSize,quantizerSize));
		  
		  ModelQuantizer z = new ModelQuantizer(x,numberOfTimeSteps,1.0/numberOfTimeSteps,t);
		  
		  
		  long start = System.currentTimeMillis(); 
		  QuantizedModel w = z.quantize();
		  System.out.println("Distribution Successfully computed " + (double)(System.currentTimeMillis() - start)/1000);
		  

		  for(int k = 80; k <= 120; k= k+5) { 
			  
		double d =0; 
		double c = 0;
		  
		  for(Map.Entry<double[], Double> element : w.getProductQuantizerDistribution(numberOfTimeSteps).entrySet()) {
		  
		  if(k <= 100) { 
			  d = d + Math.max(0.5*element.getKey()[0] + 0.5*element.getKey()[1] - k,0)*element.getValue();
			  }

		  
		  if(k >= 100) { 
			  c = c + Math.max( k - 0.5*element.getKey()[0]-0.5*element.getKey()[1],0)*element.getValue(); } 
		  
		  }
		  
		  if(k <=100) {
			  System.out.println(d*Math.exp(-0.04)); 
		  
		  }
		  if(k>=100) {
			  System.out.println(c*Math.exp(-0.04));
		  }
		  
		  }
		  
		  for(double element: w.getProductQuantizerDistribution(8).values()) {
			  System.out.println(element);
		  }
		  
		  

		  
		  
	}
}









