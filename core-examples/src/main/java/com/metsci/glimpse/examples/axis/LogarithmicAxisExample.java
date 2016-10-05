package com.metsci.glimpse.examples.axis;

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.axis.LogarithmicAxis1D;
import com.metsci.glimpse.axis.painter.label.GridAxisLabelHandler;
import com.metsci.glimpse.axis.painter.label.LogarithmicLabelHandler;
import com.metsci.glimpse.examples.Example;
import com.metsci.glimpse.layout.GlimpseLayout;
import com.metsci.glimpse.layout.GlimpseLayoutProvider;
import com.metsci.glimpse.plot.SimplePlot2D;

/**
 * Created by alicana on 04/10/2016.
 */
public class LogarithmicAxisExample implements GlimpseLayoutProvider {
	@Override
	public GlimpseLayout getLayout() throws Exception {

		SimplePlot2D plot = new SimplePlot2D("name") {

			@Override
			protected Axis1D createAxisX() {
				return new LogarithmicAxis1D(7, 0, 282_475_249);
			}

			@Override
			protected GridAxisLabelHandler createLabelHandlerX() {
				return new LogarithmicLabelHandler();
			}
		};

		return plot;
	}

	public static void main(String[] args) throws Exception {
		Example.showWithSwing(new LogarithmicAxisExample());
	}
}
