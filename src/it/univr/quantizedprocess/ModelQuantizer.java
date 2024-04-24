package it.univr.quantizedprocess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.RealVector;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.AbstractModel;
import it.univr.normal.quantizer.MultiDimNormalGrid;
import it.univr.normal.quantizer.OneDimensionalGrid;
import it.univr.optimalgrid.NewtonRaphsonMethod;
import it.univr.productquantizer.ProductQuantizer;

public class ModelQuantizer {
	

	private final int sizeForCubatureFormula = 10;
	
	private NormalDistribution x = new NormalDistribution();
	private AbstractModel model;
	
	public MultiDimNormalGrid NormalGridCubFormula;
	ProductQuantizer startingProductQuantizer;
	private final ArrayList<Double> jposplus = new ArrayList<Double>();
	private final ArrayList<Double> jposminus = new ArrayList<Double>();
	private final ArrayList<Double> jnegplus = new ArrayList<Double>();
	private final ArrayList<Double> jnegminus = new ArrayList<Double>();
	private double deltaT;
	
	private TransitionProbabilityLattice transitionLattice = new TransitionProbabilityLattice();
	private QuantizedProcessLattice quantizedProcess;
	
	/**
	 * Recursive product-quantization of the model in input.
	 * 
	 * @param model
	 * @param numberOfTimeSteps
	 * @param deltaT
	 * @param startingProductQuantizer
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ModelQuantizer(AbstractModel model, 
								   int numberOfTimeSteps,
								   double deltaT,
								   ProductQuantizer startingProductQuantizer) throws InvalidFormatException, IOException {
		
		this.startingProductQuantizer = startingProductQuantizer;
		this.model = model;
		this.deltaT = deltaT;
		
		QuantizedProcessLattice quantizedProcess = new QuantizedProcessLattice(model, numberOfTimeSteps, deltaT);
	
		quantizedProcess.setProductQuantizer(0, startingProductQuantizer);
		this.quantizedProcess = quantizedProcess;
	}
	
	
	public QuantizedModel quantize() throws InvalidFormatException, IOException {
		
		
		quantizedProcess.getTimeSet().stream().filter(i -> i >=1).forEach(i -> {
		
		ProductQuantizer next = null;
		
		try {
			next = this.computeProductQuantizer(i);
		} catch (InvalidFormatException|IOException e) {

			e.printStackTrace();
		}
		
		try {
			this.computeDistribution(next, i);
		} catch (InvalidFormatException|IOException e) {

			e.printStackTrace();
		}
		
			this.quantizedProcess.setProductQuantizer(i, next);
	}
			);

	
	return new QuantizedModel(this.quantizedProcess,this.transitionLattice,this.model);
	}
	
	
	/**
	 * Implements formula 40
	 * 
	 * @param time The time step of interest
	 * @param stateVariable
	 * @return
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
	private double psi(ProductQuantizer step, 
						int[] quantizer, 
						double time, 
						double[] stateVariable, 
						RealVector normalVariable) throws InvalidFormatException, IOException {

	 for(int i = 1; i <= this.model.getNumberOfProcess(); i++) {
			
		double upperbound = step.getBoundariesOf(quantizer, '+', i);
		double lowerbound = step.getBoundariesOf(quantizer, '-', i);
			
		RealVector actualRow = this.model.getDiffusionRow(stateVariable, time,i);
		RealVector subvector = actualRow.getSubVector(1, this.model.getNumberOfRiskFactors() -1);

		if(actualRow.getEntry(0) == 0.0) {						
			if((Math.sqrt(deltaT) * subvector.dotProduct(normalVariable) > upperbound - stateVariable[i-1] - deltaT *
								   this.model.getDrift(stateVariable, time,i)) ||
			   (Math.sqrt(deltaT) * subvector.dotProduct(normalVariable) < lowerbound - stateVariable[i-1] - deltaT *
				   				   this.model.getDrift(stateVariable, time,i))) {
				jposplus.clear();
				jposminus.clear();
				jnegplus.clear();
				jnegminus.clear();
			  return 0.0;
				}
			} else
				
		if(actualRow.getEntry(0) < 0) {
			
			jnegminus.add((lowerbound - stateVariable[i-1] - 
						   this.model.getDrift(stateVariable, time,i) * deltaT
						   - Math.sqrt(deltaT) * subvector.dotProduct(normalVariable)) /
						   (Math.sqrt(deltaT) * actualRow.getEntry(0)));
			jnegplus.add((upperbound - stateVariable[i-1] -
						   this.model.getDrift(stateVariable, time,i) * deltaT
						   - Math.sqrt(deltaT) * subvector.dotProduct(normalVariable)) /
						   (Math.sqrt(deltaT) * actualRow.getEntry(0)));			
			} else
		if(actualRow.getEntry(0) > 0) {

			jposminus.add((lowerbound - stateVariable[i-1] -
						   this.model.getDrift(stateVariable, time,i) * deltaT
						   - Math.sqrt(deltaT) * subvector.dotProduct(normalVariable)) /
						   (Math.sqrt(deltaT) * actualRow.getEntry(0)));
			jposplus.add((upperbound - stateVariable[i-1] -
						   this.model.getDrift(stateVariable, time,i) * deltaT
						   - Math.sqrt(deltaT) * subvector.dotProduct(normalVariable)) /
						   (Math.sqrt(deltaT) * actualRow.getEntry(0)));
			}
		}		
		double alpha = 0;
		double beta  = 0;
		
		if(jnegminus.isEmpty() & jposminus.isEmpty() & jnegplus.isEmpty()  & jposplus.isEmpty()) {
		 return 1.0;
		}		
		if(jposminus.isEmpty() & jposplus.isEmpty()) {
			alpha = Collections.max(jnegplus);
			beta  = Collections.min(jnegminus);
			jnegplus.clear();
			jnegminus.clear();

		 return Math.max(x.cumulativeProbability(beta) - x.cumulativeProbability(alpha), 0);
		}  			
		if(jnegminus.isEmpty() & jnegplus.isEmpty()) {
			alpha = Collections.max(jposminus);
			beta  = Collections.min(jposplus);
			jposplus.clear();
			jposminus.clear();
		 return Math.max(x.cumulativeProbability(beta) - x.cumulativeProbability(alpha), 0);
		}
			alpha = Math.max(Collections.max(jposminus), Collections.max(jnegplus));
			beta  = Math.min(Collections.min(jposplus), Collections.min(jnegminus));
			jposplus.clear();
			jposminus.clear();
			jnegplus.clear();
			jnegminus.clear();			
		 return Math.max(x.cumulativeProbability(beta) - x.cumulativeProbability(alpha),0);
	}
	
	private double psiCubatureFormula(ProductQuantizer step, 
										int[] quantizer, 
										double time, 
										double[] stateVariable) throws InvalidFormatException, IOException {
		
		double expectedValue = 0;
		
		if(this.NormalGridCubFormula == null) {
			
			this.NormalGridCubFormula = new MultiDimNormalGrid(sizeForCubatureFormula,this.model.getNumberOfRiskFactors() - 1);
		}
		for(Map.Entry<RealVector, Double> element : this.NormalGridCubFormula.getGridSet()) {

			expectedValue = expectedValue + this.psi(step, quantizer, time, stateVariable, element.getKey()) *
					element.getValue();
		
		}

		return expectedValue;
	
	}
	
	
	private double probability(ProductQuantizer step,
							  int[] quantizer, 
							  int timeStep) throws InvalidFormatException, IOException {
		
		double probability = 0;
		
		for(Map.Entry<double[], Double> element : quantizedProcess.getProductQuantizerDistribution(timeStep-1).entrySet()) {
		
		if(this.model.getNumberOfRiskFactors() == 1) {
			double conditionalProbability = x.cumulativeProbability((step.getBoundariesOf(quantizer, '+', 1)-
					deltaT*this.model.getDrift(element.getKey(), deltaT*(timeStep-1), 0)- element.getKey()[0])/
					(Math.sqrt(deltaT)*this.model.getDiffusionRow(element.getKey(), deltaT*(timeStep-1), 0).getNorm())) - 
					x.cumulativeProbability((step.getBoundariesOf(quantizer, '-', 1)-
							deltaT*this.model.getDrift(element.getKey(), deltaT*(timeStep-1), 0)- element.getKey()[0])/
							(Math.sqrt(deltaT)*this.model.getDiffusionRow(element.getKey(), deltaT*(timeStep-1), 0).getNorm()));
			this.transitionLattice.setTransitionProbability(timeStep-1, element.getKey(), step.get(quantizer[0]), conditionalProbability);
			probability = probability + conditionalProbability*element.getValue();
			continue;
		}
		
		double conditionalProbability = this.psiCubatureFormula(step, quantizer, timeStep*(deltaT-1), element.getKey());
		this.transitionLattice.setTransitionProbability(timeStep-1, element.getKey(), step.get(quantizer), conditionalProbability);
		probability = probability + conditionalProbability*element.getValue();
		}
		return probability;
	}
	
	
	private ProductQuantizer computeProductQuantizer(int timeStep) throws InvalidFormatException, IOException {
		
		OneDimensionalGrid[] newGrids = new OneDimensionalGrid[this.model.getNumberOfProcess()];
		for(Integer element : quantizedProcess.getGridSet(timeStep-1).keySet()) 
		{
			OneDimensionalGrid f = NewtonRaphsonMethod.find(model, quantizedProcess.getProductQuantizer(timeStep-1), element, deltaT*timeStep, deltaT);
			
		newGrids[element - 1] = f;
		}
		ProductQuantizer next = ProductQuantizer.build(newGrids);
		return next;
	}
	
	private void computeDistribution(ProductQuantizer next, int timeStep) throws InvalidFormatException, IOException {

		for(Map.Entry<int[], double[]> element : next.getSet().entrySet()) {

			next.setWeight(element.getValue(), probability(next,element.getKey(),timeStep));
		}

		
	}

	
}
