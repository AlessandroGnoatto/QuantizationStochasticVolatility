package it.univr.products;

import java.util.Map;

import it.univr.quantizedprocess.QuantizedModel;

public class CVA {
	
	
	double hazardrate = 0.02;
	double R = 0.4;
	

	double kdom;
	double kfor;
	double thetadom;
	double thetafor;
	double sigmafx;
	double sigmadom;
	double sigmafor;
	double rhofxdom;
	double rhofxfor;
	double rhodomfor;
	
	double spotFX;
	double spotShortdom;
	double spotShortfor;

	public CVA(double kdom, double kfor, double thetadom, double thetafor, double sigmafx, double sigmadom, double sigmafor, double rhofxdom, double rhofxfor, 
			double rhodomfor, double spotFX, double spotShortdom, double spotShortfor) {
		
		
		this.kdom = kdom;
		this.kfor = kfor;
		this.thetadom = thetadom;
		this.thetafor = thetafor;
		this.sigmafx = sigmafx;
		this.sigmadom = sigmadom;
		this.sigmafor = sigmafor;
		this.rhofxdom = rhofxdom;
		this.rhofxfor = rhofxfor;
		this.rhodomfor = rhodomfor;
		
		this.spotFX = spotFX;
		this.spotShortdom = spotShortdom;
		this.spotShortfor = spotShortfor;
		
	}
	
	
	
	public double evalueate(QuantizedModel qModel) {
		
		double DEPEsum = 0;
		double fairRateAtZero = this.spotFX*(ZCBfor(0,qModel.getDeltaT()*qModel.getNumberOfTimeSteps(), this.spotShortfor)/
				ZCBdom(0,qModel.getDeltaT()*qModel.getNumberOfTimeSteps(), this.spotShortdom));
		
		
	for(int i = 1; i <= qModel.getNumberOfTimeSteps(); i ++) {
		
		double DEPEi = 0;
		double prevTime = (i-1) * qModel.getDeltaT();
		double time = i * qModel.getDeltaT();
		
		for(Map.Entry<double[],Double> point : qModel.getProductQuantizerDistribution(i).entrySet()) {
			
			//Fair Forward rate at t
			double forwardRate = point.getKey()[0]*(ZCBfor(time,qModel.getDeltaT()*qModel.getNumberOfTimeSteps(), point.getKey()[2])/
					ZCBdom(time,qModel.getDeltaT()*qModel.getNumberOfTimeSteps(), point.getKey()[1]));
			
			//Payoff discounted at t
			double discPayoff = (forwardRate - fairRateAtZero)*ZCBdom(time,qModel.getDeltaT()*qModel.getNumberOfTimeSteps(), point.getKey()[1]);
			
			//DEPE for point
			double option = Math.max(discPayoff, 0)*ZCBdom(0,time, this.spotShortdom)*point.getValue();
			
			DEPEi = DEPEi + option*hazard(prevTime,time);
		}
		
		DEPEsum = DEPEsum + DEPEi;
	}
		
		return DEPEsum*(1-this.R);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void testHazard(double time1, double time2) {
		
		System.out.println(hazard(time1,time2));
		
	}
	
	public double hazard(double time1, double time2) {
		
		return (Math.exp(-this.hazardrate*time1) - Math.exp(-this.hazardrate*time2));
		
	}
	
	
	public void testXCB() {
		
		System.out.println(ZCBdom(0,1,this.spotShortdom) + " " + ZCBfor(0,1,this.spotShortfor));
		
	}
	
	
	private double ZCBdom(double t, double T, double shortrate) {
		
		double B = (1 - Math.exp(-this.kdom * (T - t)))/this.kdom;
		
		double A = Math.exp( (this.thetadom - Math.pow(sigmadom, 2)/(2*this.kdom) ) /this.kdom * (B - T + t) -  Math.pow(this.sigmadom, 2)/(4*this.kdom)*Math.pow(B, 2) );
		
		return A*Math.exp(-B*shortrate);
		
	}
	
	private double ZCBfor(double t, double T, double shortrate) {
		
		double B = (1 - Math.exp(-this.kfor * (T - t)))/this.kfor;
		
		double A = Math.exp( ( (this.thetafor - this.rhofxfor*this.sigmafor*this.sigmafx) - Math.pow(sigmafor, 2)/(2*this.kfor) ) /this.kfor * (B - T + t) -  Math.pow(this.sigmafor, 2)/(4*this.kfor)*Math.pow(B, 2) );
		
		return A*Math.exp(-B*shortrate);
		
	}
	
}
