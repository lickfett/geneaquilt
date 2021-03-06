/**
 * Copyright (c) 2010-2014, Jean-Daniel Fekete, Pierre Dragicevic, and INRIA.
 * All rights reserved.
 *
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
/*
 * Copyright (c) 2008-2010, Piccolo2D project, http://piccolo2d.org
 * Copyright (c) 1998-2008, University of Maryland
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * None of the name of the University of Maryland, the name of the Piccolo2D project, or the names of its
 * contributors may be used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package geneaquilt.nodes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PPaintContext;

/**
 * This is a simple node that draws a "3D" rectangle within the bounds of the
 * node. Drawing a 3D rectangle in a zooming environment is a little tricky
 * because if you just use the regular (Java2D) 3D rectangle, the 3D borders get
 * scaled, and that is ugly. This version always draws the 3D border at fixed 2
 * pixel width.
 * 
 * @author Ben Bederson
 */
public class PFlatRect extends PNode {
    private static final long serialVersionUID = 1L;
    private Color borderColor;
    private transient GeneralPath path = null;
    private transient Stroke stroke = new BasicStroke(1);

    /**
     * Constructs a simple P3DRect with empty bounds and a black stroke.
     */
    public PFlatRect() {
    }

    /**
     * Constructs a P3DRect with the provided bounds.
     * 
     * @param bounds bounds to assigned to the P3DRect
     */
    public PFlatRect(final Rectangle2D bounds) {
        this(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    /**
     * Constructs a P3DRect with the bounds provided.
     * 
     * @param x left of bounds
     * @param y top of bounds
     * @param width width of bounds
     * @param height height of bounds
     */
    public PFlatRect(final double x, final double y, final double width, final double height) {
        this();
        setBounds(x, y, width, height);
    }

    /**
     * Paints this rectangle with shaded edges. Making it appear to stand out of
     * the page as normal 3D buttons do.
     * 
     * @param paintContext context in which the paiting should occur
     */
    protected void paint(final PPaintContext paintContext) {
    	
    	int old_quality = paintContext.getRenderQuality();
    	paintContext.setRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);

    	
        // lazy init:
        if (stroke == null) {
            stroke = new BasicStroke(0);
        }
        if (path == null) {
            path = new GeneralPath();
        }

        final Graphics2D g2 = paintContext.getGraphics();

        final double x = getX();
        final double y = getY();
        final double width = getWidth()-1;
        final double height = getHeight()-1;
        final double scaleX = g2.getTransform().getScaleX();
        final double scaleY = g2.getTransform().getScaleY();
        final double dx = (float) (1.0 / scaleX);
        final double dy = (float) (1.0 / scaleY);
        final PBounds bounds = getBounds();

        g2.setPaint(getPaint());
        g2.fill(bounds);
        g2.setStroke(stroke);

        path.reset();
        path.moveTo((float) (x + width), (float) y);
        path.lineTo((float) x, (float) y);
        path.lineTo((float) x, (float) (y + height));
        g2.setPaint(borderColor);
        g2.draw(path);

        path.reset();
        path.moveTo((float) (x + width), (float) (y + dy));
        path.lineTo((float) (x + dx), (float) (y + dy));
        path.lineTo((float) (x + dx), (float) (y + height));
        g2.setPaint(borderColor);
        g2.draw(path);

        path.reset();
        path.moveTo((float) (x + width), (float) y);
        path.lineTo((float) (x + width), (float) (y + height));
        path.lineTo((float) x, (float) (y + height));
        g2.setPaint(borderColor);
        g2.draw(path);

        path.reset();
        path.moveTo((float) (x + width - dx), (float) (y + dy));
        path.lineTo((float) (x + width - dx), (float) (y + height - dy));
        path.lineTo((float) x, (float) (y + height - dy));
        g2.setPaint(borderColor);
        g2.draw(path);
        
    	paintContext.setRenderQuality(old_quality);

    }

    /**
     * Changes the paint that will be used to draw this rectangle. This paint is
     * used to shade the edges of the rectangle.
     * 
     * @param newPaint the color to use for painting this rectangle
     */
    public void setPaint(final Paint newPaint) {
        super.setPaint(newPaint);

        if (newPaint instanceof Color) {
            final Color color = (Color) newPaint;
            borderColor = color.darker().darker();
        }
        
    }
}
