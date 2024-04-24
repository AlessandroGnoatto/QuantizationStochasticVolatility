package it.univr.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.AlphaHypergeometricModel;
import it.univr.model.DoubleHestonModel;
import it.univr.model.FXcurrencyModel;
import it.univr.model.Model32;
import it.univr.model.parameters.AlphaHypergeometricParameterFunction;
import it.univr.model.parameters.DoubleHestonParameterFunction;
import it.univr.model.parameters.FXcurrencyParameterFunction;
import it.univr.model.parameters.Model32ParameterFunction;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.AmericanOption;
import it.univr.products.CVA;
import it.univr.products.EuropeanOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;

public class TestClass11 {

	public static void main(String[] args) throws InvalidFormatException, IOException {
		
		double notional = 100000000;
		
		double spotFX = notional*1.1;
		double spotShortdom = 0.02;
		double spotShortfor = 0.04;
		
		int N1 = 10;
		int N2 = 7;
		int N3 = 7;
		
		int n = 10;
		
		double kdom = 0.8;
		double kfor = 0.9;
		double thetadom = 0.04;
		double thetafor = 0.02;
		double sigmafx = 0.2;
		double sigmadom = 0.15;
		double sigmafor = 0.3;
		double rhofxdom = 0.2;
		double rhofxfor = -0.3;
		double rhodomfor = 0.5;
		
		FXcurrencyParameterFunction a = new FXcurrencyParameterFunction(kdom,kfor,thetadom,thetafor,sigmafx,sigmadom,sigmafor,rhofxdom,rhofxfor,rhodomfor);;
    	
		FXcurrencyModel x = new FXcurrencyModel(a);
	    
	    ProductQuantizer t = ProductQuantizer.buildWithCostantValues(asList(spotFX,spotShortdom,spotShortfor), asList(N1,N2,N3));
	    
	    ModelQuantizer z = new ModelQuantizer(x,n,1.0/n,t);
	    long start = System.currentTimeMillis();
		  QuantizedModel w = z.quantize();
		  System.out.println("Distribution Successfully computed " + (double)(System.currentTimeMillis() - start)/1000);
	
	CVA ex = new CVA(kdom,kfor,thetadom,thetafor,sigmafx,sigmadom,sigmafor,rhofxdom,rhofxfor,rhodomfor,spotFX,spotShortdom,spotShortfor);

     System.out.println(ex.evalueate(w));
		  
		  
	}

	
}
