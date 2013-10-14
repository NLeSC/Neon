package nl.esciencecenter.neon.models;

import nl.esciencecenter.neon.exceptions.UninitializedException;
import nl.esciencecenter.neon.math.VecF3;

/* Copyright [2013] [Netherlands eScience Center]
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
 * Bounding box implementation for use in behind-the-scenes graphics
 * calculations. (for now, mainly used in Text implementation)
 * 
 * @author Maarten van Meersbergen <m.vanmeersbergen@esciencecenter.nl>
 */
public class BoundingBox {
    private boolean initialized = false;
    private float minX, minY, minZ;
    private float maxX, maxY, maxZ;

    /**
     * Basic constructor for BoundingBox
     */
    public BoundingBox() {
        initialized = false;
    }

    /**
     * Unsets initialization state.
     */
    public void reset() {
        initialized = false;
    }

    /**
     * Resize method for this bounding box. Changes the bounds based on the
     * addition of new points. Updates minX, minY and minZ global variables and
     * sets initialized state.
     * 
     * @param newEntry
     *            A new vertex in the model to which this bounding box applies.
     */
    public void resize(VecF3 newEntry) {
        if (initialized) {
            minX = Math.min(minX, newEntry.getX());
            minY = Math.min(minY, newEntry.getY());
            minZ = Math.min(minZ, newEntry.getZ());

            maxX = Math.max(maxX, newEntry.getX());
            maxY = Math.max(maxY, newEntry.getY());
            maxZ = Math.max(maxZ, newEntry.getZ());
        } else {
            minX = newEntry.getX();
            minY = newEntry.getY();
            minZ = newEntry.getZ();

            maxX = newEntry.getX();
            maxY = newEntry.getY();
            maxZ = newEntry.getZ();
        }

        initialized = true;
    }

    /**
     * Getter for the minimum (x,y,z) point of the box.
     * 
     * @return The minimum (x,y,z) point of the box.
     * @throws UninitializedException
     *             if this box was not initialized before this method was
     *             called.
     */
    public VecF3 getMin() throws UninitializedException {
        if (!initialized) {
            throw new UninitializedException("BoundingBox not initialized.");
        }
        return new VecF3(minX, minY, minZ);
    }

    /**
     * Getter for the maximum (x,y,z) point of the box.
     * 
     * @return The maximum (x,y,z) point of the box.
     * @throws UninitializedException
     *             if this box was not initialized before this method was
     *             called.
     */
    public VecF3 getMax() throws UninitializedException {
        if (!initialized) {
            throw new UninitializedException("BoundingBox not initialized.");
        }
        return new VecF3(maxX, maxY, maxZ);
    }

    /**
     * Getter for the height (Y) of this box.
     * 
     * @return the height of this box.
     */
    public float getHeight() {
        return maxY - minY;
    }

    /**
     * Getter for the width (X) of this box.
     * 
     * @return the height of this box.
     */
    public float getWidth() {
        return maxX - minX;
    }

    /**
     * Getter for the depth (Z) of this box.
     * 
     * @return the height of this box.
     */
    public float getDepth() {
        return maxZ - minZ;
    }

    /**
     * Get the center point for this box.
     * 
     * @return The center point of this box.
     */
    public VecF3 getCenter() {
        float x = maxX - (0.5f * getWidth());
        float y = maxY - (0.5f * getHeight());
        float z = maxZ - (0.5f * getDepth());

        return new VecF3(x, y, z);
    }
}
