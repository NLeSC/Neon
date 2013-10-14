package nl.esciencecenter.neon.util;

import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;

public class QuitListener implements WindowListener {

    @Override
    public void windowDestroyNotify(WindowEvent arg0) {
        // We actually want this to exit the JVM, since it's the close button
        // we're talking about.
        System.exit(0); // NOSONAR
    }

    @Override
    public void windowDestroyed(WindowEvent arg0) {
    }

    @Override
    public void windowGainedFocus(WindowEvent arg0) {
    }

    @Override
    public void windowLostFocus(WindowEvent arg0) {
    }

    @Override
    public void windowMoved(WindowEvent arg0) {
    }

    @Override
    public void windowRepaint(WindowUpdateEvent arg0) {
    }

    @Override
    public void windowResized(WindowEvent arg0) {
    }

}
