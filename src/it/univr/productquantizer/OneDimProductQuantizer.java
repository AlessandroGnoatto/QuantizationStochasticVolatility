package it.univr.productquantizer;

import java.util.HashMap;
import java.util.Map;
import it.univr.normal.quantizer.OneDimensionalGrid;

/**
 * Implements the Quantization of a stationary normal grid.
 * 
 * @author Alessandro Fina
 *
 */
public class OneDimProductQuantizer extends ProductQuantizer {
    
private HashMap<Integer, OneDimensionalGrid> grids;
private HashMap<double[], Double> distribution;
private HashMap<int[], double[]> set;
private final int numberOfProcess;
    
/**
 * Create a set for the quantization of the random variable in input.
 * The set cardinality is the cardinality of the grid in input.
 * 
 * @param first The first one-dimensional standard normal 
 * random variable grid
 */
public OneDimProductQuantizer(OneDimensionalGrid first) {
	     
HashMap<Integer, OneDimensionalGrid> grids = new 
	HashMap<Integer, OneDimensionalGrid>();

grids.put(1, first);
this.grids = grids;
HashMap<int[], double[]> set = new HashMap<int[], double[]>();
for(Map.Entry<Integer, Double> element1 : first.getGrid().entrySet())  {
	int[] key = {element1.getKey()};
	double[] value = {element1.getValue()};
	set.put(key, value);
	}
this.set = set;
this.numberOfProcess = 1;
this.distribution = new HashMap<double[], Double>(this.set.size());
}
		
/**
 * Create a new OneDimProductQuantizer of size N1 where the 
 * grid has constant value equal to value1.
 * 
 * @param value1 The value of the components of the first grid
 * @param N1 The size of the first grid
 * 
 * @return A new TwoDimProductQuantizer with where every grid 
 * has constant values
 */
public static OneDimProductQuantizer buildWithConstantValues(double value1,
                                                             int N1) {	
OneDimensionalGrid first  = 
OneDimensionalGrid.buildWithCostantValues(value1, N1);	
OneDimProductQuantizer constant = new OneDimProductQuantizer(first);	
HashMap<double[], Double> distribution = new HashMap<double[], Double>();
distribution.put(constant.get(1), 1.0);
constant.distribution = distribution;
		
return constant;
}

/**
 * Get the number of process.
 * 
 * @return the number of process
 */
@Override
public int getNumberOfProcess() {
	
return this.numberOfProcess;
}

/**
 * Get the set of the quantization of the random variable in input.
 * 
 * @return HashMap where the keys are the index of the set and the values
 * are the points
 */
@Override
public HashMap<int[], double[]> getSet() {
return set;
}

/**
 * Get the distribution of the product quantizer.
 * 
 * @return HashMap where the keys are the quantizers and
 * the values are their companion weights
 */
@Override
public HashMap<double[], Double> getDistribution() {
		
if(this.distribution == null) {
	throw new IllegalArgumentException("Distribution has not been set");
	}
return this.distribution;
}

/**
 * Get the boundaries of the quantizer of interest. The quantizer is an
 * element of the set of this instance.
 * 
 * @param quatizer
 * @param ul '+' for the upper-bound, '-' for the lower-bound
 * @return the boundaries
 */
@Override
public double[] getBoundaries(int[] quantizer, char ul) {
    	
double[] boundaries = {grids.get(1).getBoundary(quantizer[0], ul)};
 
return boundaries;    	
}

/**
 * Get the boundaries of the specified component of the quantizer
 * of interest. The quantizer is an element of the set of this instance.
 * @param quatizer
 * @param ul '+' for the upper-bound, '-' for the lower-bound
 * @param process the process of interest
 * @return the boundary
 */
@Override
public double getBoundariesOf(int[] quantizer, char ul, int process) {

return getBoundaries(quantizer,ul)[process-1];
}

/**
 * Get the set of the grids in this instance.
 * 
 * @return HashMap where the keys are the numbers of the grids and
 * the values are the grid.
 */
@Override
public HashMap<Integer, OneDimensionalGrid> getGrids() {

return this.grids;
}

/**
 * Get the size of the grid of interest
 * 
 * @param grid the grid of interest
 * @return the size of the grid
 */
@Override
public int getGridSize(int grid) {

return this.grids.get(grid).getQuantizerSize();
}

/**
 * Get the quantizer.
 * 
 * @return the quantizer
 */
@Override
public double[] get(int... point) {
for(Map.Entry<int[], double[]> element : this.set.entrySet()) {
	if(element.getKey()[0] == point[0]) {
    return element.getValue();
		}
	}
throw new IllegalArgumentException("Point not found");
}

/**
 * Set the weight of the specified point.
 * 
 * @param point The point of interest
 * @param weight The probability of the point
 */
@Override
public void setWeight(double[] point, double weight) {
    	
if(weight > 1 || weight < 0)  {
	throw new IllegalArgumentException("Invalid weight value");
 	}
this.distribution.put(point, weight);
}   
}