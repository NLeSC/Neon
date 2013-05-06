package nl.esciencecenter.esight.textures;

import java.nio.ByteBuffer;

/**
 * Generic wrapper for a {@link Texture} made out of an (initially) empty
 * ByteBuffer.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class PostProcessTexture extends Texture2D {

    /**
     * Default constructor, wrapper for an (initially) empty ByteBuffer.
     * Do not forget to call {@link #init(javax.media.opengl.GL3)} before use.
     * 
     * @param glMultitexUnit
     *            The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture
     *            uses.
     * @param width
     *            The width of this texture.
     * @param height
     *            The height of this texture.
     */
    public PostProcessTexture(int width, int height, int gLMultiTexUnit) {
        super(gLMultiTexUnit);
        this.height = height;
        this.width = width;

        pixelBuffer = ByteBuffer.allocate(width * height * 4);
    }
}
