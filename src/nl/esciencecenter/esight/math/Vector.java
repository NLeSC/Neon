package nl.esciencecenter.esight.math;

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
 * Generic abstract type for all vector implementaions.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public abstract class Vector {
    public static enum Type {
        LONG, FLOAT, INT, SHORT, BYTE
    }

    public static int SIZE_LONG = 8;
    public static int SIZE_FLOAT = 4;
    public static int SIZE_INT = 4;
    public static int SIZE_SHORT = 2;
    public static int SIZE_BYTE = 1;

    public final int size;
    public final Type type;

    public Vector(int size, Type type) {
        this.size = size;
        this.type = type;
    }

    /**
     * Getter for the size (number of places) for this vector.
     * 
     * @return the size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Getter for the type.
     * 
     * @return the type.
     */
    public Type getType() {
        return type;
    }
}
