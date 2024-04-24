package it.univr.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.BlackScholesModel;
import it.univr.model.parameters.BlackScholesParameterFunction;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.AmericanOption;
import it.univr.products.AmericanOption2;
import it.univr.products.EuropeanOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;
import net.finmath.functions.AnalyticFormulas;

public class TestClass4 {

	public static void main(String[] args) throws InvalidFormatException, IOException {

		BlackScholesModel l = new BlackScholesModel(0.04,0.3);
		
		boolean americanonly = false;
		boolean americaeuropa = true;
		

		//System.out.println("True Price " + AnalyticFormulas.blackScholesOptionValue(100.0, 0.04, 0.3, 1, 100,true));

		if(americaeuropa == true) {
			
		ProductQuantizer q = ProductQuantizer.buildWithCostantValues(asList(100.0), asList(30));

		ModelQuantizer v = new ModelQuantizer(l,10,1.0/10,q);
		long start = System.currentTimeMillis(); 
		QuantizedModel a = v.quantize();
		//System.out.println("Distribution Successfully computed " + (double)(System.currentTimeMillis() - start)/1000);

			for(int i = 80; i <= 120; i=i+5) {
			
			
			if(i <= 100) {
				double h = AnalyticFormulas.blackScholesOptionValue(100.0, 0.04, 0.3, 1, i,true);
				double c = new EuropeanOption(i,'c').evaluate(a);
				System.out.println(h + " " + c + " " + 100*Math.abs(h-c)/h);
				
			}
			if(i >= 100) {
				double d = AnalyticFormulas.blackScholesOptionValue(100.0, 0.04, 0.3, 1, i,false);
				double f = new EuropeanOption(i,'p').evaluate(a);
				System.out.println(d + " " + f + " " + 100*Math.abs(d-f)/d);
				}
			}
			
		}
		

		if(americanonly == true) {
			
		ProductQuantizer q = ProductQuantizer.buildWithCostantValues(asList(100.0), asList(30));
		ProductQuantizer w = ProductQuantizer.buildWithCostantValues(asList(100.0), asList(50));
		ProductQuantizer e = ProductQuantizer.buildWithCostantValues(asList(100.0), asList(100));
		ProductQuantizer r = ProductQuantizer.buildWithCostantValues(asList(100.0), asList(300));
		ProductQuantizer t = ProductQuantizer.buildWithCostantValues(asList(100.0), asList(500));
		  
		ModelQuantizer a = new ModelQuantizer(l,20,1.0/20,q);
		ModelQuantizer s = new ModelQuantizer(l,20,1.0/20,w);
		ModelQuantizer d = new ModelQuantizer(l,20,1.0/20,e);
		ModelQuantizer f = new ModelQuantizer(l,20,1.0/20,r);
		ModelQuantizer g = new ModelQuantizer(l,20,1.0/20,t);
		  
		long start1 = System.currentTimeMillis(); 
		QuantizedModel z = a.quantize();
		System.out.println("30 time " + (double)(System.currentTimeMillis() - start1)/1000);
		long start2 = System.currentTimeMillis(); 
		QuantizedModel x = s.quantize();
		System.out.println("50 time " + (double)(System.currentTimeMillis() - start2)/1000);
		long start3 = System.currentTimeMillis(); 
		QuantizedModel c = d.quantize();
		System.out.println("100 time " + (double)(System.currentTimeMillis() - start3)/1000);
		long start4 = System.currentTimeMillis(); 
		QuantizedModel v = f.quantize(); 
		System.out.println("300 time " + (double)(System.currentTimeMillis() - start4)/1000);
		long start5 = System.currentTimeMillis(); 
		QuantizedModel b = g.quantize();
		System.out.println("500 time " + (double)(System.currentTimeMillis() - start5)/1000);
		
		  
		System.out.println("American");
		System.out.println(new AmericanOption(100.0).evaluate(z,'c'));
		System.out.println(new AmericanOption(100.0).evaluate(x,'c'));
		System.out.println(new AmericanOption(100.0).evaluate(c,'c'));
		System.out.println(new AmericanOption(100.0).evaluate(v,'c'));
		System.out.println(new AmericanOption(100.0).evaluate(b,'c'));
		
		
		System.out.println("European");
		//System.out.println(new EuropeanOptionx(90.0,'c').evaluate(z));
		//System.out.println(new EuropeanOptionx(90.0,'c').evaluate(x));
		//System.out.println(new EuropeanOptionx(90.0,'c').evaluate(c));
		//System.out.println(new EuropeanOptionx(90.0,'c').evaluate(v));
		//System.out.println(new EuropeanOptionx(90.0,'c').evaluate(b));
		 
		}
 }
		  
}


