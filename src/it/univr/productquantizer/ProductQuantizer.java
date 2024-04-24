package it.univr.productquantizer;

import java.util.HashMap;
import java.util.List;

import it.univr.normal.quantizer.OneDimensionalGrid;

/**
 * Abstract base class for a product quantizer.
 * 
 * @author Alessandro Fina
 *
 */
public abstract class ProductQuantizer {

/**
 * Build a new Product quantizer through the grids in input.
 * 
 * @param args the grids in input
 * @return the product quantizer
 */
public static final ProductQuantizer build(OneDimensionalGrid... args) {
		
switch(args.length) {
		
case(1) :
	return new OneDimProductQuantizer(args[0]);			
case(2) :
	return new TwoDimProductQuantizer(args[0], args[1]);
case(3) :
	return new ThreeDimProductQuantizer(args[0], args[1], args[2]);
default:
throw new 
IllegalArgumentException("The dimension of the product quantizer does not exist");
	}
}

/**
 * Build a new product quantizer of size one.
 * 
 * @param values the values of the quantizer
 * @param size the size of every grid
 * @return the product quantizer with only one value
 */
public static final ProductQuantizer buildWithCostantValues(List<Double> values,
	                                                        List<Integer> size) {
switch(values.size()) {
		
case(1) :
	return 
	OneDimProductQuantizer.buildWithConstantValues(values.get(0), size.get(0));		
case(2) :
	return 
	TwoDimProductQuantizer.buildWithConstantValues(values.get(0),values.get(1),
			                                       size.get(0),size.get(1));
case(3) :
	return 
	ThreeDimProductQuantizer.buildWithConstantValues(values.get(0), values.get(1),
	                                                 values.get(2), size.get(0),
	                                                 size.get(1), size.get(2));
default:
throw new 
IllegalArgumentException("The dimension of the product quantizer does not exist");
	}	
}
	
/**
 * @return the number of process in the product quantizer
 */
public abstract int getNumberOfProcess();

/**
 * @return HashMap where the keys are the index of the set and the values
 * are the points
 */
public abstract HashMap<int[], double[]> getSet();
	
/**
 * @return HashMap where the keys are the points of the set and the values
 * are their companion weights
 */
public abstract HashMap<double[], Double> getDistribution();

/**
 * Get the boundaries of the quantizer of interest. The quantizer is an
 * element of the set of this instance.
 * 
 * @param quatizer
 * @param ul '+' for the upper-bound, '-' for the lower-bound
 * @return the boundaries
 */
public abstract double[] getBoundaries(int[] quantizer, char ul);

/**
 * Get the boundaries of the specified component of the quantizer
 * of interest. The quantizer is an element of the set of this instance.
 * @param quatizer
 * @param ul '+' for the upper-bound, '-' for the lower-bound
 * @param process the process of interest
 * @return the boundary
 */
public abstract double getBoundariesOf(int[] quantizer, char ul, int process);

/**
 * Get the set of the grids.
 * 
 * @return HashMap where the keys are the numbers of the grids and
 * the values are the grid.
 */
public abstract HashMap<Integer, OneDimensionalGrid> getGrids();
	
/**
 * Get the size of the grid of interest
 * 
 * @param grid the grid of interest
 * @return the size of the grid
 */
public abstract int getGridSize(int grid);

/**
 * Get the quantizer.
 * 
 * @return the quantizer
 */
public abstract double[] get(int... point);


/**
 * Set the weight of the specified point.
 * 
 * @param point The point of interest
 * @param weight The probability of the point
 */
public abstract void setWeight(double[] point, double weight);
}