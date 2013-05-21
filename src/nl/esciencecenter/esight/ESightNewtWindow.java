package nl.esciencecenter.esight;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import nl.esciencecenter.esight.input.InputHandler;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowUpdateEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;

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
 * Common (extendible) class for a stand alone native supported OpenGL window.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class ESightNewtWindow {
    /** Screen id number to start the application on. */
    static int screenIdx = 0;

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
    public ESightNewtWindow(boolean forceGL2ES2, InputHandler inputHandler, final GLEventListener glEventListener,
            int width, int height, String windowTitle) {
        final GLProfile glp;
        // if (forceGL2ES2) {
        // glp = GLProfile.get(GLProfile.GL2ES2);
        // } else {
        GLProfile.initSingleton();
        glp = GLProfile.get(GLProfile.GL3);
        // }

        // Set up the GL context
        final GLCapabilities caps = new GLCapabilities(glp);
        caps.setBackgroundOpaque(true);
        caps.setHardwareAccelerated(true);
        caps.setDoubleBuffered(true);

        // Add Anti-Aliasing
        caps.setSampleBuffers(true);
        caps.setAlphaBits(4);
        caps.setNumSamples(4);

        // Create the Newt Window
        Display dpy = NewtFactory.createDisplay("decon");
        Screen screen = NewtFactory.createScreen(dpy, screenIdx);
        final GLWindow glWindow = GLWindow.create(screen, caps);
        glWindow.setTitle(windowTitle);

        // Set to automatically swap front and back buffers once the display
        // cycle is done.
        glWindow.setAutoSwapBufferMode(true);

        // Add listeners
        glWindow.addMouseListener(inputHandler);
        glWindow.addKeyListener(inputHandler);
        // glWindow.setFullscreen(true);

        // WindowListener[] listeners = glWindow.getWindowListeners();
        // final WindowListener original = listeners[0];
        // glWindow.removeWindowListener(original);

        glWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent arg0) {
                // glWindow.getAnimator().stop();
                // original.windowDestroyNotify(arg0);
                System.exit(0);
            }

            @Override
            public void windowDestroyed(WindowEvent arg0) {
                // glWindow.getAnimator().stop();
                // original.windowDestroyed(arg0);
                // glWindow.destroy();
                System.exit(0);
            }

            @Override
            public void windowGainedFocus(WindowEvent arg0) {
                // original.windowGainedFocus(arg0);
            }

            @Override
            public void windowLostFocus(WindowEvent arg0) {
                // original.windowLostFocus(arg0);
            }

            @Override
            public void windowMoved(WindowEvent arg0) {
                // original.windowMoved(arg0);
            }

            @Override
            public void windowRepaint(WindowUpdateEvent arg0) {
                // original.windowRepaint(arg0);
            }

            @Override
            public void windowResized(WindowEvent arg0) {
                // original.windowResized(arg0);
            }
        });

        glWindow.addGLEventListener(glEventListener);

        // Create the Animator
        final Animator animator = new Animator();
        animator.add(glWindow);
        animator.start();

        glWindow.setSize(width, height);

        glWindow.setVisible(true);
    }
}
