package nl.esciencecenter.neon.models;

import nl.esciencecenter.neon.math.VecF3;
import nl.esciencecenter.neon.math.VecF4;
import nl.esciencecenter.neon.math.VectorFMath;

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
    public Axis(VecF3 start, VecF3 end, float majorInterval, float minorInterval) {
        super(VertexFormat.LINES);

        float length = VectorFMath.length(end.sub(start));
        int numMajorIntervals = (int) Math.floor(length / majorInterval);
        int numMinorIntervals = (int) Math.floor(length / minorInterval);

        int numVertices = 2 + (numMajorIntervals * 2) - 4 + (numMinorIntervals * 2) - 4;

        VecF4[] points = new VecF4[numVertices];
        VecF3[] normals = new VecF3[numVertices];
        VecF3[] tCoords = new VecF3[numVertices];

        int arrayindex = 0;
        points[0] = new VecF4(start, 1f);
        points[1] = new VecF4(end, 1f);

        normals[0] = VectorFMath.normalize(start).neg();
        normals[1] = VectorFMath.normalize(end).neg();

        tCoords[0] = new VecF3(0, 0, 0);
        tCoords[1] = new VecF3(1, 1, 1);

        arrayindex += 2;

        VecF3 perpendicular;
        VecF3 vec = VectorFMath.normalize((end.sub(start)));
        if (vec.getX() > 0.5f) {
            perpendicular = new VecF3(0f, 0f, 1f);
        } else if (vec.getY() > 0.5f) {
            perpendicular = new VecF3(1f, 0f, 0f);
        } else {
            perpendicular = new VecF3(0f, 1f, 0f);
        }

        VecF3 nil = new VecF3();

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
        this.setVertices(VectorFMath.toBuffer(points));
        this.setNormals(VectorFMath.toBuffer(normals));
        this.setTexCoords(VectorFMath.toBuffer(tCoords));
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
    private int addInterval(VecF4[] points, VecF3[] normals, VecF3[] tCoords, int offset, VecF3 center,
            VecF3 alignment, float size) {
        int tmpOffset = offset;
        points[tmpOffset] = new VecF4(center.add(alignment.mul(size)), 1f);
        normals[tmpOffset] = VectorFMath.normalize(alignment);
        tCoords[tmpOffset] = new VecF3(0, 0, 0);
        tmpOffset++;
        points[tmpOffset] = new VecF4(center.sub(alignment.mul(size)), 1f);
        normals[tmpOffset] = VectorFMath.normalize(alignment).neg();
        tCoords[tmpOffset] = new VecF3(1, 1, 1);
        tmpOffset++;
        return tmpOffset;
    }
}
