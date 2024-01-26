package it.univr.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.CorrelatedBlackScholesModel;
import it.univr.productquantizer.ProductQuantizer;
import it.univr.products.AmericanBasketOption;
import it.univr.products.EuropeanBasketOption;
import it.univr.quantizedprocess.ModelQuantizer;
import it.univr.quantizedprocess.QuantizedModel;
import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionLazyInit;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

public class TestClass3 {

	public static void main(String[] args) throws InvalidFormatException, IOException {

	
		  
		  CorrelatedBlackScholesModel x = new CorrelatedBlackScholesModel(0.04,0.3,0.2,0.7);
		  
		  
		  ProductQuantizer t = ProductQuantizer.buildWithCostantValues(asList(100.0,100.0), asList(20,20));
		  
		  ModelQuantizer z = new ModelQuantizer(x,10,1.0/10,t);
		  
		  long startTime = System.currentTimeMillis();
		  QuantizedModel w = z.run();
		  long endTime = System.currentTimeMillis();
		  long time = endTime-startTime;
		  System.out.println("Quantization grid computed in "+time +" milliseconds.");
		  
		  
		  
		  double d =0; 
		  
		  startTime = System.currentTimeMillis();
		  for(Map.Entry<double[], Double> element : z.quantizedProcess.getDistributionOf(10).entrySet()) {

			  d = d + Math.max(100.0 - 0.6*element.getKey()[0] - 0.4*element.getKey()[1],0)*element.getValue(); //d = d + element.getValue(); 
		  }
		  System.out.println(d*Math.exp(-0.04));
		  endTime = System.currentTimeMillis();
		  time = endTime-startTime;
		  System.out.println("Quantization grid computed in "+time +" milliseconds.");
		  
		  d =0; 
		  
		  for(Map.Entry<double[], Double> element : z.quantizedProcess.getDistributionOf(10).entrySet()) {

			  d = d + Math.max(105.0 - 0.6*element.getKey()[0] - 0.4*element.getKey()[1],0)*element.getValue(); //d = d + element.getValue(); 
		  }
		  System.out.println(d*Math.exp(-0.04));
		  
		  d =0; 
		  
		  for(Map.Entry<double[], Double> element : z.quantizedProcess.getDistributionOf(10).entrySet()) {

			  d = d + Math.max(110.0 - 0.6*element.getKey()[0] - 0.4*element.getKey()[1],0)*element.getValue(); //d = d + element.getValue(); 
		  }
		  System.out.println(d*Math.exp(-0.04));
		 
		  d =0; 
		  
		  for(Map.Entry<double[], Double> element : z.quantizedProcess.getDistributionOf(10).entrySet()) {

			  d = d + Math.max(115.0 - 0.6*element.getKey()[0] - 0.4*element.getKey()[1],0)*element.getValue(); //d = d + element.getValue(); 
		  }
		  System.out.println(d*Math.exp(-0.04));
		  
		  d =0; 
		  
		  for(Map.Entry<double[], Double> element : z.quantizedProcess.getDistributionOf(10).entrySet()) {

			  d = d + Math.max(120.0 - 0.6*element.getKey()[0] - 0.4*element.getKey()[1],0)*element.getValue(); //d = d + element.getValue(); 
		  }
		  System.out.println(d*Math.exp(-0.04));
		 
		  startTime = System.currentTimeMillis();
		  TimeDiscretization td = new TimeDiscretizationFromArray(0.0, 10, 0.1);
		  BrownianMotion bm = new BrownianMotionLazyInit(td, 2, 10000000, 3213);   
			
		  RandomVariable myIncr = bm.getBrownianIncrement(9,0);
		  endTime = System.currentTimeMillis();
		  time = endTime-startTime;
		  System.out.println("Generation of the Brownian motion completed in "+time +" milliseconds.");
			  
		 
		
		AmericanBasketOption q1 = new AmericanBasketOption(100.0,0.6,0.4);
		AmericanBasketOption q2 = new AmericanBasketOption(105.0,0.6,0.4);
		AmericanBasketOption q3 = new AmericanBasketOption(110.0,0.6,0.4);
		AmericanBasketOption q4 = new AmericanBasketOption(115.0,0.6,0.4);
		AmericanBasketOption q5 = new AmericanBasketOption(120.0,0.6,0.4);
		
		
		//EuropeanBasketOption q2 = new EuropeanBasketOption(100.0,0.5,0.5);
		startTime = System.currentTimeMillis();
		System.out.println(q1.evaluate(w));
		endTime = System.currentTimeMillis();
		time = endTime-startTime;
		System.out.println("Backward recursion computed in "+time +" milliseconds.");
			
		startTime = System.currentTimeMillis();
		System.out.println(q2.evaluate(w));
		endTime = System.currentTimeMillis();
		time = endTime-startTime;
		System.out.println("Backward recursion computed in "+time +" milliseconds.");
		
		startTime = System.currentTimeMillis();
		System.out.println(q3.evaluate(w));
		endTime = System.currentTimeMillis();
		time = endTime-startTime;
		System.out.println("Backward recursion computed in "+time +" milliseconds.");
		
		startTime = System.currentTimeMillis();
		System.out.println(q4.evaluate(w));
		endTime = System.currentTimeMillis();
		time = endTime-startTime;
		System.out.println("Backward recursion computed in "+time +" milliseconds.");
		
		
		startTime = System.currentTimeMillis();
		System.out.println(q5.evaluate(w));
		endTime = System.currentTimeMillis();
		time = endTime-startTime;
		System.out.println("Backward recursion computed in "+time +" milliseconds.");
		
	
	}
}









