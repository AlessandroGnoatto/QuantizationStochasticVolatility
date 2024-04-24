package it.univr.test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;


public class sabrmctest {

	public static void main(String[] args) {

		int numberOfTimeSteps = 100;
		int numberOfPaths = 1000000;
		
		double deltaT = 1.0/numberOfTimeSteps;
		
		double s = 100.0;
		double v = Math.log(0.06);
		double alfa = 0.8;
		double beta = 1.25;
		double rho = -0.85;
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
				
				vSpace[j] = vSpace[j-1] -0.5*Math.pow(alfa, 2)*deltaT + alfa*Math.sqrt(deltaT)*risk1;
				
				sSpace[j] = sSpace[j-1] + Math.exp(vSpace[j-1])*rho*Math.pow(sSpace[j-1], beta)*risk1*
						Math.sqrt(deltaT) + Math.sqrt(1-Math.pow(rho, 2))*Math.exp(vSpace[j-1])*Math.pow(sSpace[j-1], beta)
						*risk2*Math.sqrt(deltaT);

			}
			
		results[i-1] = Math.max(-sSpace[numberOfTimeSteps] + K,0);
			
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
