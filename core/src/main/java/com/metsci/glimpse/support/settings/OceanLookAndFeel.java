/*
 * Copyright (c) 2016, Metron, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Metron, Inc. nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL METRON, INC. BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.metsci.glimpse.support.settings;

import static com.metsci.glimpse.support.color.GlimpseColor.addRgb;
import static com.metsci.glimpse.support.color.GlimpseColor.getBlack;
import static com.metsci.glimpse.support.color.GlimpseColor.getWhite;
import static com.metsci.glimpse.support.font.FontUtils.getDefaultPlain;

import com.metsci.glimpse.support.color.GlimpseColor;

/**
 * A Glimpse LookAndFeel with a blue color scheme.
 * 
 * @author ulman
 */
public class OceanLookAndFeel extends AbstractLookAndFeel
{
    public OceanLookAndFeel( )
    {
        map.put( CROSSHAIR_COLOR, getBlack( ) );
        map.put( BORDER_COLOR, getWhite( ) );

        map.put( PLOT_BACKGROUND_COLOR, addRgb( GlimpseColor.fromColorRgb( 25, 42, 62 ), -0.08f ) );
        map.put( FRAME_BACKGROUND_COLOR, GlimpseColor.fromColorRgb( 25, 42, 62 ) );

        map.put( AXIS_TEXT_COLOR, getWhite( ) );
        map.put( AXIS_TICK_COLOR, getWhite( ) );
        map.put( AXIS_TAG_COLOR, getWhite( 0.2f ) );

        map.put( AXIS_FONT, getDefaultPlain( 11 ) );
        map.put( TITLE_FONT, getDefaultPlain( 14 ) );

        map.put( TOOLTIP_BACKGROUND_COLOR, getBlack( 0.7f ) );
        map.put( TOOLTIP_TEXT_COLOR, getWhite( ) );
    }
}
