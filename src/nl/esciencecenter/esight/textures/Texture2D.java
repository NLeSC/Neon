package nl.esciencecenter.esight.textures;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.UninitializedException;

import com.jogamp.common.nio.Buffers;

/**
 * 2-Dimensional {@link Texture} object representation. Provides some generic
 * methods and variables for all types of 2-Dimensional textures.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public abstract class Texture2D extends Texture {

    /**
     * Generic constructor, should be called by all classes extending this class
     * to set the glMultitexUnit.
     * Do not forget to call {@link #init(javax.media.opengl.GL3)} before use.
     * 
     * @param glMultitexUnit
     *            The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture
     *            uses.
     */
    public Texture2D(int glMultitexUnit) {
        super(glMultitexUnit);
    }

    /**
     * Initialization method generic for all 2-D Textures. Allocates space on
     * the device and copies data from the pixelBuffer into it. Therefore,
     * pixelBuffer cannot be null, or an error will be generated.
     * 
     * @param gl
     *            The current OpenGL instance.
     */
    public void init(GL3 gl) {
        if (!initialized) {
            if (pixelBuffer == null) {
                System.err
                        .println("Add a pixelbuffer first, by using a custom constructor. The Texture2D constructor is only meant to be extended.");
            }

            // Tell OpenGL we want to use 2D textures
            gl.glEnable(GL3.GL_TEXTURE_2D);
            gl.glActiveTexture(this.glMultiTexUnit);

            // Create a Texture Object
            pointer = Buffers.newDirectIntBuffer(1);
            gl.glGenTextures(1, pointer);

            // Tell OpenGL that this texture is 2D and we want to use it
            gl.glBindTexture(GL3.GL_TEXTURE_2D, pointer.get(0));

            // Wrap.
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_S,
                    GL3.GL_CLAMP_TO_EDGE);
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_T,
                    GL3.GL_CLAMP_TO_EDGE);
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER,
                    GL3.GL_LINEAR);
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAG_FILTER,
                    GL3.GL_LINEAR);

            // Specifies the alignment requirements for the start of each pixel
            // row in memory.
            gl.glPixelStorei(GL3.GL_UNPACK_ALIGNMENT, 1);

            gl.glTexImage2D(GL3.GL_TEXTURE_2D, 0, // Mipmap level.
                    GL3.GL_RGBA32F, // GL.GL_RGBA, // Internal Texel Format,
                    width, height, 0, // Border
                    GL3.GL_RGBA, // External format from image,
                    GL3.GL_UNSIGNED_BYTE, pixelBuffer // Imagedata as ByteBuffer
            );

            initialized = true;
        }
    }

    public void use(GL3 gl) throws UninitializedException {
        if (!initialized) {
            init(gl);
        }

        gl.glEnable(GL3.GL_TEXTURE_2D);
        gl.glActiveTexture(glMultiTexUnit);
        gl.glBindTexture(GL3.GL_TEXTURE_2D, getPointer());
    }

    public void unBind(GL3 gl) {
        gl.glBindTexture(GL3.GL_TEXTURE_2D, 0);
    }

    // public Texture2D copy(GL3 gl, int glMultitexUnit) {
    // Texture2D result = new Texture2D(glMultitexUnit);
    // result.pixelBuffer = pixelBuffer.duplicate();
    // result.init(gl);
    //
    // return result;
    // }

}