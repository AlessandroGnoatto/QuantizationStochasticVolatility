package it.univr.test;

import static java.util.Arrays.asList;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.AlphaHypergeometricModel;
import it.univr.model.SteinSteinModel;
import it.univr.model.parameters.AlphaHypergeometricParameterFunction;
import it.univr.model.parameters.SteinSteinParameterFunction;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.EuropeanOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;

public class TestClass6 {

	public static void main(String[] args) throws InvalidFormatException, IOException {
		SteinSteinParameterFunction a = new SteinSteinParameterFunction(0.04,0.05,3,0.3,-0.5);
    	
		SteinSteinModel x = new SteinSteinModel(a);
	    
	    ProductQuantizer t =
				  ProductQuantizer.buildWithCostantValues(asList(100.0,0.25), asList(20,10));
	    
	    ModelQuantizer z = new ModelQuantizer(x,10,1.0/10,t);
		  
		  QuantizedModel w = z.run();

		  
		  for(int i = 60; i <=140; i = i+5) {
			  
			  if(i <= 100) {
			  System.out.println(new EuropeanOption(i,'c').evaluate(w));
			  }
			  if(i > 100) {
				  System.out.println(new EuropeanOption(i,'p').evaluate(w));
				  }
			  
		  }

	}

}
