package nl.esciencecenter.esight.examples.colormap;

import nl.esciencecenter.esight.input.InputHandler;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

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
 * Example {@link InputHandler} class implementation, that overrides the default
 * mousePressed event. This class uses the Singleton design pattern found here:
 * 
 * http://en.wikipedia.org/wiki/Singleton_pattern
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class ColormapExampleInputHandler extends InputHandler implements MouseListener, KeyListener {

    private static class SingletonHolder {
        public static final ColormapExampleInputHandler instance = new ColormapExampleInputHandler();
    }

    public static ColormapExampleInputHandler getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        // Do something that is different than the standard
    }
}
