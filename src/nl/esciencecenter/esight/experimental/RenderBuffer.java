package nl.esciencecenter.esight.experimental;

import java.nio.IntBuffer;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.datastructures.FBO;
import nl.esciencecenter.esight.exceptions.UninitializedException;

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
 * Experimental class, use at your own risk. Renderbuffer data construct for
 * Renderbuffer and Framebuffer objects.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public class RenderBuffer {
    private final IntBuffer pointer = IntBuffer.allocate(1);
    private final int width;
    private final int height;

    /**
     * Basic constructor for RenderBuffer.
     * 
     * @param gl
     *            The OpenGL instance
     * @param width
     *            The width of the internal storage.
     * @param height
     *            The height of the internal storage.
     */
    public RenderBuffer(GL3 gl, int width, int height) {
        this.width = width;
        this.height = height;
        gl.glGenRenderbuffers(1, pointer);
    }

    /**
     * @param gl
     *            The OpenGL instance
     * @param fbo
     */
    public void attach(GL3 gl, FBO fbo) {
        try {
            fbo.bind(gl);
        } catch (UninitializedException e) {
            e.printStackTrace();
        }
        bind(gl);
        gl.glRenderbufferStorage(GL3.GL_RENDERBUFFER, GL3.GL_RGBA, width, height);
        gl.glFramebufferRenderbuffer(GL3.GL_FRAMEBUFFER, GL3.GL_RGBA, GL3.GL_RENDERBUFFER, pointer.get(0));
        unBind(gl);
        fbo.unBind(gl);
    }

    /**
     * GL bind method (prepare for use).
     * 
     * @param gl
     *            The OpenGL instance
     */
    public void bind(GL3 gl) {
        gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, pointer.get(0));
    }

    /**
     * GL Unbind method.
     * 
     * @param gl
     *            The OpenGL instance
     */
    public void unBind(GL3 gl) {
        gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, 0);
    }

    /**
     * @param gl
     *            The OpenGL instance
     */
    public void delete(GL3 gl) {
        gl.glDeleteRenderbuffers(1, pointer);
    }

    /**
     * Getter for the pointer to this Renderbufffer
     * 
     * @return The pointer.
     */
    public IntBuffer getPointer() {
        return pointer;
    }

}
