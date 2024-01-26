package it.univr.test;
import static java.util.Arrays.asList;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import it.univr.model.SABRModel;
import it.univr.model.parameters.SABRParameterFunction;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.EuropeanOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;

public class TestClass1 {


	public static void main(String args[]) throws InvalidFormatException, IOException {


		SABRParameterFunction a = new SABRParameterFunction(0.224,0.75,-0.824);

		SABRModel x = new SABRModel(a);

		ProductQuantizer t =
				ProductQuantizer.buildWithCostantValues(asList(100.0,0.0719), asList(20,20));

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