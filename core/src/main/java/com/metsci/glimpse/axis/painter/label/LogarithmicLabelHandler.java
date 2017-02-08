package com.metsci.glimpse.axis.painter.label;

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
			return String.valueOf(logarithmicAxis1D.getLogValue(number));
		}
		return super.tickString(axis, number, orderAxis);
	}

	@Override
	public String[] getTickLabels(Axis1D axis, double[] tickPositions) {

		if (axis instanceof LogarithmicAxis1D) {



		}

		return super.getTickLabels(axis, tickPositions);
	}

	@Override
	protected double[] tickPositions(Axis1D axis, double tickInterval) {


		if (axis instanceof LogarithmicAxis1D) {

		}

		return super.tickPositions(axis, tickInterval);
	}
}
