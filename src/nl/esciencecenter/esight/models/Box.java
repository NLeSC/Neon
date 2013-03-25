package nl.esciencecenter.esight.models;

import nl.esciencecenter.esight.math.Point4;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VectorFMath;

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
 * Box implementation of the Model class.
 * 
 * @author Maarten van Meersbergen <m.vanmeersbergen@esciencecenter.nl>
 */
public class Box extends Model {
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
        super(vertex_format.TRIANGLES);

        Point4[] vertices = makeVertices(width, height, depth);

        int numVertices;
        if (bottom) {
            numVertices = 36;
        } else {
            numVertices = 30;
        }

        Point4[] points = new Point4[numVertices];
        VecF3[] normals = new VecF3[numVertices];
        VecF3[] tCoords = new VecF3[numVertices];

        int arrayindex = 0;
        for (int i = arrayindex; i < arrayindex + 6; i++) {
            normals[i] = new VecF3(0, 0, -1);
        }
        arrayindex = newQuad(points, tCoords, arrayindex, vertices, 1, 0, 3, 2); // FRONT

        for (int i = arrayindex; i < arrayindex + 6; i++) {
            normals[i] = new VecF3(1, 0, 0);
        }
        arrayindex = newQuad(points, tCoords, arrayindex, vertices, 2, 3, 7, 6); // RIGHT

        if (bottom) {
            for (int i = arrayindex; i < arrayindex + 6; i++) {
                normals[i] = new VecF3(0, -1, 0);
            }
            arrayindex = newQuad(points, tCoords, arrayindex, vertices, 3, 0, 4, 7); // BOTTOM
        }

        for (int i = arrayindex; i < arrayindex + 6; i++) {
            normals[i] = new VecF3(0, 1, 0);
        }
        arrayindex = newQuad(points, tCoords, arrayindex, vertices, 6, 5, 1, 2); // TOP

        for (int i = arrayindex; i < arrayindex + 6; i++) {
            normals[i] = new VecF3(0, 0, 1);
        }
        arrayindex = newQuad(points, tCoords, arrayindex, vertices, 4, 5, 6, 7); // BACK

        for (int i = arrayindex; i < arrayindex + 6; i++) {
            normals[i] = new VecF3(-1, 0, 0);
        }
        arrayindex = newQuad(points, tCoords, arrayindex, vertices, 5, 4, 0, 1); // LEFT

        this.numVertices = numVertices;
        this.vertices = VectorFMath.toBuffer(points);
        this.normals = VectorFMath.toBuffer(normals);
        this.texCoords = VectorFMath.toBuffer(tCoords);
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

        Point4[] result = new Point4[] { new Point4(xneg, yneg, zpos, 1.0f), new Point4(xneg, ypos, zpos, 1.0f),
                new Point4(xpos, ypos, zpos, 1.0f), new Point4(xpos, yneg, zpos, 1.0f),
                new Point4(xneg, yneg, zneg, 1.0f), new Point4(xneg, ypos, zneg, 1.0f),
                new Point4(xpos, ypos, zneg, 1.0f), new Point4(xpos, yneg, zneg, 1.0f) };

        return result;
    }

    /**
     * Helper method used to generate quads out of an array of points.
     * 
     * @param points
     *            The _output_ parameter for the vertex data.
     * @param tCoords
     *            The _output_ array of texture coordinates.
     * @param offset
     *            The offset in the output arrays.
     * @param source
     *            The source array of vertices.
     * @param a
     *            The index of the first vertex in the input array.
     * @param b
     *            The index of the second vertex in the input array.
     * @param c
     *            The index of the third vertex in the input array.
     * @param d
     *            The index of the fourth vertex in the input array.
     * @return The new array offset for the output arrays.
     */
    private int newQuad(Point4[] points, VecF3[] tCoords, int offset, Point4[] source, int a, int b, int c, int d) {
        points[offset] = source[a];
        tCoords[offset] = new VecF3(source[a]);
        offset++;
        points[offset] = source[b];
        tCoords[offset] = new VecF3(source[b]);
        offset++;
        points[offset] = source[c];
        tCoords[offset] = new VecF3(source[c]);
        offset++;
        points[offset] = source[a];
        tCoords[offset] = new VecF3(source[a]);
        offset++;
        points[offset] = source[c];
        tCoords[offset] = new VecF3(source[c]);
        offset++;
        points[offset] = source[d];
        tCoords[offset] = new VecF3(source[d]);
        offset++;

        return offset;
    }
}
