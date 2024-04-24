package it.univr.normal.quantizer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class MultiDimNormalGrid {
		
	private HashMap<RealVector, Double> grid;
	
/**
* Upload a multi-dimensional grid of size N of a multivariate standard normal 
* random variable of dimension d
* 
* @param N the number of points
* @param d the dimension of the normal random variable
* 
* @throws IOException
* @throws InvalidFormatException
*/
public MultiDimNormalGrid(int N, int d) throws IOException, InvalidFormatException {
		
/*final String XLSX_FILE_PATH = "../../_download/_multi_dim/" + 
                              Integer.toString(N) + "_" +
                              Integer.toString(d) + 
                              ".xlsx";*/  
final String XLSX_FILE_PATH = "grids/"  +
        Integer.toString(N) + "_" +
        Integer.toString(d) + 
        ".xlsx";  


	Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
	Sheet sheet = workbook.getSheetAt(0);
	            
	HashMap<RealVector, Double> grid = new HashMap<RealVector, Double>();	    
	for(int i = 0; i < N; i++) {    	
	  double[] coordinates = new double[d];	    	
	    for(int j = 1; j <= d; j++) {
	      coordinates[j-1] = sheet.getRow(i).getCell(j).getNumericCellValue();
	    }
	      grid.put(new ArrayRealVector(coordinates),
	      sheet.getRow(i).getCell(0).getNumericCellValue());
	}	    
	this.grid = grid;
}

	/**
	 * Quantization grid for a multi-dimensional standard Normal distribution
	 * with associated companion weights
	 * 
	 * @return HashMap<RealVector, Double> where the key are the optimal points of the grid and the values
	 * are their companion weights
	 */
	public Set<Entry<RealVector, Double>> getGridSet() {
		return grid.entrySet();
	}

}
