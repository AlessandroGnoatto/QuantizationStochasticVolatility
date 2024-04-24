package it.univr.test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;


public class cbsmctest {

	public static void main(String[] args) {

		int numberOfTimeSteps = 100;
		int numberOfPaths = 1000000;
		
		double deltaT = 1.0/numberOfTimeSteps;
		
		double s1 = 100;
		double s2 = 100;
		double r = 0.04;
		double sigma1 = 0.2;
		double sigma2 = 0.4;
		double rho = 0.7;
		double K = 100;
		
		
		Random x = new Random();
		double[] results = new double[numberOfPaths];
		double[] sSpace1 = new double[numberOfTimeSteps+1];
		double[] sSpace2 = new double[numberOfTimeSteps+1];
		sSpace1[0] = s1;
		sSpace2[0] = s2;
		
		long start = System.currentTimeMillis(); 
		
		for(int i = 1; i < numberOfPaths; i++) {

			for(int j = 1; j <= numberOfTimeSteps; j++) {
				
				double risk1 = x.nextGaussian();
				double risk2 = x.nextGaussian();
				
				sSpace1[j] = sSpace1[j-1] + sSpace1[j]*r*deltaT + sSpace1[j-1]*rho*sigma1*risk1*Math.sqrt(deltaT) + 
						sSpace1[j-1]*Math.sqrt(1- Math.pow(rho,2))*Math.sqrt(deltaT)*risk2*sigma1;
				
				sSpace2[j] = sSpace2[j-1] + sSpace2[j-1]*r*deltaT + sSpace2[j-1]*sigma2*risk1*Math.sqrt(deltaT);

			}
			
		results[i-1] = Math.exp(-r)*Math.max(-sSpace1[numberOfTimeSteps]*0.6 - 
				            sSpace2[numberOfTimeSteps]*0.4 + K,0);

			
		}


		double sum = 0;
	for(int n = 0; n < numberOfPaths -1; n++) {
		
		sum = sum + results[n];
	}

	//System.out.println("Distribution Successfully computed " + (double)(System.currentTimeMillis() - start)/1000);
	
	double stdev = Math.sqrt(StatUtils.populationVariance(results));
	
	System.out.println(sum/numberOfPaths + " " + (sum/numberOfPaths - (stdev/Math.sqrt(0.05*numberOfPaths))) + " "  +
			 (sum/numberOfPaths + (stdev/Math.sqrt(0.05*numberOfPaths) ) ));
}
	
}
