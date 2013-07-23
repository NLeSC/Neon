package nl.esciencecenter.esight.examples.goggleswing;

import javax.swing.JFrame;

import nl.esciencecenter.esight.ESightNewtWindow;

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
 * Example implementation of the main class for an eSight application.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class GoggleSwingExample {
    // This static definition (singleton) of a settings class ensures that these
    // global settings are useable throughout the application. This settings
    // class reads from a file named "settings.properties" to allow default
    // settings to be changed by the user.
    private final static GoggleSwingSettings settings = GoggleSwingSettings.getInstance();

    // This is an implementation of a 'loose' interface window. This is done to
    // allow java swing components as user interface elements. It is in a
    // seperate window because of MacOSX limitations in the JOGL library.
    private static GoggleSwingInterfaceWindow examplePanel;

    // An example implementation of a GLEventlistener. These listeners are the
    // backbone of any OpenGL application, and provide the display cycle needed
    // for continuous updates of the screen.
    private static GoggleSwingGLEventListener exampleGLEventListener;

    // In this simple example, we only call the constructor. This is enough to
    // keep the program running, since the constructor will create new threads
    // for animation etc. Therefore, even though we do not use the new Object,
    // the program will not terminate.
    public static void main(String[] args) {
        new GoggleSwingExample();
    }

    public GoggleSwingExample() {
        // Create the Swing interface elements
        examplePanel = new GoggleSwingInterfaceWindow();

        // Create the custom GLEventListener
        exampleGLEventListener = new GoggleSwingGLEventListener();

        // Create the default ESight OpenGL window.
        new ESightNewtWindow(true, GoggleSwingInputHandler.getInstance(), exampleGLEventListener,
                settings.getDefaultScreenWidth(), settings.getDefaultScreenHeight(),
                "GoggleSwing - example Visualization Tool");

        // Create the interface frame
        final JFrame frame = new JFrame("- - - -");
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            // Override the close method to exit the program instead of the
            // default do-nothing.
            public void windowClosing(java.awt.event.WindowEvent arg0) {
                System.exit(0);
            }
        });

        // Set the interface size according to the settings.
        frame.setSize(settings.getInterfaceWidth(), settings.getInterfaceHeight());

        // Lock the interface size.
        frame.setResizable(false);

        // Do the addition of the interface panel with the swing (non)threading
        // model.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    frame.getContentPane().add(examplePanel);
                } catch (final Exception e) {
                    e.printStackTrace(System.err);
                    System.exit(1);
                }
            }
        });

        frame.setVisible(true);
    }
}
