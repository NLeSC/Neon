package nl.esciencecenter.neon.models;

import java.util.ArrayList;
import java.util.List;

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
 * A sphere implementation based on a number of latitude and longitude
 * divisions.
 * 
 * @author Maarten van Meersbergen <m.vanmeersbergen@esciencecenter.nl>
 */
public class InvertedGeoSphere extends Model {
    /** state keeper for texture coordinate state (3d coordinates yes/no) */
    private boolean texCoordsIn3D = false;
    /** the number of ribs in the latitude or longitude directions */
    private final int latRibs, lonRibs;

    /**
     * Constructor for InvertedGeoSphere, prepares the attributes for this model
     * and makes it ready to be initialized.
     * 
     * @param latRibs
     *            The number of desired ribs in the latitude (north-south)
     *            direction.
     * @param lonRibs
     *            The number of desired ribs in the longitude (east-west)
     *            direction.
     * @param radius
     *            The radius of the sphere (assumed to be in 0.0 to 1.0 range)
     * @param texCoordsIn3D
     *            Flag for possible 3d texture coordinates.
     */
    public InvertedGeoSphere(int latRibs, int lonRibs, float radius, boolean texCoordsIn3D) {
        super(VertexFormat.TRIANGLES);
        this.texCoordsIn3D = texCoordsIn3D;
        this.latRibs = latRibs;
        this.lonRibs = lonRibs;

        List<Float4Vector> points4List = new ArrayList<Float4Vector>();
        List<Float3Vector> normals3List = new ArrayList<Float3Vector>();
        List<Float3Vector> tCoords3List = new ArrayList<Float3Vector>();

        for (int lon = 0; lon < lonRibs; lon++) {
            for (int lat = 0; lat < latRibs; lat++) {
                Float3Vector[] quad = makeQuad(lat, lon, radius);

                points4List.addAll(makeVertices(quad));
                normals3List.addAll(makeNormals(quad));
                tCoords3List.addAll(makeTexCoords(quad, lat, lon));
            }
        }

        setNumVertices(points4List.size());

        setVertices(FloatVectorMath.vec4ListToBuffer(points4List));
        setNormals(FloatVectorMath.vec3ListToBuffer(normals3List));
        setTexCoords(FloatVectorMath.vec3ListToBuffer(tCoords3List));
    }

    /**
     * Make a single quad based on the current and next latitude and longitude
     * coordinates.
     * 
     * @param latRib
     *            The latitude rib to make coordinates for.
     * @param lonRib
     *            The longitude rib to make coordinates for.
     * @param radius
     *            The radius of the sphere.
     * @return a curved quad on the sphere surface.
     */
    private Float3Vector[] makeQuad(int latRib, int lonRib, float radius) {
        float lonAnglePerRib = (float) ((2 * Math.PI) / lonRibs);
        float latAnglePerRib = (float) ((Math.PI) / latRibs);

        float startLonAngle = lonAnglePerRib * lonRib;
        float stopLonAngle = lonAnglePerRib * (lonRib + 1);

        float startLatAngle = latAnglePerRib * latRib;
        float stopLatAngle = latAnglePerRib * (latRib + 1);

        float x00 = (float) (Math.sin(startLatAngle) * Math.cos(startLonAngle));
        float x10 = (float) (Math.sin(stopLatAngle) * Math.cos(startLonAngle));
        float x01 = (float) (Math.sin(startLatAngle) * Math.cos(stopLonAngle));
        float x11 = (float) (Math.sin(stopLatAngle) * Math.cos(stopLonAngle));

        float y00 = (float) (Math.cos(startLatAngle));
        float y10 = (float) (Math.cos(stopLatAngle));
        float y01 = (float) (Math.cos(startLatAngle));
        float y11 = (float) (Math.cos(stopLatAngle));

        float z00 = (float) (Math.sin(startLatAngle) * Math.sin(startLonAngle));
        float z10 = (float) (Math.sin(stopLatAngle) * Math.sin(startLonAngle));
        float z01 = (float) (Math.sin(startLatAngle) * Math.sin(stopLonAngle));
        float z11 = (float) (Math.sin(stopLatAngle) * Math.sin(stopLonAngle));

        Float3Vector[] result = new Float3Vector[] { new Float3Vector(x00, y00, z00).mul(radius),
                new Float3Vector(x11, y11, z11).mul(radius), new Float3Vector(x01, y01, z01).mul(radius),

                new Float3Vector(x00, y00, z00).mul(radius), new Float3Vector(x10, y10, z10).mul(radius),
                new Float3Vector(x11, y11, z11).mul(radius) };

        return result;
    }

