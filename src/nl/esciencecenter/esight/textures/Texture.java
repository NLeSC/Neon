package nl.esciencecenter.esight.textures;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.UninitializedException;

/**
 * Texture object representation. Provides some generic methods and variables
 * for all types of textures.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public abstract class Texture {
    /** The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture uses. */
    protected int        glMultiTexUnit;

    protected IntBuffer  pointer;
    protected ByteBuffer pixelBuffer;
    protected int        width, height, depth;
    protected boolean    initialized = false;

    /**
     * Generic constructor, should be called by all classes extending this class
     * to set the glMultitexUnit.
     * 
     * @param gLMultiTexUnit
     *            The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture
     *            uses.
     */
    public Texture(int gLMultiTexUnit) {
        this.glMultiTexUnit = gLMultiTexUnit;
    }

    /**
     * Getter for glMultitexUnit.
     * 
     * @return The OpenGL-internal GL_TEXTUREX this texture uses.
     */
    public int getGLMultiTexUnit() {
        return glMultiTexUnit;
    }

    /**
     * Getter for the NUMBER associated with this Texture's GlMultitexUnit.
     * 
     * @return this.glMultiTexUnit - GL.GL_TEXTURE0;
     */
    public int getMultitexNumber() {
        int result = glMultiTexUnit - GL.GL_TEXTURE0;

        return result;
    }

    /**
     * Static method that provides a method of calculating the GLMultitexUnit
     * represented by the given number.
     * 
     * @param val
     *            The number of the GLMultitexUnit you wish to receive.
     * @return val + GL.GL_TEXTURE0;
     */
    public static int toMultitexNumber(int val) {
        int result = val + GL.GL_TEXTURE0;

        return result;
    }

    /**
     * @return the pointer
     */
    public int getPointer() throws UninitializedException {
        if (pointer == null)
            throw new UninitializedException();
        return pointer.get(0);
    }

    /**
     * @param pointer
     *            the pointer to set
     */
    public void setPointer(IntBuffer pointer) {
        this.pointer = pointer;
    }

    /**
     * @return the pixelBuffer
     */
    public ByteBuffer getPixelBuffer() {
        return pixelBuffer;
    }

    /**
     * @param pixelBuffer
     *            the pixelBuffer to set
     */
    public void setPixelBuffer(ByteBuffer pixelBuffer) {
        this.pixelBuffer = pixelBuffer;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @param depth
     *            the depth to set
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * @param initialized
     *            the initialized to set
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * Deletes this Texture from memory.
     * 
     * @param gl
     *            The current OpenGL instance.
     */
    public void delete(GL3 gl) {
        gl.glDeleteTextures(1, pointer);
    }
}
