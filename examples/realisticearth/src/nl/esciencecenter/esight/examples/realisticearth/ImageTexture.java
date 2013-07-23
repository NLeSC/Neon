package nl.esciencecenter.esight.examples.realisticearth;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import nl.esciencecenter.esight.textures.Texture2D;

/* Copyright 2013 Netherlands eScience Center
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
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
    /**
     * Constructor for this Texture. Reads the file designated by fileName. Do
     * not forget to call {@link #init(javax.media.opengl.GL3)} before use.
     * 
     * @param filename
     *            The image file to be read.
     * @param w_offSet
     *            Optional width offset in the image file.
     * @param h_offSet
     *            Optional height offset in the image file.
     * @param glMultiTexUnit
     *            The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture
     *            uses.
     */
    public ImageTexture(String filename, int w_offSet, int h_offSet, int glMultiTexUnit) {
        super(glMultiTexUnit);

        // Read the file
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int x = 0, y = 0, width = bi.getWidth(), height = bi.getHeight();

        this.width = width;
        this.height = height;

        // Grab pixels
        int[] pixels = new int[width * height];
        PixelGrabber pg = new PixelGrabber(bi, x, y, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("interrupted waiting for pixels!");
            return;
        }

        // Allocate ByteBuffer and fill it with pixel data.
        ByteBuffer tempBuffer = ByteBuffer.allocate(width * height * 4);

        for (int row = h_offSet; row < height; row++) {
            for (int col = width - w_offSet - 1; col >= 0; col--) {
                tempBuffer.put((byte) ((pixels[row * width + col] >> 16) & 0xff)); // red
                tempBuffer.put((byte) ((pixels[row * width + col] >> 8) & 0xff)); // green
                tempBuffer.put((byte) ((pixels[row * width + col]) & 0xff)); // blue
                tempBuffer.put((byte) ((pixels[row * width + col] >> 24) & 0xff)); // alpha
            }
        }

        tempBuffer.rewind();

        pixelBuffer = tempBuffer;
    }
}