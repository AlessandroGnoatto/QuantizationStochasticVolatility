package it.univr.test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;


public class mctest32 {

	public static void main(String[] args) {

		int numberOfTimeSteps = 100;
		int numberOfPaths = 1000000;
		
		double deltaT = 1.0/numberOfTimeSteps;
		
		double s = 100;
		double r = 0.04;
		double k = 8.85;
		double v = 0.0575;
		double theta = 0.075;
		double sigma = 2.56 ;
		double rho = -0.94;
		double K = 80;
		
		
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
				
				sSpace[j] = sSpace[j-1] + r*sSpace[j-1]* deltaT + Math.sqrt(Math.abs(vSpace[j-1]))*sSpace[j-1]*
						Math.sqrt(deltaT)*risk1;
				
				vSpace[j] = vSpace[j-1] + k*Math.abs(vSpace[j-1])*(theta - Math.abs(vSpace[j-1]))* deltaT + rho*sigma*Math.pow(Math.abs(vSpace[j-1]),3.0/2.0)
							*Math.sqrt(deltaT)*risk1
						+ sigma*Math.pow(Math.abs(vSpace[j-1]),3.0/2.0)*Math.sqrt(1-Math.pow(rho, 2))*Math.sqrt(deltaT)*risk2;

			}
			
		results[i-1] = Math.exp(-r)*Math.max(sSpace[numberOfTimeSteps] - K,0);
			
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
