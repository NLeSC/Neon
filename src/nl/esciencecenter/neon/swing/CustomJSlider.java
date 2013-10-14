package nl.esciencecenter.neon.swing;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

/* Copyright 2013 Netherlands eScience Center
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * A {@link JSlider} customization that allows us to click the track and jump to
 * that position. Also allows for a second Thumb, for use in {@link RangeSlider}
 * s.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class CustomJSlider extends JSlider {
    private static final long serialVersionUID = -3067450096465148814L;

    /**
     * Default constructor for the {@link JSlider}, but overrides the normal
     * mouselisteners. Commonly used like:
     * "timeBar = new CustomJSlider(new BasicSliderUI(timeBar));"
     * 
     * @param ui
     *            the {@link BasicSliderUI} that you want to use for this
     *            slider.
     */
    public CustomJSlider(final BasicSliderUI ui) {
        ChangeListener[] cl = getChangeListeners();
        for (ChangeListener l : cl) {
            removeChangeListener(l);
        }

        MouseListener[] listeners = getMouseListeners();
        for (MouseListener l : listeners) {
            removeMouseListener(l);
        }
        setUI(ui);

        ColoredSliderUI.TrackListener tl = ui.new TrackListener() {
            // this is where we jump to absolute value of click
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                int value = ui.valueForXPosition(p.x);

                setValue(value);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            // disable check that will invoke scrollDueToClickInTrack
            @Override
            public boolean shouldScroll(int dir) {
                return false;
            }
        };
        addMouseListener(tl);
    }

    /**
     * utility method to set the upper value for {@link RangeSlider}s.
     * 
     * @param value
     *            the value for the upper Thumb.
     */
    public void setUpperValue(int value) {
        // Compute new extent.
        int lowerValue = getValue();
        // Set extent to set upper value.
        int newExtent = Math.min(Math.max(0, value - lowerValue), getMaximum() - lowerValue);
        setExtent(newExtent);
    }
}