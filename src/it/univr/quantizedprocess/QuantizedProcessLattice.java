package it.univr.quantizedprocess;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.distribution.NormalDistribution;

import it.univr.model.AbstractModel;
import it.univr.normal.quantizer.OneDimensionalGrid;
import it.univr.productquantizer.ProductQuantizer;

public class QuantizedProcessLattice {
	
	private NormalDistribution x = new NormalDistribution();
	private double deltaT;
	private int numberOfTimeSteps;
	private AbstractModel model;
	private HashMap<Integer, ProductQuantizer> productQuantizerSet;
	public QuantizedProcessLattice(AbstractModel model, int numberOfTimeSteps, double deltaT) {
		
		this.numberOfTimeSteps = numberOfTimeSteps;
		this.deltaT = deltaT;
		this.model = model;
		this.productQuantizerSet = new HashMap<Integer, ProductQuantizer>();
		
		for(int i = 0; i <= numberOfTimeSteps; i ++) {
			this.productQuantizerSet.put(i, null);
		}
	}
	
	public void setProductQuantizer(int timeStep, ProductQuantizer output) {
		
		this.productQuantizerSet.put(timeStep, output);
	}

	public double getDeltaT() {
		
		return this.deltaT;
	}

	public AbstractModel getModel() {
		
		return this.model;
	}

	public int getNumberOfTimeSteps() {
		
		return this.numberOfTimeSteps;
	}

	public Set<Integer> getTimeSet() {
		
		return this.productQuantizerSet.keySet();
	}

	public ProductQuantizer getProductQuantizer(int timeStep) {
		
		return this.productQuantizerSet.get(timeStep);
	}
	
	public HashMap<double[], Double> getProductQuantizerDistribution(int timeStep) {
		
		return this.getProductQuantizer(timeStep).getDistribution();
}
	public HashMap<Integer, OneDimensionalGrid> getGridSet(int timeStep) {
		
		return this.getProductQuantizer(timeStep).getGrids();
	}
	
	public HashMap<Double, Double> getMarginalDistributionOf(int process) {
		
		HashMap<Double, Double> distribution = new HashMap<Double, Double>();
		if(this.model.getNumberOfRiskFactors() == 1) {
			
			this.getProductQuantizerDistribution(numberOfTimeSteps).entrySet().stream().forEach(i -> {
				distribution.put(i.getKey()[0], i.getValue());
			});
			
			return distribution;
		}
		
	
	
		for(Map.Entry<Integer, Double> i : this.getGrid(numberOfTimeSteps, process).getGrid().entrySet()) {
		
		double value = 0;
		for(Map.Entry<double[], Double> j: this.getProductQuantizerDistribution(numberOfTimeSteps-1).entrySet()){
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
		}
		
	
	return distribution;
	}

	private OneDimensionalGrid getGrid(int timeStep, int process) {
		
		return this.getProductQuantizer(timeStep).getGrids().get(process);
	}
}
