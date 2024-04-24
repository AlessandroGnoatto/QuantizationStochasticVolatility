package it.univr.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.HestonModelx;
import it.univr.model.parameters.HestonParameterFunction;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.AmericanOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;
import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionLazyInit;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.models.HestonModel;
import net.finmath.montecarlo.assetderivativevaluation.products.BermudanOption;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.montecarlo.process.EulerSchemeFromProcessModel;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

import static java.util.Arrays.asList;
public class TestClass2 {

	public static void main(String[] args) throws InvalidFormatException, IOException, CalculationException {

		
		int numberofstep = 10;
		int ssize = 20;
		int vsize = 10;
			
		int quant = 1;
		
		
		HestonParameterFunction parameter = new HestonParameterFunction(0.04,0.1269,0.1922,0.4058,-0.925); 
		HestonModelx x = new HestonModelx(parameter);

		ProductQuantizer t = ProductQuantizer.buildWithCostantValues(asList(100.0,0.0319), asList(ssize,vsize));

		if (quant == 1) {
		ModelQuantizer z = new ModelQuantizer(x,numberofstep,1.0/numberofstep,t);
	    long start = System.currentTimeMillis();
		QuantizedModel w = z.quantize();
		  System.out.println("Distribution Successfully computed " + (double)(System.currentTimeMillis() - start)/1000);
		
		  for(int k = 80; k <= 120; k= k+5) { 
			  
				  System.out.println(new AmericanOption(k).evaluate(w, 'p')); 
			  } 


	}


}
}
