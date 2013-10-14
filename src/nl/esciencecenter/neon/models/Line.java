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
 * Model for a line in space.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Line extends Model {
    /**
     * Basic constructor for Line model.
     * 
     * @param start
     *            The start point for the line.
     * @param end
     *            The end point for the line.
     */
    public Line(Float3Vector start, Float3Vector end) {
        super(VertexFormat.LINES);

        int numVertices = 2;

        Float4Vector[] points = new Float4Vector[numVertices];
        Float3Vector[] normals = new Float3Vector[numVertices];
        Float3Vector[] tCoords = new Float3Vector[numVertices];

        points[0] = new Float4Vector(start, 1f);
        points[1] = new Float4Vector(end, 1f);

        normals[0] = FloatVectorMath.normalize(start).neg();
        normals[1] = FloatVectorMath.normalize(end).neg();

        tCoords[0] = new Float3Vector(0, 0, 0);
        tCoords[1] = new Float3Vector(1, 1, 1);

        this.setNumVertices(numVertices);
        this.setVertices(FloatVectorMath.toBuffer(points));
        this.setNormals(FloatVectorMath.toBuffer(normals));
        this.setTexCoords(FloatVectorMath.toBuffer(tCoords));
    }
}
