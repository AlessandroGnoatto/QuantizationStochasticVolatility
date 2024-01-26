package it.univr.productquantizer;

import java.util.HashMap;
import java.util.Map;
import it.univr.normal.quantizer.OneDimStandardNormalGrid;

	/**
	 * Implements the Product Quantization of three stationary normal grids.
	 * 
	 * @author Alessandro Fina
	 *
	 */
	public class ThreeDimProductQuantizer extends ProductQuantizer {
    
		private HashMap<Integer, OneDimStandardNormalGrid> grids;
		public HashMap<double[], Double> distribution;
		private HashMap<int[], double[]> set;
		private final int numberOfProcess;
    
    
	/**
	 * Create a set for the product quantization of the random variables in input
	 * The set cardinality is the product of the cardinality of the grids in input
	 * 
	 * @param first The first one-dimensional standard normal random variable grid
	 * @param second The second one-dimensional standard normal random variable grid
	 * @param third The third one-dimensional standard normal random variable grid
	 */
	public ThreeDimProductQuantizer(OneDimStandardNormalGrid first,
			OneDimStandardNormalGrid second,
			OneDimStandardNormalGrid third) {
	     
		HashMap<Integer, OneDimStandardNormalGrid> grids = new HashMap<Integer, OneDimStandardNormalGrid>();
		grids.put(1, first);
		grids.put(2, second);
		grids.put(3, third);
		
		this.grids = grids;
	            
	    HashMap<int[], double[]> set = new HashMap<int[], double[]>();
	    
	    for(Map.Entry<Integer, Double> element1 : first.getGrid().entrySet())  {
	    	for(Map.Entry<Integer, Double> element2 : second.getGrid().entrySet()) {
	    	    for(Map.Entry<Integer, Double> element3 : third.getGrid().entrySet()) {
	    		    int[] key = {element1.getKey(), element2.getKey(), element3.getKey()};
	    		    double[] value = {element1.getValue(), element2.getValue(), element3.getValue()};
	    		    set.put(key, value);
	        	}   
	        }
	    }
	    this.set = set;
	    this.numberOfProcess = 3;
	    this.distribution = new HashMap<double[], Double>(this.set.size());
	}
	
	
	/**
	 * Create a new ThreeDimProductQuantizer of size N1 x N2 x N3 where every grid has constant values
	 * respectively of value1, value2 and value3.
	 * 
	 * @param value1 The value of the components of the first grid
	 * @param value2 The value of the components of the second grid
	 * @param value3 The value of the components of the third grid
	 * @param N1 The size of the first grid
	 * @param N2 The size of the second grid
	 * @param N3 The size of the third grid
	 * 
	 * @return A new ThreeDimProductQuantizer with where every grid has constant values
	 */
	public static ThreeDimProductQuantizer buildWithConstantValues(double value1,
	                                                               double value2,
	                                                               double value3,
	                                                               int N1,
	                                                               int N2,
	                                                               int N3) {
		
		OneDimStandardNormalGrid first  = OneDimStandardNormalGrid.buildWithCostantValues(value1, N1);
		OneDimStandardNormalGrid second = OneDimStandardNormalGrid.buildWithCostantValues(value2, N2);
		OneDimStandardNormalGrid third  = OneDimStandardNormalGrid.buildWithCostantValues(value3, N3);
		
		ThreeDimProductQuantizer constant = new ThreeDimProductQuantizer(first,second,third);
		
		HashMap<double[], Double> distribution = new HashMap<double[], Double>();
		distribution.put(constant.get(1,1,1), 1.0);
		constant.distribution = distribution;
		
		return constant;
	}

	
	/**
	 * Get the set of the product quantization of the random variables in input.
	 * 
	 * @return HashMap where the keys are the index of the set and the values
	 * are the points
	 */
	@Override
	public HashMap<int[], double[]> getSet() {
		return set;
	}



	/**
     * Get the point of interest in the product quantization grid.
     * The key to access a point is an array where every component is the index of the point
     * in the respective dimension (e.g. access to point made by the product quantization of
     * the 7th point of the first grid, the 10th point of the second grid and the 3th point of
     * the third grid >> key = [7, 10, 3])
     * 
     * @param first Index of the point in the first dimension
     * @param second Index of the point in the second dimension
     * @param third Index of the point in the second dimension
     * 
     * @return
     */
	
    public double[] get(int first, int second, int third) {
        for(Map.Entry<int[], double[]> element : this.set.entrySet()) {
            if(element.getKey()[0] == first && element.getKey()[1] == second && element.getKey()[2] == third) {
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
    
    
    @Override
    public double[] getBoundaries(int[] quantizer, char ul) {
    	
    	double[] boundaries = {grids.get(1).getBoundary(quantizer[0], ul),
    						   grids.get(2).getBoundary(quantizer[1], ul),
    	                       grids.get(3).getBoundary(quantizer[2], ul)};
 
    	return boundaries;    	
    }


	@Override
	public double getBoundariesOf(int[] quantizer, char ul, int process) {

		return getBoundaries(quantizer,ul)[process-1];
	}


	@Override
	public HashMap<double[], Double> getDistribution() {
		
		if(this.distribution == null) {
			throw new IllegalArgumentException("Distribution has not been set");
		}
		return this.distribution;
	}
	


	@Override
	public HashMap<Integer, OneDimStandardNormalGrid> getGrids() {

		return this.grids;
	}

	
	@Override
	public int getNumberOfProcess() {

		return this.numberOfProcess;
	}
	

	@Override
	public int getGridSize(int grid) {

		return this.grids.get(grid).getQuantizerSize();
	}




	@Override
	public double[] get(int... point) {
		
		return null;
	}
    
}