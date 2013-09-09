package nl.esciencecenter.esight.examples;

import nl.esciencecenter.esight.ESightNewtWindow;
import nl.esciencecenter.esight.input.InputHandler;
import nl.esciencecenter.esight.util.Settings;

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
 * Example implementation of the main class for an eSight application.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class HelloWorldExample {
    // This static definition (singleton) of a settings class ensures that these
    // global settings are useable throughout the application. This settings
    // class reads from a file named "settings.properties" to allow default
    // settings to be changed by the user.
    private final static Settings settings = Settings.getInstance();

    // An example implementation of a GLEventlistener. These listeners are the
    // backbone of any OpenGL application, and provide the display cycle needed
    // for continuous updates of the screen.
    private static HelloWorldGLEventListener exampleGLEventListener;

    // In this simple example, we only call the constructor. This is enough to
    // keep the program running, since the constructor will create new threads
    // for animation etc. Therefore, even though we do not use the new Object,
    // the program will not terminate.
    public static void main(String[] args) {
        new HelloWorldExample();
    }

    public HelloWorldExample() {
        // Create the custom GLEventListener
        exampleGLEventListener = new HelloWorldGLEventListener();

        // Create the default ESight OpenGL window.
        new ESightNewtWindow(true, InputHandler.getInstance(), exampleGLEventListener,
                settings.getDefaultScreenWidth(), settings.getDefaultScreenHeight(),
                "ESightExample - example Visualization Tool");
    }
}
