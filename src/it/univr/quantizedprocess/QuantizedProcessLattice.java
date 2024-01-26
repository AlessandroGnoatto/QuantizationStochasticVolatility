package it.univr.quantizedprocess;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.distribution.NormalDistribution;

import it.univr.model.ModelInterface;
import it.univr.normal.quantizer.OneDimStandardNormalGrid;
import it.univr.productquantizer.ProductQuantizer;

public class QuantizedProcessLattice {
	
	public NormalDistribution x = new NormalDistribution();
	public HashMap<Integer, ProductQuantizer> quantizedProcess;
	public int numberOfTimeSteps;
	public double deltaT;
	public ModelInterface model;

	public QuantizedProcessLattice(ModelInterface model, int numberOfTimeSteps, double deltaT) {
		
		this.numberOfTimeSteps = numberOfTimeSteps;
		this.deltaT = deltaT;
		this.model = model;
		this.quantizedProcess = new HashMap<Integer, ProductQuantizer>();
		
		for(int i = 0; i <= numberOfTimeSteps; i ++) {
			this.quantizedProcess.put(i, null);
		}
	}
	
	public void setQuantizedProcessOutput(int timeStep, ProductQuantizer output) {
		
		this.quantizedProcess.put(timeStep, output);
	}
	
	public ProductQuantizer getQuantizedProcessOutput(int timeStep) {
		
		return this.quantizedProcess.get(timeStep);
	}
	
	public Set<Integer> getKeySet() {
		
		return this.quantizedProcess.keySet();
	}

	public HashMap<double[], Double> getDistributionOf(int timeStep) {
		
		return this.getQuantizedProcessOutput(timeStep).getDistribution();
}
	public HashMap<Integer, OneDimStandardNormalGrid> getGridSetOf(int timeStep) {
		
		return this.getQuantizedProcessOutput(timeStep).getGrids();
	}
	
	public int getNumberOfTimeSteps() {
		
		return this.numberOfTimeSteps;
	}
	
	public OneDimStandardNormalGrid getGrid(int timeStep, int process) {
		
		return this.getGridSetOf(timeStep).get(process);
	}
	
	public HashMap<Double, Double> getMarginalDistributionOf(int process) {
		
		HashMap<Double, Double> distribution = new HashMap<Double, Double>();
		if(this.model.getNumberOfRiskFactors() == 1) {
			
			this.getDistributionOf(numberOfTimeSteps).entrySet().stream().forEach(i -> {
				distribution.put(i.getKey()[0], i.getValue());
			});
			
			return distribution;
		}
		
	
	
		this.getGrid(numberOfTimeSteps, process).getGrid().entrySet().stream().forEach(i -> {
		
		double value = 0;
		for(Map.Entry<double[], Double> j: this.getDistributionOf(numberOfTimeSteps-1).entrySet()){
			value =value +
					 (x.cumulativeProbability((this.getGrid(numberOfTimeSteps, process).getBoundary(i.getKey(), '+') -
					 deltaT*this.model.getDrift(j.getKey(),(numberOfTimeSteps-1)*deltaT,process) -
					 j.getKey()[process-1])/(Math.sqrt(deltaT)*this.model.getDiffusionRow
					 (j.getKey(), (numberOfTimeSteps-1)*deltaT,process).getNorm())) -
					 x.cumulativeProbability((this.getGrid(numberOfTimeSteps, process).getBoundary(i.getKey(), '-') -
					 deltaT*this.model.getDrift(j.getKey(),(numberOfTimeSteps-1)*deltaT,process) -
					 j.getKey()[process-1])/(Math.sqrt(deltaT)*this.model.getDiffusionRow
					 (j.getKey(), (numberOfTimeSteps-1)*deltaT,process).getNorm())))*j.getValue();
			}
		distribution.put(i.getValue(), value);
		});
	
	return distribution;
	}
}
