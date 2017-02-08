package com.metsci.glimpse.axis;

/**
 * Created by alicana on 03/10/2016.
 */
public class LogarithmicAxis1D extends Axis1D {

	private int logBase;
	private double logOfBaseCache;

	// Default logBase 10 logarithm
	public LogarithmicAxis1D() {
		this(10);
	}

	public LogarithmicAxis1D(int logBase) {
		setLogBase(logBase);
	}

	public LogarithmicAxis1D(LogarithmicAxis1D logarithmicAxis1D) {
		super(logarithmicAxis1D);
		setLogBase(logarithmicAxis1D.getLogBase());
	}

	public int getLogBase() {
		return logBase;
	}

	public void setLogBase(int logBase) {
		this.logBase = logBase;
		// may raise event!
		this.logOfBaseCache = Math.log(logBase);
	}

	/**
	 * Logarithmic result of value in logBase
	 *
	 * @param value
	 * @return
	 */
	public double getLogValue(double value) {
		return Math.log(value) / logOfBaseCache;
	}

	@Override
	public Axis1D clone() {
		return new LogarithmicAxis1D(this);
	}

	public static void main(String[] args) {

		LogarithmicAxis1D logarithmicAxis1D = new LogarithmicAxis1D();
		logarithmicAxis1D.setLogBase(7);
		System.err.println(logarithmicAxis1D.getLogValue(49));

	}

}
