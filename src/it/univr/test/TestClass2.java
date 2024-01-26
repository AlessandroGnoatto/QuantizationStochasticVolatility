package it.univr.test;

import java.io.IOException;

import java.util.Map;



import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.HestonModel;
import it.univr.model.parameters.HestonParameterFunction;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.EuropeanOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;

import static java.util.Arrays.asList;
public class TestClass2 {

	public static void main(String[] args) throws InvalidFormatException, IOException {

		HestonParameterFunction parameter = new HestonParameterFunction(0.04,2.3924,0.0929,0.6903,-0.82); 
		
		HestonModel x = new HestonModel(parameter);

		ProductQuantizer t = ProductQuantizer.buildWithCostantValues(asList(Math.log(100.0),0.0719), asList(20,10));
		

		ModelQuantizer z = new ModelQuantizer(x,4,1.0/4,t);
		
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
