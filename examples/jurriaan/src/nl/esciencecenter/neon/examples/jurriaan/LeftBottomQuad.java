package nl.esciencecenter.neon.examples.jurriaan;

import java.util.ArrayList;
import java.util.List;

import nl.esciencecenter.neon.math.Point4;
import nl.esciencecenter.neon.math.Float3Vector;
import nl.esciencecenter.neon.math.Float4Vector;
import nl.esciencecenter.neon.math.FloatVectorMath;
import nl.esciencecenter.neon.models.Model;

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
 * A simple quad (square) {@link Model}.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public class LeftBottomQuad extends Model {
    private static final int VERTICES_PER_QUAD = 6;

    /** width for this quad */
    private float width;
    /** height for this quad */
    private float height;

    /**
     * Simple constructor. Do not forget to call the
     * {@link #init(javax.media.opengl.GL3)} function before use.
     * 
     * @param height
     *            This quad's height.
     * @param width
     *            This quad's width.
     * @param leftBottom
     *            The left bottom location of this quad.
     */
    public LeftBottomQuad(float height, float width, Float3Vector leftBottom) {
        super(VertexFormat.TRIANGLES);

        Point4[] vertices = makeVertices(height, width, leftBottom);

        List<Float4Vector> allPoints = new ArrayList<Float4Vector>();
        List<Float3Vector> allNormals = new ArrayList<Float3Vector>();
        List<Float3Vector> allTexCoords = new ArrayList<Float3Vector>();

        // FRONT QUAD
        List<Point4> listPoints = tesselate(vertices, 1, 0, 3, 2);
        List<Float3Vector> listNormals = createNormals(new Float3Vector(0, 0, -1));
        List<Float3Vector> listTCoords = createTexCoords();

        allPoints.addAll(listPoints);
        allNormals.addAll(listNormals);
        allTexCoords.addAll(listTCoords);

        this.setNumVertices(allPoints.size());

        this.setVertices(FloatVectorMath.vec4ListToBuffer(allPoints));
        this.setNormals(FloatVectorMath.vec3ListToBuffer(allNormals));
        this.setTexCoords(FloatVectorMath.vec3ListToBuffer(allTexCoords));
    }

    /**
     * Generate the individual vertices needed for the construction of a Quad,
     * based on the given specifications.
     * 
     * @param height
     *            The height of this Model.
     * @param width
     *            The width of this Model.
     * @param leftBottomCoordinates
     *            The left Bottom location for this Model.
     * @return The points(vertices) that make up the composition of this Model.
     */
    private Point4[] makeVertices(float height, float width, Float3Vector leftBottomCoordinates) {
        float x = leftBottomCoordinates.getX();
        float y = leftBottomCoordinates.getY();

        float xpos = x + width;
        float xneg = x;
        float ypos = y + height;
        float yneg = y;

        Point4[] result = new Point4[] { new Point4(xneg, yneg, 0.0f), new Point4(xneg, ypos, 0.0f),
                new Point4(xpos, ypos, 0.0f), new Point4(xpos, yneg, 0.0f) };

        return result;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
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
    private List<Float3Vector> createTexCoords() {
        ArrayList<Float3Vector> result = new ArrayList<Float3Vector>();

        result.add(new Float3Vector(0, 0, 0));
        result.add(new Float3Vector(0, 1, 0));
        result.add(new Float3Vector(1, 1, 0));
        result.add(new Float3Vector(0, 0, 0));
        result.add(new Float3Vector(1, 1, 0));
        result.add(new Float3Vector(1, 0, 0));

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
    private List<Float3Vector> createNormals(Float3Vector normalToCreate) {
        ArrayList<Float3Vector> result = new ArrayList<Float3Vector>();

        for (int i = 0; i < VERTICES_PER_QUAD; i++) {
            result.add(new Float3Vector(normalToCreate));
        }

        return result;
    }
}
