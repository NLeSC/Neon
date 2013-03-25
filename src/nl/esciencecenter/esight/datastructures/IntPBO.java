package nl.esciencecenter.esight.datastructures;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.UninitializedException;

public class IntPBO {
    private final IntBuffer pboPointer;

    private final int       width, height;
    private boolean         initialized = false;

    private ByteBuffer      data;

    public IntPBO(int width, int height) {
        this.width = width;
        this.height = height;

        pboPointer = IntBuffer.allocate(1);

        data = ByteBuffer.allocate(width * height * 4);
    }

    public void init(GL3 gl) {
        checkNoError(gl, "PRE: ", true);

        /* generate the pixel buffer object */
        gl.glGenBuffers(1, pboPointer);
        gl.glBindBuffer(GL3.GL_PIXEL_PACK_BUFFER, pboPointer.get(0));
        gl.glPixelStorei(GL3.GL_UNPACK_ALIGNMENT, 1);

        gl.glBufferData(GL3.GL_PIXEL_PACK_BUFFER, width * height * 4, null,
                GL3.GL_STREAM_READ);
        gl.glReadPixels(0, 0, width, height, GL3.GL_BGRA, GL3.GL_UNSIGNED_BYTE,
                0);

        checkNoError(gl, "POST: ", false);

        // Unbind. The FBO is now ready for use.
        gl.glBindBuffer(GL3.GL_PIXEL_PACK_BUFFER, 0);

        initialized = true;
    }

    private boolean checkNoError(GL gl, String exceptionMessage,
            boolean quietlyRemoveAllPreviousErrors) {
        int error = gl.glGetError();
        if (!quietlyRemoveAllPreviousErrors) {
            if (GL.GL_NO_ERROR != error) {
                System.err.println("GL ERROR(s) " + exceptionMessage + " : ");
                while (GL.GL_NO_ERROR != error) {
                    System.err.println(" GL Error 0x"
                            + Integer.toHexString(error));
                    error = gl.glGetError();
                }
                return false;
            }
        } else {
            while (GL.GL_NO_ERROR != error) {
                error = gl.glGetError();
            }
        }
        return true;
    }

    public void copyToPBO(GL3 gl) throws UninitializedException {
        if (initialized) {
            gl.glBindBuffer(GL3.GL_PIXEL_PACK_BUFFER, pboPointer.get(0));

            gl.glReadPixels(0, 0, width, height, GL3.GL_BGRA,
                    GL3.GL_UNSIGNED_BYTE, 0);
            data = gl.glMapBuffer(GL3.GL_PIXEL_PACK_BUFFER, GL3.GL_WRITE_ONLY);
        } else {
            throw new UninitializedException("PBO not initialized.");
        }
    }

    public ByteBuffer getBuffer() {
        return data;
    }

    public void unBind(GL3 gl) {
        gl.glUnmapBuffer(GL3.GL_PIXEL_PACK_BUFFER);
        gl.glBindBuffer(GL3.GL_PIXEL_PACK_BUFFER, 0);
    }

    public void delete(GL3 gl) {
        gl.glDeleteBuffers(1, pboPointer);
    }

    public IntBuffer getPointer() {
        return pboPointer;
    }
}
