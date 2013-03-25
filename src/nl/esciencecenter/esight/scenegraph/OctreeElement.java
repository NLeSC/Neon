package nl.esciencecenter.esight.scenegraph;

import nl.esciencecenter.esight.math.VecF3;

/* Copyright [2013] [Netherlands eScience Center]
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
 * Default element to be stored in an octree data structure, as used in
 * {@link OctreeNode}
 * 
 * @author Maarten van Meersbergen <m.vanmeersbergen@esciencecenter.nl>
 */
public class OctreeElement {
    /** The center location for this octree element. */
    private final VecF3 center;

    /**
     * Basic constructor for OctreeElement
     * 
     * @param center
     *            the center location for this element.
     */
    public OctreeElement(VecF3 center) {
        this.center = center;
    }

    /**
     * Getter for the center of this element.
     * 
     * @return the center of this element.
     */
    public VecF3 getCenter() {
        return this.center;
    }
}
