package it.univr.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.BlackScholesModel;
import it.univr.model.PseudoCEVModel;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.AmericanOption;
import it.univr.products.EuropeanOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;

public class TestClass7 {

	public static void main(String[] args) throws InvalidFormatException, IOException {

		PseudoCEVModel x = new PseudoCEVModel(0.04,2.0,0.7);
		
		ProductQuantizer t =
				  ProductQuantizer.buildWithCostantValues(asList(100.0), asList(7));
				  
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
				  System.out.println(Math.exp(-0.04)*d); 
			  
			  }
			  if(k>=100) {
				  System.out.println(Math.exp(-0.04)*c);
			  }
			  
	  
  
}
	  

		  
	}
		

	}


