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
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;

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
    public ESightNewtWindow(boolean forceGL2ES2, InputHandler inputHandler, final GLEventListener glEventListener, int width,
            int height, String windowTitle) {
        final GLProfile glp;
        // if (forceGL2ES2) {
        // glp = GLProfile.get(GLProfile.GL2ES2);
        // } else {
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
        Display dpy = NewtFactory.createDisplay(null);
        Screen screen = NewtFactory.createScreen(dpy, screenIdx);
        final GLWindow glWindow = GLWindow.create(screen, caps);

        glWindow.setTitle(windowTitle);

        // Add listeners
        glWindow.addMouseListener(inputHandler);
        glWindow.addKeyListener(inputHandler);
        // glWindow.setFullscreen(true);

        WindowListener[] listeners = glWindow.getWindowListeners();
        final WindowListener original = listeners[0];
        glWindow.addWindowListener(0, new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent arg0) {
                glWindow.getAnimator().stop();
                System.exit(0);
            }

            @Override
            public void windowDestroyed(WindowEvent arg0) {
                glWindow.getAnimator().stop();
                System.exit(0);
            }

            @Override
            public void windowGainedFocus(WindowEvent arg0) {
                original.windowGainedFocus(arg0);
            }

            @Override
            public void windowLostFocus(WindowEvent arg0) {
                original.windowLostFocus(arg0);
            }

            @Override
            public void windowMoved(WindowEvent arg0) {
                original.windowMoved(arg0);
            }

            @Override
            public void windowRepaint(WindowUpdateEvent arg0) {
                original.windowRepaint(arg0);
            }

            @Override
            public void windowResized(WindowEvent arg0) {
                original.windowResized(arg0);
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
