package nl.esciencecenter.esight.math;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import nl.esciencecenter.esight.datastructures.GLSLAttrib;
import nl.esciencecenter.esight.math.Vector.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.common.nio.Buffers;

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
 * Generic List implementation for all eSight Vector implementations. Provides
 * toBuffer functionality for list-to-buffer transformations (needed to give the
 * data to shaders.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class VectorList extends ArrayList<Vector> {
    private static final long serialVersionUID = -7523352180818551741L;

    private final static Logger logger = LoggerFactory.getLogger(GLSLAttrib.class);

    public int vectorSize;
    public Vector.Type type;

    public VectorList(int vectorSize, Vector.Type type) {
        super();

        this.vectorSize = vectorSize;
        this.type = type;
    }

    /**
     * Getter for the size (number of places) for this vector.
     * 
     * @return the size.
     */
    public int getVectorSize() {
        return vectorSize;
    }

    /**
     * Getter for the type.
     * 
     * @return the type.
     */
    public Type getType() {
        return type;
    }

    @Override
    public boolean add(Vector v) {
        if (v.getSize() == vectorSize && v.getType() == type) {
            super.add(v);
            return true;
        } else {
            logger.error("ERROR: malformed VectorList add type or size.");
            return false;
        }
    }

    /**
     * Floatbuffer converter for this list of Vectors.
     * 
     * @return The Floatbuffer filled with the vectors.
     */
    public Buffer toBuffer() {
        if (type == Vector.Type.FLOAT) {
            FloatBuffer result = Buffers.newDirectFloatBuffer(size() * vectorSize);

            for (int i = 0; i < size(); i++) {
                VectorF vf = (VectorF) iterator().next();
                result.put(vf.asBuffer());
            }

            result.rewind();

            return result;
        } else {
            logger.error("ERROR: unimplemented VectorList toBuffer type.");
            return null;
        }
    }

}
