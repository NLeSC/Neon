package nl.esciencecenter.esight.input;

import nl.esciencecenter.esight.math.VecF3;

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
 * A singleton pattern generic Input event Handler for use in OpenGL
 * applications. Currently handles only basic mouse events (left-click-drag,
 * scrollwheel).
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class InputHandler implements MouseListener, KeyListener {

    /**
     * Octants are used to define a direction from which the viewer is looking
     * at the scene, these are useful in Octrees.
     */
    public static enum octants {
        PPP, PPN, PNP, PNN, NPP, NPN, NNP, NNN
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
    private float dragLeftXorigin;
    /** Mouse drag start point in Y direction */
    private float dragLeftYorigin;

    /** Final rotation in openGL units */
    private VecF3 rotation;
    /** Final view distance (translation) in openGL units */
    private float viewDist = -5f;

    /** Current direction of the view */

    private static class SingletonHolder {
        public static final InputHandler INSTANCE = new InputHandler();
    }

    /**
     * The only access point for this singleton class.
     * 
     * @return The only instance of this class allowed at one time.
     */
    public static InputHandler getInstance() {
        return SingletonHolder.INSTANCE;
    }

    protected InputHandler() {
        rotation = new VecF3();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Empty - unneeded
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Empty - unneeded
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isButtonDown(MouseEvent.BUTTON1)) {
            dragLeftXorigin = e.getX();
            dragLeftYorigin = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        rotationXorigin = rotationX;
        rotationYorigin = rotationY;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.isButtonDown(MouseEvent.BUTTON1)) {
            // x/y reversed because of axis orientation. (up/down => x axis
            // rotation in OpenGL)
            if (e.isShiftDown()) {
                rotationX = ((e.getX() - dragLeftXorigin) / 10f + rotationXorigin) % 360;
                rotationY = ((e.getY() - dragLeftYorigin) / 10f + rotationYorigin) % 360;
            } else {
                rotationX = ((e.getX() - dragLeftXorigin) + rotationXorigin) % 360;
                rotationY = ((e.getY() - dragLeftYorigin) + rotationYorigin) % 360;
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
            newViewDist -= wheelRotation * 2;
        } else {
            float wheelRotation = e.getRotation()[1];
            newViewDist -= wheelRotation * 10;
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
    public VecF3 getRotation() {
        return rotation;
    }

    /**
     * @param rotation
     *            the current OpenGL ModelView rotation variable to set
     */
    public void setRotation(VecF3 rotation) {
        this.rotation = rotation;
    }

    /**
     * @return the current OpenGL ModelView View distance (translation) variable
     */
    public float getViewDist() {
        return viewDist;
    }

    /**
     * @param viewDist
     *            the current OpenGL ModelView View distance (translation)
     *            variable to set
     */
    public void setViewDist(float viewDist) {
        this.viewDist = viewDist;
    }
}
