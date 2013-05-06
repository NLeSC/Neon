package nl.esciencecenter.esight.swing;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

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
     * mouselisteners.
     * Commonly used like:
     * "timeBar = new CustomJSlider(new BasicSliderUI(timeBar));"
     * 
     * @param ui
     *            the {@link BasicSliderUI} that you want to use for this
     *            slider.
     */
    public CustomJSlider(final BasicSliderUI ui) {
        ChangeListener[] cl = getChangeListeners();
        for (ChangeListener l : cl)
            removeChangeListener(l); // remove UI-installed ChangeListeners

        MouseListener[] listeners = getMouseListeners();
        for (MouseListener l : listeners)
            removeMouseListener(l); // remove UI-installed TrackListener
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
        int newExtent = Math.min(Math.max(0, value - lowerValue), getMaximum()
                - lowerValue);
        setExtent(newExtent);
    }
}