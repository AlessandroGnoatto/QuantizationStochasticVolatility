package it.univr.test;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import it.univr.model.HestonModelx;
import it.univr.model.SABRModel;
import it.univr.model.parameters.HestonParameterFunction;
import it.univr.model.parameters.SABRParameterFunction;
import it.univr.optimalgrid.NewtonRaphsonMethod;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.productquantizer.TwoDimProductQuantizer;
import it.univr.products.EuropeanOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;

public class TestClass1 {

    
    public static void main(String args[]) throws InvalidFormatException, IOException {
    	
    	
    SABRParameterFunction a = new SABRParameterFunction(0.8,1.25,-0.85);
    	
    SABRModel x = new SABRModel(a);
    
    ProductQuantizer t =
			  ProductQuantizer.buildWithCostantValues(asList(100.0,0.06), asList(20,10));
   
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
