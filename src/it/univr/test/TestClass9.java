package it.univr.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.AlphaHypergeometricModel;
import it.univr.model.Model32;
import it.univr.model.parameters.AlphaHypergeometricParameterFunction;
import it.univr.model.parameters.Model32ParameterFunction;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.AmericanOption;
import it.univr.products.EuropeanOption;
import it.univr.products.EuropeanOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;

public class TestClass9 {

	public static void main(String[] args) throws InvalidFormatException, IOException {
		Model32ParameterFunction a = new Model32ParameterFunction(0.04,8.85,0.075,2.56,-0.94);
    	
		Model32 x = new Model32(a);
	    
	    ProductQuantizer t =
				  ProductQuantizer.buildWithCostantValues(asList(100.0,0.0575), asList(25,12));
	    
	    ModelQuantizer z = new ModelQuantizer(x,20,1.0/20,t);
		  
	    long start = System.currentTimeMillis(); 
		  QuantizedModel w = z.quantize();
		  System.out.println("Distribution Successfully computed " + (double)(System.currentTimeMillis() - start)/1000);
		  
		  for(int k = 80; k <= 120; k= k+5) { 
			  
				double d =0; 
				double c = 0;
				  
				  for(Map.Entry<double[], Double> element : w.getProductQuantizerDistribution(20).entrySet()) {
				  
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

		  
		  System.out.println(new AmericanOption(90.0).evaluate(w, 'c'));

	}

}