    /**
     * Make the vertex coordinates for the given quad.
     * 
     * @param quad
     *            The quad to make coordinates for.
     * @return The vertex coordinates corresponding to the given quad.
     */
    private List<Float4Vector> makeVertices(Float3Vector[] quad) {
        List<Float4Vector> vertices = new ArrayList<Float4Vector>();

        vertices.add(new Float4Vector(quad[0], 1f));
        vertices.add(new Float4Vector(quad[1], 1f));
        vertices.add(new Float4Vector(quad[2], 1f));

        vertices.add(new Float4Vector(quad[3], 1f));
        vertices.add(new Float4Vector(quad[4], 1f));
        vertices.add(new Float4Vector(quad[5], 1f));

        return vertices;
    }

    /**
     * Make the normal vectors for the given quad.
     * 
     * @param quad
     *            The quad to make normals for.
     * @return The normals corresponding to the given quad.
     */
    private List<Float3Vector> makeNormals(Float3Vector[] quad) {
        List<Float3Vector> normals = new ArrayList<Float3Vector>();

        normals.add(FloatVectorMath.normalize(quad[0]));
        normals.add(FloatVectorMath.normalize(quad[1]));
        normals.add(FloatVectorMath.normalize(quad[2]));

        normals.add(FloatVectorMath.normalize(quad[3]));
        normals.add(FloatVectorMath.normalize(quad[4]));
        normals.add(FloatVectorMath.normalize(quad[5]));

        return normals;
    }

    /**
     * Make the texture coordinates for the given quad.
     * 
     * @param quad
     *            The quad to make coordinates for.
     * @param latRib
     *            The latitude rib to make coordinates for.
     * @param lonRib
     *            The longitude rib to make coordinates for.
     * @return The texture coordinates corresponding to the given quad.
     */
    private List<Float3Vector> makeTexCoords(Float3Vector[] quad, int latRib, int lonRib) {
        List<Float3Vector> texCoords = new ArrayList<Float3Vector>();

        if (texCoordsIn3D) {
            texCoords.add((quad[0].add(new Float3Vector(1f, 1f, 1f))).div(2f));
            texCoords.add((quad[1].add(new Float3Vector(1f, 1f, 1f))).div(2f));
            texCoords.add((quad[2].add(new Float3Vector(1f, 1f, 1f))).div(2f));

            texCoords.add((quad[3].add(new Float3Vector(1f, 1f, 1f))).div(2f));
            texCoords.add((quad[4].add(new Float3Vector(1f, 1f, 1f))).div(2f));
            texCoords.add((quad[5].add(new Float3Vector(1f, 1f, 1f))).div(2f));
        } else {
            texCoords.add(new Float3Vector(((float) lonRib / (float) lonRibs), ((float) latRib / (float) latRibs), 0));
            texCoords.add(new Float3Vector(((lonRib + 1f) / lonRibs), ((latRib + 1f) / latRibs), 0));
            texCoords.add(new Float3Vector(((lonRib + 1f) / lonRibs), ((float) latRib / (float) latRibs), 0));

            texCoords.add(new Float3Vector(((float) lonRib / lonRibs), (((float) latRib / (float) latRibs)), 0));
            texCoords.add(new Float3Vector(((float) lonRib / lonRibs), ((latRib + 1f) / latRibs), 0));
            texCoords.add(new Float3Vector(((lonRib + 1f) / lonRibs), ((latRib + 1f) / latRibs), 0));
        }

        return texCoords;
    }

    /**
     * Getter for the number of ribs in latitide (north-south) direction.
     * 
     * @return the number of ribs in latitide (north-south) direction.
     */
    public int getNumlatRibs() {
        return latRibs;
    }

    /**
     * Getter for the number of ribs in longitude (east-west) direction.
     * 
     * @return the number of ribs in longitude (east-west) direction.
     */
    public int getNumlonRibs() {
        return lonRibs;
    }
}
