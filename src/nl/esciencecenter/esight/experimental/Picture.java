package nl.esciencecenter.esight.experimental;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.media.opengl.GLException;

import com.jogamp.opengl.util.awt.Screenshot;

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
 * Experimental class, use at your own risk.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Picture {
    private final int width, height;

    public Picture(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Picture(ByteBuffer pixels, int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void copyFrameBufferToFile(String path, String fileName) {
        File newDir = new File(path + "screenshots");
        if (!newDir.exists())
            newDir.mkdir();

        String bareName = path + "screenshots/" + fileName;

        File newFile = new File(bareName + ".png");
        try {
            int attempt = 1;
            while (newFile.exists()) {
                String newName = bareName + " (" + attempt + ").png";
                newFile = new File(newName);

                attempt++;
            }

            System.out.println("Writing screenshot: " + newFile.getAbsolutePath());

            Screenshot.writeToFile(newFile, width, height);
            // }
        } catch (GLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private BufferedImage transformPixelsRGBBuffer2ARGB_ByHand(ByteBuffer pixelsRGB) {
        // Transform the ByteBuffer and get it as pixeldata.

        int[] pixelInts = new int[width * height];

        // Convert RGB bytes to ARGB ints with no transparency.
        // Flip image vertically by reading the
        // rows of pixels in the byte buffer in reverse
        // - (0,0) is at bottom left in OpenGL.
        //
        // Points to first byte (red) in each row.
        int p = width * height * 3;
        int q; // Index into ByteBuffer
        int i = 0; // Index into target int[]
        int w3 = width * 3; // Number of bytes in each row
        for (int row = 0; row < height; row++) {
            p -= w3;
            q = p;
            for (int col = 0; col < width; col++) {
                int iR = pixelsRGB.get(q++);
                int iG = pixelsRGB.get(q++);
                int iB = pixelsRGB.get(q++);
                pixelInts[i++] = 0xFF000000 | ((iR & 0x000000FF) << 16) | ((iG & 0x000000FF) << 8) | (iB & 0x000000FF);
            }
        }

        // Create a new BufferedImage from the pixeldata.
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, width, height, pixelInts, 0, width);

        return bufferedImage;
    }
}
