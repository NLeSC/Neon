package nl.esciencecenter.esight.models;

import java.util.ArrayList;
import java.util.List;

import nl.esciencecenter.esight.math.Point4;
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
 * Box implementation of the Model class.
 * 
 * @author Maarten van Meersbergen <m.vanmeersbergen@esciencecenter.nl>
 */
public class Box extends Model {

    private static final int VERTICES_PER_QUAD = 6;

    /**
     * Basic constructor for Box. Allows for an optional bottom side.
     * 
     * @param width
     *            The width (X) of this box (should fall in 0.0 to 1.0
     *            interval).
     * @param height
     *            The height (Y) of this box (should fall in 0.0 to 1.0
     *            interval).
     * @param depth
     *            The depth (Z) for this box (should fall in 0.0 to 1.0
     *            interval).
     * @param bottom
     *            flag to draw either a bottom (true) or no bottom (false).
     */
    public Box(float width, float height, float depth, boolean bottom) {
        super(VertexFormat.TRIANGLES);

        Point4[] vertices = makeVertices(width, height, depth);

        List<VecF4> allPoints = new ArrayList<VecF4>();
        List<VecF3> allNormals = new ArrayList<VecF3>();
        List<VecF3> allTexCoords = new ArrayList<VecF3>();

        // FRONT QUAD
        List<Point4> points = tesselate(vertices, 1, 0, 3, 2);
        List<VecF3> normals = createNormals(new VecF3(0, 0, -1));
        List<VecF3> tCoords = createTexCoords();

        allPoints.addAll(points);
        allNormals.addAll(normals);
        allTexCoords.addAll(tCoords);

        // RIGHT QUAD
        points = tesselate(vertices, 2, 3, 7, 6);
        normals = createNormals(new VecF3(1, 0, 0));
        tCoords = createTexCoords();

        allPoints.addAll(points);
        allNormals.addAll(normals);
        allTexCoords.addAll(tCoords);

        if (bottom) {
            // BOTTOM QUAD
            points = tesselate(vertices, 3, 0, 4, 7);
            normals = createNormals(new VecF3(0, -1, 0));
            tCoords = createTexCoords();

            allPoints.addAll(points);
            allNormals.addAll(normals);
            allTexCoords.addAll(tCoords);
        }

        // TOP QUAD
        points = tesselate(vertices, 6, 5, 1, 2);
        normals = createNormals(new VecF3(0, 1, 0));
        tCoords = createTexCoords();

        allPoints.addAll(points);
        allNormals.addAll(normals);
        allTexCoords.addAll(tCoords);

        // BACK QUAD
        points = tesselate(vertices, 4, 5, 6, 7);
        normals = createNormals(new VecF3(0, 0, 1));
        tCoords = createTexCoords();

        allPoints.addAll(points);
        allNormals.addAll(normals);
        allTexCoords.addAll(tCoords);

        // LEFT QUAD
        points = tesselate(vertices, 5, 4, 0, 1);
        normals = createNormals(new VecF3(-1, 0, 0));
        tCoords = createTexCoords();

        allPoints.addAll(points);
        allNormals.addAll(normals);
        allTexCoords.addAll(tCoords);

        final int floatsPerVecF4 = 4;
        this.setNumVertices(allPoints.size() / floatsPerVecF4);

        this.setVertices(VectorFMath.vec4ListToBuffer(allPoints));
        this.setNormals(VectorFMath.vec3ListToBuffer(allNormals));
        this.setTexCoords(VectorFMath.vec3ListToBuffer(allTexCoords));
    }

    /**
     * Helper method to create a Vertex array describing the corners of a box.
     * The sides still need to be divided into triangles before it can be used.
     * 
     * @param width
     *            The width of the box to make (assumes input from 0.0 to 1.0).
     * @param height
     *            The height of the box to make (assumes input from 0.0 to 1.0).
     * @param depth
     *            The depth of the box to make (assumes input from 0.0 to 1.0).
     * @return The array of 8 points that makes up all the corners of a box.
     */
    private Point4[] makeVertices(float width, float height, float depth) {
        float xpos = +(width / 2f);
        float xneg = -(width / 2f);
        float ypos = +(height / 2f);
        float yneg = -(height / 2f);
        float zpos = +(depth / 2f);
        float zneg = -(depth / 2f);

        Point4[] result = new Point4[] { new Point4(xneg, yneg, zpos), new Point4(xneg, ypos, zpos),
                new Point4(xpos, ypos, zpos), new Point4(xpos, yneg, zpos), new Point4(xneg, yneg, zneg),
                new Point4(xneg, ypos, zneg), new Point4(xpos, ypos, zneg), new Point4(xpos, yneg, zneg) };

        return result;
    }

    /**
     * Create two triangles with vertices (Points) in the correct order out of
     * the given points.
     * 
     * @param source
     *            The source array with base-model vertices.
     * @param a
     *            The index of the first corner in the source array.
     * @param b
     *            The index of the second corner in the source array.
     * @param c
     *            The index of the third corner in the source array.
     * @param d
     *            The index of the fourth corner in the source array.
     * @return A list with 6 vertices representing 2 triangles.
     */
    private List<Point4> tesselate(Point4[] source, int a, int b, int c, int d) {
        ArrayList<Point4> result = new ArrayList<Point4>();

        result.add(source[a]);
        result.add(source[b]);
        result.add(source[c]);
        result.add(source[a]);
        result.add(source[c]);
        result.add(source[d]);

        return result;
    }

    /**
     * Create texture coordinates for the given points.
     * 
     * @param source
     *            The source array with base-model vertices.
     * @param a
     *            The index of the first corner in the source array.
     * @param b
     *            The index of the second corner in the source array.
     * @param c
     *            The index of the third corner in the source array.
     * @param d
     *            The index of the fourth corner in the source array.
     * @return An array with 6 vectors representing texture coordinates for the
     *         given points.
     */
    private List<VecF3> createTexCoords() {
        ArrayList<VecF3> result = new ArrayList<VecF3>();

        result.add(new VecF3(0, 0, 0));
        result.add(new VecF3(0, 1, 0));
        result.add(new VecF3(1, 1, 0));
        result.add(new VecF3(0, 0, 0));
        result.add(new VecF3(1, 1, 0));
        result.add(new VecF3(1, 0, 0));

        return result;
    }

    /**
     * Create normals for the given points.
     * 
     * @param normalToCreate
     *            The normal vector to copy X times ;)
     * @return An array with 6 vectors representing normals for the given
     *         points.
     */
    private List<VecF3> createNormals(VecF3 normalToCreate) {
        ArrayList<VecF3> result = new ArrayList<VecF3>();

        for (int i = 0; i < VERTICES_PER_QUAD; i++) {
            result.add(new VecF3(normalToCreate));
        }

        return result;
    }
}
