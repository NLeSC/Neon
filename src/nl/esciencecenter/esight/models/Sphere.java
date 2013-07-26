package nl.esciencecenter.esight.models;

import java.util.ArrayList;
import java.util.List;

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
 * Sphere implementation (based on the code in the OpenGL red book) of the Model
 * class implemented by subdivision of a regular isocahedron. Minimum number of
 * vertices is therefore 20. Allows for multiple Levels of Detail, each level
 * multiplying the amount of vertices by 4 as compared to the previous level.
 * 
 * nr_of_vertices = 20 * (divisions ^ 4) * 3
 * 
 * @author Maarten van Meersbergen <m.vanmeersbergen@esciencecenter.nl>
 */
public class Sphere extends Model {
    private static final float X = 0.525731112119133606f;
    private static final float Z = 0.850650808352039932f;

    private static final VecF3[] vdata = { new VecF3(-X, 0f, Z), new VecF3(X, 0f, Z), new VecF3(-X, 0f, -Z),
            new VecF3(X, 0f, -Z), new VecF3(0f, Z, X), new VecF3(0f, Z, -X), new VecF3(0f, -Z, X),
            new VecF3(0f, -Z, -X), new VecF3(Z, X, 0f), new VecF3(-Z, X, 0f), new VecF3(Z, -X, 0f),
            new VecF3(-Z, -X, 0f) };

    private static final int[][] tindices = { { 1, 4, 0 }, { 4, 9, 0 }, { 4, 5, 9 }, { 8, 5, 4 }, { 1, 8, 4 },
            { 1, 10, 8 }, { 10, 3, 8 }, { 8, 3, 5 }, { 3, 2, 5 }, { 3, 7, 2 }, { 3, 10, 7 }, { 10, 6, 7 },
            { 6, 11, 7 }, { 6, 0, 11 }, { 6, 1, 0 }, { 10, 1, 6 }, { 11, 0, 9 }, { 2, 11, 9 }, { 5, 2, 9 },
            { 11, 2, 7 } };

    /**
     * Basic constructor for Sphere. Allows for multiple levels of detail.
     * 
     * @param divisions
     *            The number of divisions for the isocahedron. nr_of_vertices =
     *            20 * (divisions ^ 4) * 3
     */
    public Sphere(int divisions, boolean texCoordsIn3D) {
        super(vertex_format.TRIANGLES);

        List<VecF3> points3List = new ArrayList<VecF3>();

        for (int i = 0; i < tindices.length; i++) {
            makeVertices(points3List, vdata[tindices[i][0]], vdata[tindices[i][1]], vdata[tindices[i][2]], divisions);
        }

        List<VecF4> points4List = new ArrayList<VecF4>();

        for (int i = 0; i < points3List.size(); i++) {
            points4List.add(new VecF4(points3List.get(i), 1f));
        }

        normals = VectorFMath.vec3ListToBuffer(points3List);
        if (texCoordsIn3D) {
            texCoords = VectorFMath.vec3ListToBuffer(points3List);
        } else {
            ArrayList<VecF3> texCoords2D = new ArrayList<VecF3>();
            float minPhi = 0f, maxPhi = 0f;
            float minTheta = 0f, maxTheta = 0f;
            for (VecF3 point : points3List) {
                double x = point.get(0);
                double y = point.get(1);
                double z = point.get(2);

                float phi = (float) ((Math.atan(x / z) + (0.5 * Math.PI)) / Math.PI);
                float theta = (float) (Math.atan(Math.sqrt(z * z + x * x) / y) / Math.PI);

                if (phi < minPhi) {
                    minPhi = phi;
                }
                if (phi > maxPhi) {
                    maxPhi = phi;
                }
                if (theta < 0) {
                    theta = 1.0f + theta;
                }
                if (theta > maxTheta) {
                    maxTheta = theta;
                }
                texCoords2D.add(new VecF3(phi, theta, 0f));
            }

            texCoords = VectorFMath.vec3ListToBuffer(texCoords2D);
        }
        vertices = VectorFMath.vec4ListToBuffer(points4List);

        numVertices = points3List.size();
    }

    /**
     * Helper method to produce the vertices for a triangular part of the
     * sphere.
     * 
     * @param pointsList
     *            _output_ array for the vertex data.
     * @param a
     *            Point A of the triangle.
     * @param b
     *            Point B of the triangle.
     * @param c
     *            Point C of the triangle.
     * @param div
     *            The The number of divisions for this triangle.
     */
    private void makeVertices(List<VecF3> pointsList, VecF3 a, VecF3 b, VecF3 c, int div) {
        if (div <= 0) {
            pointsList.add(a);
            pointsList.add(b);
            pointsList.add(c);
        } else {
            VecF3 ab = new VecF3();
            VecF3 ac = new VecF3();
            VecF3 bc = new VecF3();

            for (int i = 0; i < 3; i++) {
                ab.set(i, (a.get(i) + b.get(i)));
                ac.set(i, (a.get(i) + c.get(i)));
                bc.set(i, (b.get(i) + c.get(i)));
            }

            ab = VectorFMath.normalize(ab);
            ac = VectorFMath.normalize(ac);
            bc = VectorFMath.normalize(bc);

            makeVertices(pointsList, a, ab, ac, div - 1);
            makeVertices(pointsList, b, bc, ab, div - 1);
            makeVertices(pointsList, c, ac, bc, div - 1);
            makeVertices(pointsList, ab, bc, ac, div - 1);
        }
    }
}
