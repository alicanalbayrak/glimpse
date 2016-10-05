package com.metsci.glimpse.axis;

/**
 * Created by alicana on 03/10/2016.
 */
public class LogarithmicAxis1D extends Axis1D {

	private int logBase;
	private double minLogVal;
	private double maxLogVal;
	private double logOfBaseCache;

	public LogarithmicAxis1D(LogarithmicAxis1D axis) {
		this(axis, axis.getLogBase(), axis.getMinLogVal(), axis.getMaxLogVal());
	}

	// If not specified, default logarithm base is 10
	public LogarithmicAxis1D(double minLogVal, double maxLogVal) {
		this(null, 10, minLogVal, maxLogVal);
	}

	public LogarithmicAxis1D(int logBase, double minLogVal, double maxLogVal) {
		this(null, logBase, minLogVal, maxLogVal);
	}

	public LogarithmicAxis1D(Axis1D parent, int logBase, double minLogVal, double maxLogVal) {
		super(parent);

		this.minLogVal = minLogVal;
		this.maxLogVal = maxLogVal;

		setLogBase(logBase);
		setMin(getLogValue(minLogVal));
		setMax(getLogValue(maxLogVal));
	}

	public int getLogBase() {
		return logBase;
	}

	public void setLogBase(int logBase) {
		this.logBase = logBase;
		// may raise log base change event!
		this.logOfBaseCache = Math.log(logBase);
		setMin(getLogValue(minLogVal));
		setMax(getLogValue(maxLogVal));
	}

	public double getLogValue(double value) {
		// throw exception if value below 0
		return Double.compare(value, 0d) <= 0 ? 0d : Math.log(value) / logOfBaseCache;
	}

	public double getMinLogVal() {
		return minLogVal;
	}

	public void setMinLogVal(double minLogVal) {
		this.minLogVal = minLogVal;
	}

	public double getMaxLogVal() {
		return maxLogVal;
	}

	public void setMaxLogVal(double maxLogVal) {
		this.maxLogVal = maxLogVal;
	}

	public double findPreviousPowerOfNumber(double number) {
		return Math.pow(logBase, Math.floor(Math.log(number) / logOfBaseCache));
	}

	public double findNextPowerOfNumber(double number) {
		return Math.pow(logBase, Math.ceil(Math.log(number) / logOfBaseCache));
	}

	@Override
	public Axis1D clone() {
		return new LogarithmicAxis1D(this);
	}

	public static void main(String[] args) {

		LogarithmicAxis1D axis = new LogarithmicAxis1D(10, 0, 2046);
		//		System.err.println(axis.getLogValue(49));

		System.err.println("previous " + axis.findPreviousPowerOfNumber(5));
		System.err.println("next " + axis.findNextPowerOfNumber(5));

	}

}
