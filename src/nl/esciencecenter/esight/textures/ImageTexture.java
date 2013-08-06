package nl.esciencecenter.esight.textures;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Generic wrapper for a {@link Texture} read out of an image file.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public class ImageTexture extends Texture2D {
    private final static Logger logger = LoggerFactory.getLogger(ImageTexture.class);

    /**
     * Constructor for this Texture. Reads the file designated by fileName. Do
     * not forget to call {@link #init(javax.media.opengl.GL3)} before use.
     * 
     * @param filename
     *            The image file to be read.
     * @param wOffSet
     *            Optional width offset in the image file.
     * @param hOffSet
     *            Optional height offset in the image file.
     * @param glMultiTexUnit
     *            The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture
     *            uses.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public ImageTexture(String filename, int wOffSet, int hOffSet, int glMultiTexUnit) throws FileNotFoundException,
            IOException {
        super(glMultiTexUnit);

        // Read the file
        BufferedImage bi = null;

        bi = ImageIO.read(new FileInputStream(filename));

        int x = 0, y = 0;

        if (bi != null) {
            this.width = bi.getWidth();
            this.height = bi.getHeight();

            // Grab pixels
            int[] pixels = new int[width * height];
            PixelGrabber pg = new PixelGrabber(bi, x, y, width, height, pixels, 0, width);
            try {
                pg.grabPixels();
            } catch (InterruptedException e) {
                logger.error("interrupted waiting for pixels!");
                return;
            }

            // Allocate ByteBuffer and fill it with pixel data.
            ByteBuffer tempBuffer = ByteBuffer.allocate(width * height * 4);

            for (int row = (height + hOffSet) - 1; row >= hOffSet; row--) {
                int i = row;
                if (row >= height) {
                    i = row - height;
                }

                for (int col = wOffSet; col < (width + wOffSet); col++) {
                    int j = col;
                    if (col >= width) {
                        j = col - width;
                    }

                    tempBuffer.put((byte) ((pixels[i * width + j]) & 0xff)); // blue
                    tempBuffer.put((byte) ((pixels[i * width + j] >> 8) & 0xff)); // green
                    tempBuffer.put((byte) ((pixels[i * width + j] >> 16) & 0xff)); // red
                    tempBuffer.put((byte) ((pixels[i * width + j] >> 24) & 0xff)); // alpha
                }
            }

            tempBuffer.rewind();

            pixelBuffer = tempBuffer;
        }
    }
}