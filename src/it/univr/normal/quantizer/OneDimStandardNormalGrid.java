package it.univr.normal.quantizer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class OneDimStandardNormalGrid {
	
	private HashMap<Integer, Double> grid = new HashMap<Integer,Double>();
	public boolean hasCostantValues;
	
	
	/**
	 * Upload a one-dimensional grid of size N of a standard normal random
	 * variable. The points are in ascending order.
	 * 
	 * @param N the number of points
	 * 
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public OneDimStandardNormalGrid(int N) throws IOException, InvalidFormatException {
	
		final int pointscell = 1;
		final String XLSX_FILE_PATH = "grids/" + Integer.toString(N) + 
									  "_1" + 
									  ".xlsx";  
		
	    Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
	    Sheet sheet = workbook.getSheetAt(0);
	            
	    HashMap<Integer, Double> grid = new HashMap<Integer, Double>();
	    
	    for(int i = 0; i < N; i++) {
	    	grid.put(i + 1 ,sheet.getRow(i).getCell(pointscell).getNumericCellValue());
	    			}     
		this.grid = grid;
	}
	
	public OneDimStandardNormalGrid() {
		
	}
	
	
	/**
	 * Quantization grid for a one-dimensional standard normal random variable
	 *  
	 * @return HashMap<Integer, Double> where the keys are the indexes of the points of interest 
	 * and the values are the respective points. The mapping is non-decreasing.
	 */
	public HashMap<Integer, Double> getGrid() {
		return grid;
	}
	
	
	/**
	 * The size of the grid
	 * 
	 * @return N
	 */
	public int getQuantizerSize() {
		
		return grid.size();
	}
	

	/**
	 * Return the bound (upper or lower) of a given point.
	 * Set ul param to '+' for the upper bound, '-' for the lower
	 * 
	 * @param quantizer the point of interest
	 * @param ul
	 * @return the upper or lower bound
	 */
	public double getBoundary(int quantizer, char ul) {

		switch(ul) {
			case '+':
				if(quantizer == this.getQuantizerSize()) {
					return Double.POSITIVE_INFINITY;
				} else {
					return (this.grid.get(quantizer) + this.grid.get(quantizer + 1))/2;
				}
			case '-':
				if(quantizer == 1) {
					return Double.NEGATIVE_INFINITY;
				} else {
					return (this.grid.get(quantizer) + this.grid.get(quantizer - 1))/2;
				}
			default:
				throw new IllegalArgumentException("Point not found");
		}
	}
	
	
	/**
	 * Return the companion weight of the point of interest
	 * 
	 * @param quantizer
	 * @return companion weight of the point
	 */
	public double getWeight(int quantizer) {
		
		if(quantizer < 1 || quantizer > this.getQuantizerSize()) {
			throw new IllegalArgumentException("Point not found");
		}
	NormalDistribution x = new NormalDistribution();
	double prob = x.cumulativeProbability(getBoundary(quantizer,'+')) -
				  x.cumulativeProbability(getBoundary(quantizer,'-'));
	
	return prob;
	}
	
	public double getQuantizer(int quantizer) {
		
		
		  if(quantizer < 1) { return this.grid.get(1) - 0; } if(quantizer >
		  this.getQuantizerSize()) { return this.grid.get(this.getQuantizerSize()) + 0;
		  }
		 
		return this.grid.get(quantizer);
	}

	public static OneDimStandardNormalGrid buildWithCostantValues(double value, int N) {
		HashMap<Integer, Double> grid = new HashMap<Integer, Double>();
		
		for(int i = 1; i <= N; i++) {
	    	grid.put(i,value);
	    			}   
		OneDimStandardNormalGrid costant = new OneDimStandardNormalGrid();
		
		costant.grid = grid;
		costant.hasCostantValues = true;
		return costant;
		
	}
}
	
