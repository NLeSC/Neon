package nl.esciencecenter.neon.util;

import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;

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
 * Listener for a quit action (like pressing the X on the top right of the
 * screen. DOES System.exit(0) because nothing else would work, so be careful!!
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
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
