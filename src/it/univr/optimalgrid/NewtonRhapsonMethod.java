package it.univr.optimalgrid;


import java.io.IOException;
import java.util.Map;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import it.univr.model.ModelInterface;
import it.univr.normal.quantizer.OneDimStandardNormalGrid;
import it.univr.productquantizer.ProductQuantizer;

public class NewtonRhapsonMethod {

	
	private static NormalDistribution x = new NormalDistribution();

	private static final double epsilon = 0.000001;
	public static final double eta = 1.0;
	
	public NewtonRhapsonMethod() {
	
	}
	
	/**
	 * Compute the optimal quantization grid
	 * 
	 * @param model The model
	 * @param previous ProductQuantizer Previous realization of the product quantized process
	 * @param process The process of interest
	 * @param time The time of evaluation i.e. i*deltaT
	 * @param deltaT 
	 * @return The stationary grid
	 * 
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static OneDimStandardNormalGrid find(ModelInterface model,
								 ProductQuantizer previousProductQuantizer,				 
								 int process,
								 double time,
								 double deltaT)
								 throws InvalidFormatException, IOException {
	
		double previousTime = time-deltaT;
	
		if(time > deltaT) {
			OneDimStandardNormalGrid startingPoint = previousProductQuantizer.getGrids().get(process);
		System.out.println("Initial  " + startingPoint.getGrid());
		OneDimStandardNormalGrid found = search(model,previousProductQuantizer,process,previousTime,deltaT,startingPoint,eta);
		  System.out.println("Solution " + found.getGrid());
		  for(int i = 2; i <= found.getQuantizerSize(); i++) {
			  if(found.getQuantizer(i) < found.getQuantizer(i-1)) {
				  System.out.println(gradient(model,previousProductQuantizer,process,time,deltaT,startingPoint));
				  throw new ArithmeticException("Grid inconstistent");
			  }
		  }
		  return found;
		
		
		}
		
		if(time == deltaT) {
			OneDimStandardNormalGrid startingPoint = new OneDimStandardNormalGrid(previousProductQuantizer.getGridSize(process)); 
	
		
		
		  double mean = 0; double variance = 0;
		  
		  for(Map.Entry<double[], Double> element :
		  previousProductQuantizer.getDistribution().entrySet()) { mean = mean +
		  (deltaT*model.getDrift(element.getKey(), previousTime,process) +
		  element.getKey()[process-1]) * element.getValue();
		  
		  variance = variance +
		  (deltaT*Math.pow(model.getDiffusionRow(element.getKey(),previousTime,process)
		  .getNorm(),2))*element.getValue(); }
		  
		  for(Map.Entry<Integer, Double> element : startingPoint.getGrid().entrySet())
		  {
		  
		  startingPoint.getGrid().replace(element.getKey(),
		  element.getValue().doubleValue() * Math.sqrt(variance) + mean);
		  
		  }
		  System.out.println("Initial  " + startingPoint.getGrid());
		  OneDimStandardNormalGrid found = search(model,previousProductQuantizer,process,previousTime,deltaT,startingPoint,eta);
		  System.out.println("Solution " + found.getGrid());
		  for(int i = 2; i <= found.getQuantizerSize(); i++) {
			  if(found.getQuantizer(i) < found.getQuantizer(i-1)) {
				  System.out.println(gradient(model,previousProductQuantizer,process,time,deltaT,startingPoint));
				  throw new ArithmeticException("Grid inconstistent");
			  }
		  }
		  return found;
		
		 
		}

		return null;
		
		
	}
	
	
	/**
	 * Implements formula 49
	 * 
	 * @param model The reference model
	 * @param previousProductQuantizer
	 * @param process Process of interest
	 * @param time
	 * @param deltaT
	 * @param startingPoint Point at which gradient is valued
	 * 
	 * @return Gradient valued at startingPoint
	 */
	private static RealVector gradient(ModelInterface model,
			 				   		   ProductQuantizer previousProductQuantizer,				 
			 				   		   int process,
			 				   		   double time,
			 				   		   double deltaT,
			 				   		OneDimStandardNormalGrid startingPoint) {
		
	 ArrayRealVector gradient = new ArrayRealVector(startingPoint.getQuantizerSize());
	  for(int i = 1; i <= startingPoint.getQuantizerSize(); i++) {
	   double expectedValue = 0;
		 for(Map.Entry<double[], Double> element : previousProductQuantizer.getDistribution().entrySet() ) {
		  double value = (startingPoint.getQuantizer(i) - element.getKey()[process-1] - deltaT *
						 model.getDrift(element.getKey(), time, process)) *
						 (x.cumulativeProbability((startingPoint.getBoundary(i, '+') - element.getKey()[process -1] -
						 deltaT * model.getDrift(element.getKey(), time-deltaT,process)) /
						 (Math.sqrt(deltaT) * model.getDiffusionRow(element.getKey(), time, process).getNorm())) -
						 x.cumulativeProbability((startingPoint.getBoundary(i, '-') - element.getKey()[process-1] -
						 deltaT * model.getDrift(element.getKey(), time, process))  /
						 (Math.sqrt(deltaT) * model.getDiffusionRow(element.getKey(), time,process).getNorm()))) +
						 Math.sqrt(deltaT) * model.getDiffusionRow(element.getKey(), time, process).getNorm() *
						 (x.density((startingPoint.getBoundary(i, '+') - element.getKey()[process-1] -
						 deltaT * model.getDrift(element.getKey(), time, process)) /
					     (Math.sqrt(deltaT) * model.getDiffusionRow(element.getKey(), time,process).getNorm())) -
						 x.density((startingPoint.getBoundary(i, '-') -  element.getKey()[process-1] -
						 deltaT * model.getDrift(element.getKey(), time,process)) /
						 (Math.sqrt(deltaT) * model.getDiffusionRow(element.getKey(), time, process).getNorm())));
			
				expectedValue = expectedValue + value*element.getValue();
		}
		gradient.setEntry(i - 1 , expectedValue);
	  }
	return gradient;	
	}
	

