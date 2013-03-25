package nl.esciencecenter.esight.datastructures;

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.textures.RBOTexture;
import nl.esciencecenter.esight.textures.Texture2D;


public class FBO {
    private final IntBuffer  fboPointer;
    private final IntBuffer  rboPointer;
    private final RBOTexture rboTexture;

    private final int        width, height;
    private boolean          initialized = false;

    public FBO(int width, int height, int glMultitexUnit) {
        this.width = width;
        this.height = height;

        fboPointer = IntBuffer.allocate(1);
        rboPointer = IntBuffer.allocate(1);

        rboTexture = new RBOTexture(width, height, glMultitexUnit);
    }

    public void init(GL3 gl) {
        try {
            checkNoError(gl, "PRE: ", true);

            // Create and bind frame buffer
            gl.glGenFramebuffers(1, fboPointer);
            gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fboPointer.get(0));

            // Setup texture and color buffer
            rboTexture.init(gl);
            rboTexture.use(gl);
            gl.glFramebufferTexture2D(GL3.GL_FRAMEBUFFER,
                    GL3.GL_COLOR_ATTACHMENT0, GL3.GL_TEXTURE_2D,
                    rboTexture.getPointer(), 0);
            rboTexture.unBind(gl);

            // Setup the depth buffer
            gl.glGenRenderbuffers(1, rboPointer);
            gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, rboPointer.get(0));
            gl.glRenderbufferStorage(GL.GL_RENDERBUFFER,
                    GL3.GL_DEPTH_COMPONENT16, width, height);
            gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, 0);

            // Attach both buffers to the frame buffer
            gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER,
                    GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D,
                    rboTexture.getPointer(), 0);
            gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER,
                    GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER,
                    rboPointer.get(0));

            checkNoError(gl, "POST: ", false);

            checkStatus(gl, "Framebuffer error:");
        } catch (UninitializedException e) {
            e.printStackTrace();
        }

        // Unbind. The FBO is now ready for use.
        gl.glBindFramebuffer(GL3.GL_FRAMEBUFFER, 0);

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

    private void checkStatus(GL3 gl, String prefix) {
        boolean errDetected = false;
        String errString = prefix + " ";
        // Check for errors
        int status = gl.glCheckFramebufferStatus(GL3.GL_FRAMEBUFFER);
        if (status != GL3.GL_FRAMEBUFFER_COMPLETE) {
            errDetected = true;
            if (status == GL3.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT) {
                errString += "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT.";
            }
            if (status == GL3.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS) {
                errString += "GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS.";
            }
            if (status == GL3.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT) {
                errString += "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT.";
            }
            if (status == GL3.GL_FRAMEBUFFER_UNSUPPORTED) {
                errString += "GL_FRAMEBUFFER_UNSUPPORTED.";
            }
        }

        if (errDetected) {
            System.err.println(errString);
        }
    }

    public void bind(GL3 gl) throws UninitializedException {
        if (initialized) {
            rboTexture.use(gl);
            gl.glBindFramebuffer(GL3.GL_FRAMEBUFFER, fboPointer.get(0));
        } else {
            throw new UninitializedException("FBO not initialized.");
        }
    }

    public Texture2D getTexture() {
        return rboTexture;
    }

    public void unBind(GL3 gl) {
        // gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, 0);
        gl.glBindFramebuffer(GL3.GL_FRAMEBUFFER, 0);
        // gl.glDrawBuffer(GL3.GL_BACK);
    }

    public void delete(GL3 gl) {
        gl.glDeleteRenderbuffers(1, rboPointer);
        gl.glDeleteFramebuffers(1, fboPointer);
    }

    public IntBuffer getPointer() {
        return fboPointer;
    }
}
