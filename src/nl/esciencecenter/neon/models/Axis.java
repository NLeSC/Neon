package nl.esciencecenter.neon.models;

import nl.esciencecenter.neon.math.Float3Vector;
import nl.esciencecenter.neon.math.Float4Vector;
import nl.esciencecenter.neon.math.FloatVectorMath;

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
 * Model for an Axis in space. Has major and minor ticks at certain intervals on
 * the line.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Axis extends Model {
    /**
     * Basic constructor for Axis.
     * 
     * @param start
     *            The start point for the Axis.
     * @param end
     *            The end point for the Axis.
     * @param majorInterval
     *            The major interval for ticks.
     * @param minorInterval
     *            The minor interval for ticks.
     */
    public Axis(Float3Vector start, Float3Vector end, float majorInterval, float minorInterval) {
        super(VertexFormat.LINES);

        float length = FloatVectorMath.length(end.sub(start));
        int numMajorIntervals = (int) Math.floor(length / majorInterval);
        int numMinorIntervals = (int) Math.floor(length / minorInterval);

        int numVertices = 2 + (numMajorIntervals * 2) - 4 + (numMinorIntervals * 2) - 4;

        Float4Vector[] points = new Float4Vector[numVertices];
        Float3Vector[] normals = new Float3Vector[numVertices];
        Float3Vector[] tCoords = new Float3Vector[numVertices];

        int arrayindex = 0;
        points[0] = new Float4Vector(start, 1f);
        points[1] = new Float4Vector(end, 1f);

        normals[0] = FloatVectorMath.normalize(start).neg();
        normals[1] = FloatVectorMath.normalize(end).neg();

        tCoords[0] = new Float3Vector(0, 0, 0);
        tCoords[1] = new Float3Vector(1, 1, 1);

        arrayindex += 2;

        Float3Vector perpendicular;
        Float3Vector vec = FloatVectorMath.normalize((end.sub(start)));
        if (vec.getX() > 0.5f) {
            perpendicular = new Float3Vector(0f, 0f, 1f);
        } else if (vec.getY() > 0.5f) {
            perpendicular = new Float3Vector(1f, 0f, 0f);
        } else {
            perpendicular = new Float3Vector(0f, 1f, 0f);
        }

        Float3Vector nil = new Float3Vector();

        float majorIntervalSize = length / 100f;
        float minorIntervalSize = length / 300f;

        for (int i = 1; i < numMajorIntervals / 2; i++) {
            arrayindex = addInterval(points, normals, tCoords, arrayindex, nil.add(vec.mul(majorInterval * i)),
                    perpendicular, majorIntervalSize);
            arrayindex = addInterval(points, normals, tCoords, arrayindex, nil.sub(vec.mul(majorInterval * i)),
                    perpendicular, majorIntervalSize);
        }

        for (int i = 1; i < numMinorIntervals / 2; i++) {
            arrayindex = addInterval(points, normals, tCoords, arrayindex, nil.add(vec.mul(minorInterval * i)),
                    perpendicular, minorIntervalSize);
            arrayindex = addInterval(points, normals, tCoords, arrayindex, nil.sub(vec.mul(minorInterval * i)),
                    perpendicular, minorIntervalSize);

        }

        this.setNumVertices(numVertices);
        this.setVertices(FloatVectorMath.toBuffer(points));
        this.setNormals(FloatVectorMath.toBuffer(normals));
        this.setTexCoords(FloatVectorMath.toBuffer(tCoords));
    }

    /**
     * Helper method to add a perpendicular line as a tick on the axis.
     * 
     * @param points
     *            _output_ array for the vertex data.
     * @param normals
     *            _output_ array for the normals data.
     * @param tCoords
     *            _output_ array for the texture coordinates.
     * @param offset
     *            the index in the output arrays to start inputting data from.
     * @param center
     *            The center coordinate for this interval.
     * @param alignment
     *            The direction in which to paint the tick.
     * @param size
     *            the size of the tick to paint.
     * @return the new index in the ouput arrays.
     */
    private int addInterval(Float4Vector[] points, Float3Vector[] normals, Float3Vector[] tCoords, int offset, Float3Vector center,
            Float3Vector alignment, float size) {
        int tmpOffset = offset;
        points[tmpOffset] = new Float4Vector(center.add(alignment.mul(size)), 1f);
        normals[tmpOffset] = FloatVectorMath.normalize(alignment);
        tCoords[tmpOffset] = new Float3Vector(0, 0, 0);
        tmpOffset++;
        points[tmpOffset] = new Float4Vector(center.sub(alignment.mul(size)), 1f);
        normals[tmpOffset] = FloatVectorMath.normalize(alignment).neg();
        tCoords[tmpOffset] = new Float3Vector(1, 1, 1);
        tmpOffset++;
        return tmpOffset;
    }
}
