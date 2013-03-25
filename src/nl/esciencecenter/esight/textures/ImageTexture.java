package nl.esciencecenter.esight.textures;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

/**
 * Generic wrapper for a {@link Texture} read out of an image file.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public class ImageTexture extends Texture2D {
    /**
     * Constructor for this Texture. Reads the file designated by fileName.
     * Do not forget to call {@link #init(javax.media.opengl.GL3)} before use.
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
    public ImageTexture(String filename, int w_offSet, int h_offSet,
            int glMultiTexUnit) {
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
        PixelGrabber pg = new PixelGrabber(bi, x, y, width, height, pixels, 0,
                width);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("interrupted waiting for pixels!");
            return;
        }

        // Allocate ByteBuffer and fill it with pixel data.
        ByteBuffer tempBuffer = ByteBuffer.allocate(width * height * 4);

        for (int row = (height + h_offSet) - 1; row >= h_offSet; row--) {
            int i = row;
            if (row >= height) {
                i = row - height;
            }

            for (int col = w_offSet; col < (width + w_offSet); col++) {
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