package it.univr.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.VasicekModel;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.ZeroCouponBondOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;

public class TestClass8 {

	public static void main(String[] args) throws InvalidFormatException, IOException {

		VasicekModel l = new VasicekModel(0.5,0.3,1.5);

		ProductQuantizer y = ProductQuantizer.buildWithCostantValues(asList(0.0), asList(100));
		
		ModelQuantizer u = new ModelQuantizer(l,40,2.0/40,y);
		
		
		
		long start = System.currentTimeMillis(); 
		QuantizedModel p = u.quantize();
		System.out.println("Distribution Successfully computed " + (double)(System.currentTimeMillis() - start)/1000);


		
		ZeroCouponBondOption a = new ZeroCouponBondOption(1.0,2.0,0.8);
		
		System.out.println(p.getProductQuantizerDistribution(1).entrySet().size());
		
		
		
		
	}
	
	
}
