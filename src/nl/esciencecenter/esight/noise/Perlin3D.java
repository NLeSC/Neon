package nl.esciencecenter.esight.noise;

import nl.esciencecenter.esight.textures.Texture3D;

/**
 * 3-Dimensional Perlin Noise Texture.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Perlin3D extends Texture3D {

    /**
     * Generates a 3D blob of Perlin Noise.
     * 
     * Do not forget to call {@link #init(javax.media.opengl.GL3)} before use.
     * 
     * @param glMultitexUnit
     *            The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture
     *            uses.
     * @param width
     *            The width of this texture.
     * @param height
     *            The height of this texture.
     * @param depth
     *            The depth of this texture.
     */
    public Perlin3D(int gLMultiTexUnit, int width, int height, int depth) {
        super(gLMultiTexUnit);

        this.width = width;
        this.height = height;
        this.depth = depth;

        // System.out.print("Generating noise");
        Noise n = new Noise(4, width, height, depth);
        // System.out.println("done");

        pixelBuffer = n.pixelBuffer;
    }
}
