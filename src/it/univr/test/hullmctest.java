package it.univr.test;

import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class hullmctest {

	public static void main(String[] args) {
		int numberOfTimeSteps = 20;
		int numberOfPaths = 100000;
		
		double deltaT = 1.0/numberOfTimeSteps;
		
		double r = 0.0;
		double a = 0.3;
		double sigma = 0.6;
		
		
		Random x = new Random();
		RealVector results = new ArrayRealVector();
		double[] rSpace = new double[numberOfTimeSteps+1];
		rSpace[0] = r;
		
		long start = System.currentTimeMillis(); 
		
		for(int i = 1; i < numberOfPaths; i++) {
			
			for(int j = 1; j <= numberOfTimeSteps; j++) {
				
				rSpace[j] = rSpace[j-1] + (a*0.01*Math.sqrt((j-1)*deltaT) + (Math.pow(sigma, 2)/(2*a))* (1-Math.exp(2*a*((j-1)*deltaT))) 
						- a* rSpace[j-1]) * deltaT + sigma * Math.sqrt(deltaT)*x.nextGaussian();
				
			}
			
		results = results.append(rSpace[numberOfTimeSteps]);
			//System.out.println(Math.max(sSpace[numberOfTimeSteps] - K,0));
			
		}

		

		double sum = 0;
	for(int n = 0; n < numberOfPaths -1; n++) {
		
		sum = sum + results.getEntry(n);
	}

	System.out.println("Distribution Successfully computed " + (double)(System.currentTimeMillis() - start)/1000);
	System.out.println(sum/numberOfPaths);


	}

}
