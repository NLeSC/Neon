package nl.esciencecenter.neon.examples.graphs;

import nl.esciencecenter.neon.input.InputHandler;
import nl.esciencecenter.neon.math.Float3Vector;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

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
 * Example {@link InputHandler} class implementation, that overrides the default
 * mousePressed event. This class uses the Singleton design pattern found here:
 * 
 * http://en.wikipedia.org/wiki/Singleton_pattern
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class GraphsInputHandler extends InputHandler implements MouseListener, KeyListener {
    protected static class PickRequest {
        public final int x, y;

        public PickRequest(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /** Initial value for the rotation in the X direction */
    private float rotationXorigin = 0;
    /**
     * Final rotation in the X direction, translated to openGL units, stored to
     * make successive rotations smooth
     */
    private float rotationX;

    /** Initial value for the rotation in the Y direction */
    private float rotationYorigin = 0;
    /**
     * Final rotation in the Y direction, translated to openGL units, stored to
     * make successive rotations smooth
     */
    private float rotationY;

    /** Mouse drag start point in X direction */
    private float dragXorigin;
    /** Mouse drag start point in Y direction */
    private float dragYorigin;

    /** Final rotation and translationin openGL units */
    private Float3Vector rotation, translation;

    /** Final view distance (translation) in openGL units */
    private float viewDist = -5f;

    private float translationX = 0f;
    private float translationY = 0f;

    private float translationXorigin = 0f;
    private float translationYorigin = 0f;

    private static PickRequest pickRequest = null;

    private GraphsInputHandler() {
        reset();
    }

    private void reset() {
        rotationXorigin = 0;
        rotationX = 0;
        rotationYorigin = 0;
        rotationY = 0;

        dragXorigin = 0;
        dragYorigin = 0;

        translationX = 0f;
        translationXorigin = 0f;
        translationY = 0f;
        translationYorigin = 0f;

        rotation = new Float3Vector();
        translation = new Float3Vector();
        viewDist = -3f;
    }

    private static class SingletonHolder {
        public static final GraphsInputHandler instance = new GraphsInputHandler();
    }

    public static GraphsInputHandler getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragXorigin = e.getX();
        dragYorigin = e.getY();

        if (e.isButtonDown(MouseEvent.BUTTON1)) {
            int x = e.getX();
            int y = e.getY();
            pickRequest = new PickRequest(x, y);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        rotationXorigin = rotationX;
        rotationYorigin = rotationY;

        translationXorigin = translationX;
        translationYorigin = translationY;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.isButtonDown(MouseEvent.BUTTON1)) {
            // x/y reversed because of axis orientation. (up/down => x axis
            // rotation in OpenGL)
            if (e.isShiftDown()) {
                rotationX = ((e.getX() - dragXorigin) / 10f + rotationXorigin) % 360;
                rotationY = ((e.getY() - dragYorigin) / 10f + rotationYorigin) % 360;
            } else {
                rotationX = ((e.getX() - dragXorigin) + rotationXorigin) % 360;
                rotationY = ((e.getY() - dragYorigin) + rotationYorigin) % 360;
            }
            // Make sure the numbers are always positive (so we can determine
            // the octant we're in more easily)
            if (rotationX < 0) {
                rotationX = 360f + rotationX % 360;
            }
            if (rotationY < 0) {
                rotationY = 360f + rotationY % 360;
            }

            rotation.setX(rotationY);
            rotation.setY(rotationX);
            rotation.setZ(0f); // We never rotate around the Z axis.
        } else if (e.isButtonDown(MouseEvent.BUTTON3)) {
            translationX = (.01f * (e.getX() - dragXorigin)) + translationXorigin;
            translationY = (-.01f * (e.getY() - dragYorigin)) + translationYorigin;

            translation.setX(translationX);
            translation.setY(translationY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Empty - unneeded
    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
        float newViewDist = this.viewDist;

        if (e.isShiftDown()) {
            float wheelRotation = e.getRotation()[0];
            newViewDist -= wheelRotation * .5;
        } else {
            float wheelRotation = e.getRotation()[1];
            newViewDist -= wheelRotation * 2;
        }
        viewDist = newViewDist;
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        // We could add something useful here
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // We could add something useful here
    }

    /**
     * 
     * @return the current OpenGL ModelView rotation variable
     */
    @Override
    public Float3Vector getRotation() {
        return rotation;
    }

    /**
     * @param rotation
     *            the current OpenGL ModelView rotation variable to set
     */
    @Override
    public void setRotation(Float3Vector rotation) {
        this.rotation = rotation;
    }

    /**
     * 
     * @return the current OpenGL ModelView translation variable
     */
    public Float3Vector getTranslation() {
        return translation;
    }

    /**
     * @param rotation
     *            the OpenGL ModelView translation variable to set
     */
    public void setTranslation(Float3Vector translation) {
        this.translation = translation;
    }

    /**
     * @return the current OpenGL ModelView View distance (translation) variable
     */
    @Override
    public float getViewDist() {
        return viewDist;
    }

    /**
     * @param viewDist
     *            the current OpenGL ModelView View distance (translation)
     *            variable to set
     */
    @Override
    public void setViewDist(float viewDist) {
        this.viewDist = viewDist;
    }

    public static PickRequest doPick() {
        PickRequest tmp = pickRequest;
        pickRequest = null;

        return tmp;
    }
}
