package nl.esciencecenter.esight.models;

import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VecF4;
import nl.esciencecenter.esight.math.VectorFMath;

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
    public Line(VecF3 start, VecF3 end) {
        super(vertex_format.LINES);

        int numVertices = 2;

        VecF4[] points = new VecF4[numVertices];
        VecF3[] normals = new VecF3[numVertices];
        VecF3[] tCoords = new VecF3[numVertices];

        points[0] = new VecF4(start, 1f);
        points[1] = new VecF4(end, 1f);

        normals[0] = VectorFMath.normalize(start).neg();
        normals[1] = VectorFMath.normalize(end).neg();

        tCoords[0] = new VecF3(0, 0, 0);
        tCoords[1] = new VecF3(1, 1, 1);

        this.setNumVertices(numVertices);
        this.setVertices(VectorFMath.toBuffer(points));
        this.setNormals(VectorFMath.toBuffer(normals));
        this.setTexCoords(VectorFMath.toBuffer(tCoords));
    }
}
