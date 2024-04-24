package it.univr.test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;


public class cevmctest {

	public static void main(String[] args) {

		int numberOfTimeSteps = 100;
		int numberOfPaths = 1000000;
		
		double deltaT = 1.0/numberOfTimeSteps;
		
		double s = 100;
		double r = 0.04;
		double theta = 2.0;
		double delta =  0.7;
		double K = 120;
		
		
		Random x = new Random();
		double[] results = new double[numberOfPaths];
		double[] sSpace = new double[numberOfTimeSteps+1];
		sSpace[0] = s;
		
		long start = System.currentTimeMillis(); 
		
		for(int i = 1; i < numberOfPaths; i++) {
			
			for(int j = 1; j <= numberOfTimeSteps; j++) {
			
				
				sSpace[j] = sSpace[j-1] + r*sSpace[j-1]*deltaT + Math.sqrt(deltaT)*theta*(Math.pow(sSpace[j-1], 1 + delta)/
						Math.sqrt(1+Math.pow(sSpace[j-1], 2)))*x.nextGaussian();
				
			}
			
		results[i-1] = Math.exp(-r)*Math.max(-sSpace[numberOfTimeSteps] + K,0);
			
		}

		

		double sum = 0;
	for(int n = 0; n < numberOfPaths -1; n++) {
		
		sum = sum + results[n];
	}

	double stdev = Math.sqrt(StatUtils.populationVariance(results));
	//System.out.println("Distribution Successfully computed " + (double)(System.currentTimeMillis() - start)/1000);
	System.out.println(sum/numberOfPaths + " " + (sum/numberOfPaths - (stdev/Math.sqrt(0.05*numberOfPaths))) + " "  +
			 (sum/numberOfPaths + (stdev/Math.sqrt(0.05*numberOfPaths) ) ));
	

}
	
	
	
	
	
}
