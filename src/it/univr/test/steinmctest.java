package it.univr.test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;


public class steinmctest {

	public static void main(String[] args) {

		int numberOfTimeSteps = 100;
		int numberOfPaths = 1000000;
		
		double deltaT = 1.0/numberOfTimeSteps;
		
		double s = 100;
		double v = 0.15;
		double r = 0.04;
		double sigma = 0.2 ;
		double k =2;
		double theta =0.4;
		double rho = -0.7;
		double K = 120;
		
		
		Random x = new Random();

		double[] sSpace = new double[numberOfTimeSteps+1];
		double[] vSpace = new double[numberOfTimeSteps+1];
		double[] results = new double[numberOfPaths];
		sSpace[0] = s;
		vSpace[0] = v;
		
		long start = System.currentTimeMillis(); 
		
		for(int i = 1; i < numberOfPaths; i++) {

			for(int j = 1; j <= numberOfTimeSteps; j++) {
				
				double risk1 = x.nextGaussian();
				double risk2 = x.nextGaussian();
				
				vSpace[j] = vSpace[j-1] + k*(theta - vSpace[j-1])*deltaT + sigma*Math.sqrt(deltaT)*risk1;
				
				sSpace[j] = sSpace[j-1] + sSpace[j-1]*r*deltaT + sSpace[j-1]*vSpace[j-1]*risk1*Math.sqrt(deltaT)*rho + 
						sSpace[j-1]*vSpace[j-1]*risk2*Math.sqrt(deltaT)*Math.sqrt(1-Math.pow(rho, 2));

			}
			
		results[i-1] = Math.exp(-r)*Math.max( - sSpace[numberOfTimeSteps] + K,0);
			
		}

		

		double sum = 0;
	for(int n = 0; n < numberOfPaths -1; n++) {
		
		sum = sum + results[n];
	}

	double stdev = Math.sqrt(StatUtils.populationVariance(results));
	System.out.println("Distribution Successfully computed " + (double)(System.currentTimeMillis() - start)/1000);
	System.out.println(sum/numberOfPaths + " " + (sum/numberOfPaths - (stdev/Math.sqrt(0.05*numberOfPaths))) + " "  +
			 (sum/numberOfPaths + (stdev/Math.sqrt(0.05*numberOfPaths) ) ));
}
	
}
