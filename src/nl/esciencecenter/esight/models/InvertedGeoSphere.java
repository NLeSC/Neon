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

        List<VecF4> points4List = new ArrayList<VecF4>();
        List<VecF3> normals3List = new ArrayList<VecF3>();
        List<VecF3> tCoords3List = new ArrayList<VecF3>();

        for (int lon = 0; lon < lonRibs; lon++) {
            for (int lat = 0; lat < latRibs; lat++) {
                VecF3[] quad = makeQuad(lat, lon, radius);

                points4List.addAll(makeVertices(quad));
                normals3List.addAll(makeNormals(quad));
                tCoords3List.addAll(makeTexCoords(quad, lat, lon));
            }
        }

        setNumVertices(points4List.size());

        setVertices(VectorFMath.vec4ListToBuffer(points4List));
        setNormals(VectorFMath.vec3ListToBuffer(normals3List));
        setTexCoords(VectorFMath.vec3ListToBuffer(tCoords3List));
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
    private VecF3[] makeQuad(int latRib, int lonRib, float radius) {
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

        VecF3[] result = new VecF3[] { new VecF3(x00, y00, z00).mul(radius), new VecF3(x11, y11, z11).mul(radius),
                new VecF3(x01, y01, z01).mul(radius),

                new VecF3(x00, y00, z00).mul(radius), new VecF3(x10, y10, z10).mul(radius),
                new VecF3(x11, y11, z11).mul(radius) };

        return result;
    }

    /**
     * Make the vertex coordinates for the given quad.
     * 
     * @param quad
     *            The quad to make coordinates for.
     * @param latRib
     *            The latitude rib to make coordinates for.
     * @param lonRib
     *            The longitude rib to make coordinates for.
     * @param radius
     *            The radius of the sphere.
     * @return The vertex coordinates corresponding to the given quad.
     */
    private List<VecF4> makeVertices(VecF3[] quad) {
        List<VecF4> vertices = new ArrayList<VecF4>();

        vertices.add(new VecF4(quad[0], 1f));
        vertices.add(new VecF4(quad[1], 1f));
        vertices.add(new VecF4(quad[2], 1f));

        vertices.add(new VecF4(quad[3], 1f));
        vertices.add(new VecF4(quad[4], 1f));
        vertices.add(new VecF4(quad[5], 1f));

        return vertices;
    }

    /**
     * Make the normal vectors for the given quad.
     * 
     * @param quad
     *            The quad to make normals for.
     * @param latRib
     *            The latitude rib to make normals for.
     * @param lonRib
     *            The longitude rib to make normals for.
     * @param radius
     *            The radius of the sphere.
     * @return The normals corresponding to the given quad.
     */
    private List<VecF3> makeNormals(VecF3[] quad) {
        List<VecF3> normals = new ArrayList<VecF3>();

        normals.add(VectorFMath.normalize(quad[0]));
        normals.add(VectorFMath.normalize(quad[1]));
        normals.add(VectorFMath.normalize(quad[2]));

        normals.add(VectorFMath.normalize(quad[3]));
        normals.add(VectorFMath.normalize(quad[4]));
        normals.add(VectorFMath.normalize(quad[5]));

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
     * @param radius
     *            The radius of the sphere.
     * @return The texture coordinates corresponding to the given quad.
     */
    private List<VecF3> makeTexCoords(VecF3[] quad, int latRib, int lonRib) {
        List<VecF3> texCoords = new ArrayList<VecF3>();

        if (texCoordsIn3D) {
            texCoords.add((quad[0].add(new VecF3(1f, 1f, 1f))).div(2f));
            texCoords.add((quad[1].add(new VecF3(1f, 1f, 1f))).div(2f));
            texCoords.add((quad[2].add(new VecF3(1f, 1f, 1f))).div(2f));

            texCoords.add((quad[3].add(new VecF3(1f, 1f, 1f))).div(2f));
            texCoords.add((quad[4].add(new VecF3(1f, 1f, 1f))).div(2f));
            texCoords.add((quad[5].add(new VecF3(1f, 1f, 1f))).div(2f));
        } else {
            texCoords.add(new VecF3(((float) lonRib / (float) lonRibs), ((float) latRib / (float) latRibs), 0));
            texCoords.add(new VecF3(((lonRib + 1f) / lonRibs), ((latRib + 1f) / latRibs), 0));
            texCoords.add(new VecF3(((lonRib + 1f) / lonRibs), ((float) latRib / (float) latRibs), 0));

            texCoords.add(new VecF3(((float) lonRib / lonRibs), (((float) latRib / (float) latRibs)), 0));
            texCoords.add(new VecF3(((float) lonRib / lonRibs), ((latRib + 1f) / latRibs), 0));
            texCoords.add(new VecF3(((lonRib + 1f) / lonRibs), ((latRib + 1f) / latRibs), 0));
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
