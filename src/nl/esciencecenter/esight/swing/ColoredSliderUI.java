package nl.esciencecenter.esight.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * Extension of the basic slider UI to allow for colored thumbs.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class ColoredSliderUI extends BasicSliderUI {
    Color thumbColor;

    /**
     * Default {@link JSlider} constructor with added color value.
     * 
     * @param s
     *            the JSlider to use this UI on.
     * @param c
     *            the color to use fro the thumbs.
     */
    public ColoredSliderUI(final JSlider s, final Color c) {
        super(s);

        thumbColor = c;

    }

    @Override
    public void paint(Graphics g, JComponent c) {
        recalculateIfInsetsChanged();
        recalculateIfOrientationChanged();
        Rectangle clip = g.getClipBounds();

        if (slider.getPaintTrack() && clip.intersects(trackRect)) {
            paintTrack(g);
        }
        if (slider.getPaintTicks() && clip.intersects(tickRect)) {
            paintTicks(g);
        }
        if (slider.getPaintLabels() && clip.intersects(labelRect)) {
            paintLabels(g);
        }
        if (slider.hasFocus() && clip.intersects(focusRect)) {
            paintFocus(g);
        }
        if (clip.intersects(thumbRect)) {
            Color savedColor = slider.getBackground();
            slider.setBackground(thumbColor);
            paintThumb(g);
            slider.setBackground(savedColor);
        }
    }

    @Override
    public void paintTrack(Graphics g) {
        Rectangle trackBounds = trackRect;

        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            int cy = (trackBounds.height / 2) - 2;
            int cw = trackBounds.width;

            g.translate(trackBounds.x, trackBounds.y + cy);

            g.setColor(getShadowColor());
            g.drawLine(0, 0, cw - 1, 0);
            g.drawLine(0, 1, 0, 2);
            g.setColor(getHighlightColor());
            g.drawLine(0, 3, cw, 3);
            g.drawLine(cw, 0, cw, 3);
            g.setColor(Color.red);
            g.fillRect(1, 1, cw - 2, 4);
            // g.drawLine(1, 1, cw - 2, 1);

            g.translate(-trackBounds.x, -(trackBounds.y + cy));
        } else {
            int cx = (trackBounds.width / 2) - 2;
            int ch = trackBounds.height;

            g.translate(trackBounds.x + cx, trackBounds.y);

            g.setColor(getShadowColor());
            g.drawLine(0, 0, 0, ch - 1);
            g.drawLine(1, 0, 2, 0);
            g.setColor(getHighlightColor());
            g.drawLine(3, 0, 3, ch);
            g.drawLine(0, ch, 3, ch);
            g.setColor(Color.red);
            g.fillRect(1, 1, 4, ch - 2);
            // g.drawLine(1, 1, 1, ch - 2);

            g.translate(-(trackBounds.x + cx), -trackBounds.y);
        }
    }

    @Override
    public int valueForXPosition(int pos) {
        return super.valueForXPosition(pos);
    }
}