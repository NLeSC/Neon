package nl.esciencecenter.neon;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import nl.esciencecenter.neon.input.InputHandler;
import nl.esciencecenter.neon.util.QuitListener;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;

/* Copyright 2013 Netherlands eScience Center
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
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
 * Common (extendible) class for a stand alone native supported OpenGL window.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class NeonNewtWindow {
    /**
     * Constructor for this class. Sets up the window and enables common
     * features like anti-aliasing and hardware acceleration.
     * 
     * @param forceGL2ES2
     *            Force GL2ES2 support (default on), currently Unused
     * @param inputHandler
     *            A predefined InputHandler that is added as event handler for
     *            input events.
     * @param glEventListener
     *            A predefined GLEventListener that is added as event handler
     *            for openGL events.
     * @param width
     *            The initial window width.
     * @param height
     *            The initial window height.
     * @param windowTitle
     *            The window title.
     */
    public NeonNewtWindow(boolean forceGL2ES2, InputHandler inputHandler, final GLEventListener glEventListener,
            int width, int height, String windowTitle) {

        GLProfile.initSingleton();

        final GLProfile glp;
        glp = GLProfile.get(GLProfile.GL3);

        // Set up the GL context
        final GLCapabilities caps = new GLCapabilities(glp);
        caps.setBackgroundOpaque(true);
        caps.setHardwareAccelerated(true);
        caps.setDoubleBuffered(true);

        // Add Anti-Aliasing
        caps.setSampleBuffers(true);
        caps.setAlphaBits(4);
        caps.setNumSamples(4);

        GLWindow window = GLWindow.create(caps);

        window.addGLEventListener(glEventListener);
        window.addWindowListener(new QuitListener());
        window.setAutoSwapBufferMode(true);
        window.setSize(width, height);
        window.setTitle(windowTitle);
        window.addMouseListener(inputHandler);
        window.addKeyListener(inputHandler);

        Animator animator = new Animator();
        animator.add(window);
        animator.start();
        animator.setUpdateFPSFrames(60, null);

        window.setVisible(true);

    }
}
