package com.metsci.glimpse.examples.charts.slippy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.metsci.glimpse.charts.slippy.SlippyMapTilePainter;
import com.metsci.glimpse.charts.slippy.SlippyPainterFactory;
import com.metsci.glimpse.examples.Example;
import com.metsci.glimpse.layout.GlimpseLayout;
import com.metsci.glimpse.layout.GlimpseLayoutProvider;
import com.metsci.glimpse.plot.MultiAxisPlot2D;
import com.metsci.glimpse.util.geo.LatLonGeo;
import com.metsci.glimpse.util.geo.projection.GeoProjection;
import com.metsci.glimpse.util.geo.projection.TangentPlane;
import com.metsci.glimpse.util.units.Length;

public class SlippyTileExample implements GlimpseLayoutProvider {
    
    public static void main(String[] args) throws Exception {
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        SlippyTileExample slippy = new SlippyTileExample();
        Example example = Example.showWithSwing(slippy);
        example.getFrame().setJMenuBar(slippy.mapToolBar);
    }

    private JMenuBar mapToolBar;

    @Override
    public GlimpseLayout getLayout() throws Exception {
        
        final GeoProjection geoProj = new TangentPlane(LatLonGeo.fromDeg(38.958374, -77.358548));
        final boolean inUS = true;
        
        final MultiAxisPlot2D mapPlot = new MultiAxisPlot2D();
        double rad = Length.fromKilometers(1);
        mapPlot.getCenterAxis().lockAspectRatioXY(1);
        mapPlot.getCenterAxis().set(-rad, rad, -rad, rad);
        mapPlot.getCenterAxisX().setMaxSpan(Length.fromKilometers(50));
        mapPlot.getCenterAxisY().setMaxSpan(Length.fromKilometers(50));

        Path userHome = Paths.get(System.getProperty("user.home"));
        Path cacheRoot = null;
        if (userHome != null && Files.exists(userHome)) {
            cacheRoot = userHome.resolve(".glimpse-slippy-cache");
        }
        
        Path mapImgCacheDir = cacheRoot != null ? cacheRoot.resolve("mapquest-map") : null;
        final SlippyMapTilePainter mapPainter = SlippyPainterFactory.getMapQuestMaps(geoProj, mapImgCacheDir);
        mapPlot.addPainter(mapPainter);
        
        Path satImgCacheDir = cacheRoot != null ? cacheRoot.resolve("mapquest-sat") : null;
        final SlippyMapTilePainter satPainter = SlippyPainterFactory.getMapQuestImagery(geoProj, satImgCacheDir, inUS);
        mapPlot.addPainter(satPainter);
        satPainter.setVisible(false);

        Path cartoLightImgCacheDir = cacheRoot != null ? cacheRoot.resolve("cartodb-light") : null;
        final SlippyMapTilePainter cartoLightPainter = SlippyPainterFactory.getCartoMapLight(geoProj, cartoLightImgCacheDir);
        mapPlot.addPainter(cartoLightPainter);
        cartoLightPainter.setVisible(false);

        Path cartoDarkImgCacheDir = cacheRoot != null ? cacheRoot.resolve("cartodb-dark") : null;
        final SlippyMapTilePainter cartoDarkPainter = SlippyPainterFactory.getCartoMapDark(geoProj, cartoDarkImgCacheDir);
        mapPlot.addPainter(cartoDarkPainter);
        cartoDarkPainter.setVisible(false);
        
        this.mapToolBar = new JMenuBar();
        ButtonGroup group = new ButtonGroup();
        final JRadioButton mapCheckBox = new JRadioButton("Map Layer", mapPainter.isVisible());
        final JRadioButton satcheckBox = new JRadioButton("Imagery Layer", satPainter.isVisible());
        final JRadioButton cartoLightCheckBox = new JRadioButton("CartoDBLight Layer", cartoLightPainter.isVisible());
        final JRadioButton cartoDarkcheckBox = new JRadioButton("CartoDBDark Layer", cartoDarkPainter.isVisible());
        
        group.add(mapCheckBox);
        group.add(satcheckBox);
        group.add(cartoLightCheckBox);
        group.add(cartoDarkcheckBox);
        
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapPainter.setVisible(mapCheckBox.isSelected());
                satPainter.setVisible(satcheckBox.isSelected());
                cartoLightPainter.setVisible(cartoLightCheckBox.isSelected());
                cartoDarkPainter.setVisible(cartoDarkcheckBox.isSelected());
            }
        };
        
        mapToolBar.add(mapCheckBox);
        mapToolBar.add(satcheckBox);
        mapToolBar.add(cartoLightCheckBox);
        mapToolBar.add(cartoDarkcheckBox);
        
        mapCheckBox.addActionListener(l);
        satcheckBox.addActionListener(l);
        cartoLightCheckBox.addActionListener(l);
        cartoDarkcheckBox.addActionListener(l);
        
        return mapPlot;
    }

}
