package it.univr.test;

import static java.util.Arrays.asList;

import java.io.IOException;

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

		PseudoCEVModel x = new PseudoCEVModel(0.15,4.0,0.5);
		
		ProductQuantizer t =
				  ProductQuantizer.buildWithCostantValues(asList(100.0), asList(300));
				  
		ModelQuantizer z = new ModelQuantizer(x,10,1.0/10,t);
		  
		  QuantizedModel w = z.run();
		  
		  
		  
		  EuropeanOption d = new EuropeanOption(100,'p');
		  
		  System.out.println(d.evaluate(w));
		

	}

}
