package com.metsci.glimpse.axis.painter.label;

import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.axis.LogarithmicAxis1D;

/**
 * Created by alicana on 04/10/2016.
 */
public class LogarithmicLabelHandler extends GridAxisLabelHandler {

	@Override
	protected String tickString(Axis1D axis, double number, int orderAxis) {

		if (axis instanceof LogarithmicAxis1D) {
			LogarithmicAxis1D logarithmicAxis1D = (LogarithmicAxis1D) axis;
//			return String.valueOf(logarithmicAxis1D.getLogBase()) + "^" + number;
			return String.valueOf(Math.pow(logarithmicAxis1D.getLogBase(), number)) ;
		}
		return super.tickString(axis, number, orderAxis);
	}

	@Override
	protected double tickInterval(Axis1D axis, double approxNumTicks) {
		double calculatedMin = converter.toAxisUnits(axis.getMin());
		double calculatedMax = converter.toAxisUnits(axis.getMax());
		double min = Math.min(calculatedMin, calculatedMax);
		double max = Math.max(calculatedMin, calculatedMax);
		double approxTickInterval = (max - min) / approxNumTicks;

		// FIXME In logarithmic graph we just want to show power of logBase
		if (Double.compare(approxTickInterval, 1) < 0 ) return 1;

		double prelimTickInterval = pow(10, round(log10(approxTickInterval)));
		double prelimNumTicks = (max - min) / prelimTickInterval;

		if (prelimNumTicks >= 5 * approxNumTicks)
			return prelimTickInterval * 5;
		if (prelimNumTicks >= 2 * approxNumTicks)
			return prelimTickInterval * 2;

		if (5 * prelimNumTicks <= approxNumTicks)
			return prelimTickInterval / 5;
		if (2 * prelimNumTicks <= approxNumTicks)
			return prelimTickInterval / 2;

		return prelimTickInterval;
	}
}
