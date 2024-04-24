package it.univr.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.AlphaHypergeometricModel;
import it.univr.model.DoubleHestonModel;
import it.univr.model.Model32;
import it.univr.model.parameters.AlphaHypergeometricParameterFunction;
import it.univr.model.parameters.DoubleHestonParameterFunction;
import it.univr.model.parameters.Model32ParameterFunction;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.AmericanOption;
import it.univr.products.EuropeanOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;

public class TestClass10 {

	public static void main(String[] args) throws InvalidFormatException, IOException {
		DoubleHestonParameterFunction a = new DoubleHestonParameterFunction(0.04,2.2,2.3,0.03,0.05,0.2,0.3,-0.9,-0.8);
    	
		DoubleHestonModel x = new DoubleHestonModel(a);
	    
	    ProductQuantizer t =
				  ProductQuantizer.buildWithCostantValues(asList(100.0,0.02,0.01), asList(20,5,5));
	    ModelQuantizer z = new ModelQuantizer(x,10,1.0/10,t);
	    long start = System.currentTimeMillis();
		  QuantizedModel w = z.quantize();
		  System.out.println("Distribution Successfully computed " + (double)(System.currentTimeMillis() - start)/1000);
		  for(int k = 80; k <= 120; k= k+5) { 
			  
		double d =0; 
		double c = 0;
		  
		  for(Map.Entry<double[], Double> element : w.getProductQuantizerDistribution(10).entrySet()) {
		  
		  if(k <= 100) { 
			  d = d + Math.max(element.getKey()[0] - k,0)*element.getValue();
			  }

		  
		  if(k >= 100) { 
			  c = c + Math.max( k - element.getKey()[0],0)*element.getValue(); } 
		  
		  }
		  
		  if(k <=100) {
			  System.out.println(d*Math.exp(-0.04)); 
		  
		  }
		  if(k>=100) {
			  System.out.println(c*Math.exp(-0.04));
		  }
		  
		}
		 // System.out.println(new AmericanOption(100.0).evaluate(w, 'c'));
		  

		  for(Map.Entry<Double, Double> element : w.getMarginalDistributionOf(1).entrySet()) {
			  
			  System.out.println(element.getKey() + " " + element.getValue());
		  }
	}

}
