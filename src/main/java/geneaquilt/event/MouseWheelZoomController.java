/**
 * Copyright (c) 2010-2014, Jean-Daniel Fekete, Pierre Dragicevic, and INRIA.
 * All rights reserved.
 *
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package geneaquilt.event;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PZoomEventHandler;

/**
 * Class MouseWheelZoomController
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public class MouseWheelZoomController extends PZoomEventHandler {
    /**
     * {@inheritDoc}
     */
    public void mouseWheelRotated(PInputEvent aEvent) {
        PCamera camera = aEvent.getCamera();
        double scaleDelta = (1.0 - (0.1 * aEvent.getWheelRotation()));

        double currentScale = camera.getViewScale();
        double newScale = currentScale * scaleDelta;
        
        if (newScale < getMinScale()) {
            scaleDelta = getMinScale() / currentScale;
        }
        if ((getMaxScale() > 0) && (newScale > getMaxScale())) {
            scaleDelta = getMaxScale() / currentScale;
        }

        camera.scaleViewAboutPoint(
                scaleDelta, 
                aEvent.getPosition().getX(), 
                aEvent.getPosition().getY());
    }
}
