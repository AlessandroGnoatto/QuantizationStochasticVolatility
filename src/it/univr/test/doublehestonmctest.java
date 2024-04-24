package it.univr.test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;


public class doublehestonmctest {

	public static void main(String[] args) {

		int numberOfTimeSteps = 100;
		int numberOfPaths = 1000000;
		
		double deltaT = 1.0/numberOfTimeSteps;
		
		double s = 100;
		double r = 0.04;
		double v1 = 0.02;
		double v2 = 0.01;
		double k1 = 2.2;
		double k2 = 2.3;
		double theta1 = 0.03;
		double theta2 = 0.05;
		double sigma1 = 0.2 ;
		double sigma2 = 0.3 ;
		double rho1 = -0.9;
		double rho2 = -0.8;
		double K = 95;
		
		
		Random x = new Random();

		double[] sSpace = new double[numberOfTimeSteps+1];
		double[] v1Space = new double[numberOfTimeSteps+1];
		double[] v2Space = new double[numberOfTimeSteps+1];
		double[] results = new double[numberOfPaths];
		sSpace[0] = s;
		v1Space[0] = v1;
		v2Space[0] = v2;
		
		long start = System.currentTimeMillis(); 
		
		for(int i = 1; i < numberOfPaths; i++) {

			for(int j = 1; j <= numberOfTimeSteps; j++) {
				
				double risk1 = x.nextGaussian();
				double risk2 = x.nextGaussian();
				double risk3 = x.nextGaussian();
				double risk4 = x.nextGaussian();
				
	sSpace[j] = sSpace[j-1] + (r*sSpace[j-1])* deltaT + sSpace[j-1]*Math.sqrt(Math.abs(v1Space[j-1]))*Math.sqrt(deltaT)*risk1*rho1
													  + sSpace[j-1]*Math.sqrt(Math.abs(v1Space[j-1]))*Math.sqrt(deltaT)*risk2*Math.sqrt(1-Math.pow(rho1,2))
													  + sSpace[j-1]*Math.sqrt(Math.abs(v2Space[j-1]))*Math.sqrt(deltaT)*risk3*rho2
													  + sSpace[j-1]*Math.sqrt(Math.abs(v2Space[j-1]))*Math.sqrt(deltaT)*risk4*Math.sqrt(1-Math.pow(rho2,2));
													  
				v1Space[j] = v1Space[j-1] + k1*(theta1 - Math.abs(v1Space[j-1]))* deltaT + 
						sigma1*Math.sqrt(Math.abs(v1Space[j-1]))*Math.sqrt(deltaT)*risk1;
				
				v2Space[j] = v2Space[j-1] + k2*(theta2 - Math.abs(v2Space[j-1]))* deltaT + 
						sigma2*Math.sqrt(Math.abs(v2Space[j-1]))*Math.sqrt(deltaT)*risk3;
			}
			
		results[i-1] = Math.exp(-r)*Math.max(sSpace[numberOfTimeSteps] - K,0);
			
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
