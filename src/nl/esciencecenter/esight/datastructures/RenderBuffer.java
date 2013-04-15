package nl.esciencecenter.esight.datastructures;


import java.nio.IntBuffer;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.datastructures.FBO;
import nl.esciencecenter.esight.exceptions.UninitializedException;


public class RenderBuffer {
    private final IntBuffer pointer = IntBuffer.allocate(1);
    private final int width;
    private final int height;

    public RenderBuffer(GL3 gl, int width, int height) {
        this.width = width;
        this.height = height;
        gl.glGenRenderbuffers(1, pointer);
    }

    public void attach(GL3 gl, FBO fbo) {
        try {
            fbo.bind(gl);
        } catch (UninitializedException e) {
            e.printStackTrace();
        }
        bind(gl);
        gl.glRenderbufferStorage(GL3.GL_RENDERBUFFER, GL3.GL_RGBA, width,
                height);
        gl.glFramebufferRenderbuffer(GL3.GL_FRAMEBUFFER, GL3.GL_RGBA,
                GL3.GL_RENDERBUFFER, pointer.get(0));
        unBind(gl);
        fbo.unBind(gl);
    }

    public void bind(GL3 gl) {
        gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, pointer.get(0));
    }

    public void unBind(GL3 gl) {
        gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, 0);
    }

    public void delete(GL3 gl) {
        gl.glDeleteRenderbuffers(1, pointer);
    }

    public IntBuffer getPointer() {
        return pointer;
    }

}
