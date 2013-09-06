package nl.esciencecenter.esight.swing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.swing.ImageIcon;

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
 * A single-color-filled {@link ImageIcon}
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class ColorIcon extends ImageIcon {
    private static final long serialVersionUID = 152208824875341752L;

    private BufferedImage image;
    private final int width, height;

    /**
     * Constructor for this colored rectangle.
     * 
     * @param width
     *            the width of this colored rectangle.
     * @param height
     *            the height of this colored rectangle.
     * @param color
     *            the color of this colored rectangle.
     */
    public ColorIcon(int width, int height, Color color) {
        super();

        this.width = width;
        this.height = height;

        makeImage(color);
    }

    /**
     * Utility method to make a {@link BufferedImage} square with color filled
     * interior. sets the {@link ImageIcon}'s intenal image field.
     * 
     * @param color
     *            the color.
     */
    private void makeImage(Color color) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        final ByteBuffer outBuf = ByteBuffer.allocate(width * height * 4);
        outBuf.clear();

        for (int i = 0; i < (width * height); i++) {
            outBuf.put((byte) 0xFF);
            outBuf.put((byte) (color.getRed()));
            outBuf.put((byte) (color.getGreen()));
            outBuf.put((byte) (color.getBlue()));
        }

        outBuf.rewind();

        final int[] tmp = new int[width * height];
        outBuf.asIntBuffer().get(tmp);
        image.setRGB(0, 0, width, height, tmp, 0, 1);

        setImage(image);
    }
}
