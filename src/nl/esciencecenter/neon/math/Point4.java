package nl.esciencecenter.neon.math;

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
 * A optional, more specific {@link Float4Vector} implementation for points.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Point4 extends Float4Vector {
    /**
     * Stand-in for a 4-place vector, where the fourth place is always 1f.
     */
    public Point4() {
        super(0f, 0f, 0f, 1f);
    }

    /**
     * Stand-in for a 4-place vector, where the fourth place is always 1f.
     * 
     * @param vec
     *            The xyz values of this point.
     */
    public Point4(Float3Vector vec) {
        super(vec, 1f);
    }

    /**
     * Stand-in for a 4-place vector, where the fourth place is always 1f.
     * 
     * @param v
     *            A vector of which the fourth place will be discarded and
     *            replaced by 1f.
     */
    public Point4(Float4Vector v) {
        super(v.getX(), v.getY(), v.getZ(), 1f);
    }

    /**
     * Stand-in for a 4-place vector, where the fourth place is always 1f.
     * 
     * @param x
     *            The x value of this point.
     * @param y
     *            The y value of this point.
     * @param z
     *            The z value of this point.
     */
    public Point4(float x, float y, float z) {
        super(x, y, z, 1f);
    }
}
