package nl.esciencecenter.esight.datastructures;

import java.nio.Buffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL3;

import com.jogamp.common.nio.Buffers;

/**
 * A class representing a Vertex Buffer Object.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public class VBO {
    /** The internal OpenGL pointer to the VBO */
    private final IntBuffer vboPointer;

    /** The internal OpenGL pointer to the Array Buffer */
    private final IntBuffer bufferPointer;

    /** The array of GLSL attributes associated with this VBO */
    private GLSLAttrib[]    attribs;

    /**
     * Constructor that creates a Vertex Buffer Object with the specified GLSL
     * attributes.
     * (typically location, texture coordinates, normals, etc.)
     * 
     * @param gl
     *            The global openGL instance.
     * @param attribs
     *            One or more attributes that represent this VBO, @see
     *            GLSLAttrib
     */
    public VBO(GL3 gl, GLSLAttrib... attribs) {
        this.attribs = attribs;

        // Generate a new internal OpenGL VBO pointer
        this.vboPointer = Buffers.newDirectIntBuffer(1);
        gl.glGenVertexArrays(1, this.vboPointer);
        gl.glBindVertexArray(this.vboPointer.get(0));

        // Generate a new internal OpenGL Array Buffer pointer
        this.bufferPointer = Buffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1, this.bufferPointer);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, this.bufferPointer.get(0));

        // Allocate enough memory
        int size = 0;
        for (final GLSLAttrib attrib : attribs) {
            size += attrib.buffer.capacity() * Buffers.SIZEOF_FLOAT;
        }

        gl.glBufferData(GL3.GL_ARRAY_BUFFER, size, (Buffer) null,
                GL3.GL_STATIC_DRAW);

        // Copy the GLSL Attribute data into the internal OpenGL buffer
        int nextStart = 0;
        for (final GLSLAttrib attrib : attribs) {
            gl.glBufferSubData(GL3.GL_ARRAY_BUFFER, nextStart,
                    attrib.buffer.capacity() * Buffers.SIZEOF_FLOAT,
                    attrib.buffer);
            nextStart += attrib.buffer.capacity() * Buffers.SIZEOF_FLOAT;
        }
    }

    /**
     * Bind the VBO, so that it is ready for use.
     * 
     * @param gl
     *            The global openGL instance.
     */
    public void bind(GL3 gl) {
        gl.glBindVertexArray(this.vboPointer.get(0));
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, bufferPointer.get(0));

    }

    /**
     * Delete this VBO properly.
     * 
     * @param gl
     *            The global openGL instance.
     */
    public void delete(GL3 gl) {
        gl.glBindVertexArray(0);
        gl.glDeleteVertexArrays(1, this.vboPointer);
        gl.glDeleteBuffers(1, this.bufferPointer);
    }

    /**
     * Retrieve the GLSL Attributes that make up this VBO
     * 
     * @return The GLSL Attributes
     */
    public GLSLAttrib[] getAttribs() {
        return attribs;
    }

    /**
     * Update this VBO with (potentially) new data.
     * 
     * @param gl
     *            The global openGL instance.
     * @param attribs
     *            One or more attributes that represent this VBO, @see
     *            GLSLAttrib
     */
    public void update(GL3 gl, GLSLAttrib... attribs) {
        this.attribs = attribs;

        gl.glBindVertexArray(this.vboPointer.get(0));
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, this.bufferPointer.get(0));

        // Allocate enough memory
        int size = 0;
        for (final GLSLAttrib attrib : attribs) {
            size += attrib.buffer.capacity() * Buffers.SIZEOF_FLOAT;
        }

        gl.glBufferData(GL3.GL_ARRAY_BUFFER, size, (Buffer) null,
                GL3.GL_STATIC_DRAW);

        // Copy the GLSL Attribute data into the internal OpenGL buffer
        int nextStart = 0;
        for (final GLSLAttrib attrib : attribs) {
            gl.glBufferSubData(GL3.GL_ARRAY_BUFFER, nextStart,
                    attrib.buffer.capacity() * Buffers.SIZEOF_FLOAT,
                    attrib.buffer);
            nextStart += attrib.buffer.capacity() * Buffers.SIZEOF_FLOAT;
        }
    }
}
