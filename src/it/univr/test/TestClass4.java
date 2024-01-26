package it.univr.test;

import static java.util.Arrays.asList;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.BlackScholesModel;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.AmericanOption;
import it.univr.products.EuropeanOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;


public class TestClass4 {

	public static void main(String[] args) throws InvalidFormatException, IOException {
		
		double strike = 100;
		BlackScholesModel model = new BlackScholesModel(0.04,0.25);

		ProductQuantizer normalGrid =
				ProductQuantizer.buildWithCostantValues(asList(100.0), asList(50));
		
		int numberOfTimeSteps = 50;
		double maturity = 1.0;
		double deltaT = maturity / numberOfTimeSteps;
		ModelQuantizer modelQuantizer = new ModelQuantizer(model,numberOfTimeSteps,deltaT,normalGrid);

		long startTime = System.currentTimeMillis();
		QuantizedModel quantizedModel = modelQuantizer.run();
		long endTime = System.currentTimeMillis();
		long time = endTime-startTime;
		System.out.println("The computation of the quantization grid required  "+time +" milliseconds.");


		EuropeanOption europeanOption = new EuropeanOption(strike,'c');
		
		
		System.out.println(europeanOption.evaluate(quantizedModel));

		AmericanOption americanOption = new AmericanOption(strike);
		
		System.out.println("American Option price as benchmark - Exercise is not optimal");
		System.out.println(americanOption.evaluate(quantizedModel));
		
		  
	}

}
