package it.univr.model.parameters;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

	public class PseudoCEVParameterFunction implements ParameterFunctionInterface{
		
		private double r;
		private double theta;
		private double delta;
		
		private ArrayRealVector drift = new ArrayRealVector(1);
		private Array2DRowRealMatrix diffusion = new Array2DRowRealMatrix(1,1);
		
		public PseudoCEVParameterFunction(double r, double theta, double delta) {
			
			this.r = r;
			this.theta = theta;
			this.delta = delta;
		}

		@Override
		public RealVector getDriftValue(double[] stateVariable, double time) {
			this.drift.setEntry(0, this.r*stateVariable[0]);
			
			return drift;
		}

		@Override
		public Array2DRowRealMatrix getDiffusionValue(double[] stateVariable, double time) {

			this.diffusion.setEntry(0, 0, this.theta*(Math.pow(stateVariable[0],this.delta+1)/
					(Math.sqrt(1+Math.pow(stateVariable[0], 2)))
					));

			return diffusion;
		}
		
		
		
	}