	/**
	 * Implements formula 49-2
	 * 
	 * @param startingPoint
	 * @param pointPosition
	 * @param time
	 * @return
	 */
	private static RealMatrix hessian(ModelInterface model,
			 				  ProductQuantizer previousProductQuantizer,				 
			 				  int process,
			 				  double time,
			 				  double deltaT,
			 				 OneDimStandardNormalGrid startingPoint) {
		
	Array2DRowRealMatrix hessian = new Array2DRowRealMatrix(startingPoint.getQuantizerSize(),startingPoint.getQuantizerSize());
	
	for(int i = 0; i < startingPoint.getQuantizerSize(); i++) {
		
		if(i == 0) {
			hessian.setEntry(i,   i, diagonal(model,previousProductQuantizer,process,time,deltaT,startingPoint,i+1)
								   + superDiagonal(model,previousProductQuantizer,process,time,deltaT,startingPoint,i+1));
			hessian.setEntry(i, i+1, superDiagonal(model,previousProductQuantizer,process,time,deltaT,startingPoint,i+1));
		}
		if(i > 0 & i < startingPoint.getQuantizerSize() - 1) {
			hessian.setEntry(i, i, 	 diagonal(model,previousProductQuantizer,process,time,deltaT,startingPoint,i+1) + 
									 superDiagonal(model,previousProductQuantizer,process,time,deltaT,startingPoint,i+1) + 
									 subDiagonal(model,previousProductQuantizer,process,time,deltaT,startingPoint,i+1));
			hessian.setEntry(i, i-1, subDiagonal(model,previousProductQuantizer,process,time,deltaT,startingPoint,i+1));
			hessian.setEntry(i, i+1, superDiagonal(model,previousProductQuantizer,process,time,deltaT,startingPoint,i+1));
		}
		if(i == startingPoint.getQuantizerSize() -1) {
			hessian.setEntry(i,   i, diagonal(model,previousProductQuantizer,process,time,deltaT,startingPoint,i+1)
								   + subDiagonal(model,previousProductQuantizer,process,time,deltaT,startingPoint,i+1));
			hessian.setEntry(i, i-1, subDiagonal(model,previousProductQuantizer,process,time,deltaT,startingPoint,i+1));
		}	
	}
	return hessian;
	}
				

		/**
		 * Super-diagonal term of the Hessian matrix.
		 * 
		 * @param time
		 * @param quantizer
		 * @return
		 */
	private static double superDiagonal(ModelInterface model,
									 ProductQuantizer previousProductQuantizer,				 
									 int process,
									 double time,
									 double deltaT,
									 OneDimStandardNormalGrid startingPoint,
									 int quantizer) {
			
		double value = 0;	
			for(Map.Entry<double[], Double> element : previousProductQuantizer.getDistribution().entrySet() ) {
				double eval = -(1.0/4)*(1.0/(Math.sqrt(deltaT) * 
						      model.getDiffusionRow(element.getKey(), time,process).getNorm())) *
							  (startingPoint.getQuantizer(quantizer+1) - startingPoint.getQuantizer(quantizer)) *
							  x.density((startingPoint.getBoundary(quantizer, '+') - element.getKey()[process-1] -
							  deltaT * model.getDrift(element.getKey(), time,process)) /
							  (Math.sqrt(deltaT) * model.getDiffusionRow(element.getKey(), time,process).getNorm()));
				
				value = value + eval*element.getValue();
			}
		return value;
		}
		
		/**
		 * Sub-diagonal term of the Hessian matrix.
		 * 
		 * @param time
		 * @param quantizer
		 * @return
		 */
	private static double subDiagonal(ModelInterface model,
				 				   	  ProductQuantizer previousProductQuantizer,				 
				 				   	  int process,
				 				   	  double time,
				 				   	  double deltaT,
				 				   	OneDimStandardNormalGrid startingPoint,
				 				   	  int quantizer) {
			
			double value = 0;
			for(Map.Entry<double[], Double> element : previousProductQuantizer.getDistribution().entrySet() ) {
				double eval = -(1.0/4) * (1.0/(Math.sqrt(deltaT) * 
							  model.getDiffusionRow(element.getKey(), time,process).getNorm())) *
							  (startingPoint.getQuantizer(quantizer) - startingPoint.getQuantizer(quantizer-1)) *
							  x.density((startingPoint.getBoundary(quantizer, '-') - element.getKey()[process-1] -
							  deltaT * model.getDrift(element.getKey(), time,process)) /
						      (Math.sqrt(deltaT) * model.getDiffusionRow(element.getKey(), time,process).getNorm()));
				
				value = value + eval*element.getValue();
			}
		return value;			
		}
		
		/**
		 * 
		 * Diagonal term of the Hessian Matrix, without sub-diagonal or super-diagonal.
		 * 
		 * @param time
		 * @param quantizer
		 * @return
		 */
	private static double diagonal(ModelInterface model,
				 				   ProductQuantizer previousProductQuantizer,				 
				 				   int process,
				 				   double time,
				 				   double deltaT,
				 				  OneDimStandardNormalGrid startingPoint,
				 				   int quantizer) {
			
			double value = 0;	
			for(Map.Entry<double[], Double> element : previousProductQuantizer.getDistribution().entrySet() ) {
			double eval = x.cumulativeProbability((startingPoint.getBoundary(quantizer, '+') - element.getKey()[process-1] -
						  deltaT * model.getDrift(element.getKey(), time,process)) /
						  (Math.sqrt(deltaT) * model.getDiffusionRow(element.getKey(), time,process).getNorm())) - 
						  x.cumulativeProbability((startingPoint.getBoundary(quantizer, '-') - element.getKey()[process-1] -
						  deltaT * model.getDrift(element.getKey(), time,process)) /
						  (Math.sqrt(deltaT) * model.getDiffusionRow(element.getKey(), time,process).getNorm()));
				
				value = value + eval*element.getValue();	
			}
		return value;
		}
		
	public static RealVector step(ModelInterface model,
				 	  			  ProductQuantizer previousProductQuantizer,				 
				 	  			  int process,
				 	  			  double time,
				 	  			  double deltaT,
				 	  			OneDimStandardNormalGrid startingPoint) {
			
			return MatrixUtils.inverse(hessian(model,
					 previousProductQuantizer,				 
					 process,
					 time,
					 deltaT,
					 startingPoint
					 )).operate(gradient(model, previousProductQuantizer,process,time,deltaT,startingPoint));
		}
		
		
	public static OneDimStandardNormalGrid search(ModelInterface model,
				 						  ProductQuantizer previousProductQuantizer,				 
				 						  int process,
				 						  double time,
				 						  double deltaT,
				 						 OneDimStandardNormalGrid startingPoint,
				 						  double eta) {
		
		OneDimStandardNormalGrid initial = new OneDimStandardNormalGrid();
		for(Map.Entry<Integer, Double> element : startingPoint.getGrid().entrySet()) {
				initial.getGrid().put(element.getKey(), element.getValue());
			}
			try {	
			while(step(model,previousProductQuantizer,process,time,deltaT,startingPoint).getNorm() > epsilon) {


				ArrayRealVector startingArray = new ArrayRealVector(startingPoint.getGrid()
																	.values()
																	.toArray(new Double[startingPoint.getQuantizerSize()]));
				startingArray = startingArray.subtract(step(model,
															previousProductQuantizer,
															process,
															time,
															deltaT,
															startingPoint)
															.mapMultiply(eta));
				
				for(int i =0; i < startingArray.getDimension(); i++) {
					startingPoint.getGrid().replace(i+1, startingArray.getEntry(i));
					}
				}
			
			} catch (Exception e) {
				System.out.println("catched" + eta);
				search(model,
					   previousProductQuantizer,				 
					   process,
					   time,
					   deltaT,
					   initial,
					   eta-0.05);
				}
		return startingPoint;
		}
}
		
