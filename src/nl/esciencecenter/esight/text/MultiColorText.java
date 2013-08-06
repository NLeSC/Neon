package nl.esciencecenter.esight.text;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.datastructures.GLSLAttrib;
import nl.esciencecenter.esight.datastructures.VBO;
import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.math.Color4;
import nl.esciencecenter.esight.math.MatF4;
import nl.esciencecenter.esight.math.MatrixFMath;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VecF4;
import nl.esciencecenter.esight.math.VectorFMath;
import nl.esciencecenter.esight.models.BoundingBox;
import nl.esciencecenter.esight.models.Model;
import nl.esciencecenter.esight.shaders.ShaderProgram;
import nl.esciencecenter.esight.text.jogampExperimental.Font;
import nl.esciencecenter.esight.text.jogampExperimental.GlyphShape;
import nl.esciencecenter.esight.text.jogampExperimental.OutlineShape;
import nl.esciencecenter.esight.text.jogampExperimental.TypecastFont;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.graph.geom.Triangle;
import com.jogamp.graph.geom.Vertex;
import com.jogamp.graph.geom.opengl.SVertex;

/* Copyright 2013 Netherlands eScience Center
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
 * Multicolor-enabled text model. Relies _heavily_ on experimental and
 * undocumented jogamp additions for font and graph rendering (which I do not
 * fully understand).
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public class MultiColorText extends Model {
    private final static Logger logger = LoggerFactory.getLogger(MultiColorText.class);

    /** Initialization is needed for certain functions to work properly */
    private boolean initialized = false;

    /**
     * Private storage construct for glyph shapes (one per character in the
     * text)
     */
    private final Map<Integer, GlyphShape> glyphs;
    /**
     * Private storage construct for character colors (one per character in the
     * text)
     */
    private final Map<Integer, VecF4> colors;

    /** Buffer for final per-vertex colors */
    private FloatBuffer vertexColors;

    /** internal-use only bounding box for the model */
    private final BoundingBox bbox;

    /**
     * The previous string, as long as it doesn't change we need to do far less
     * work
     */
    private String cachedString;
    /**
     * The previous size, as long as it doesn't change we need to do far less
     * work
     */
    private int cachedSize;
    /**
     * The previous color, as long as it doesn't change we need to do far less
     * work
     */
    private Color4 cachedColor;

    /** The desired font for this text model */
    private final Font font;

    /**
     * Constructor, doesn't actually do any work, only prepares storage.
     * 
     * @param font
     *            The font for this text model.
     */
    public MultiColorText(Font font) {
        super(vertex_format.TRIANGLES);

        this.font = font;

        cachedString = "";

        this.bbox = new BoundingBox();
        colors = new HashMap<Integer, VecF4>();
        glyphs = new HashMap<Integer, GlyphShape>();

        setNumVertices(0);
    }

    public MultiColorText(GL3 gl, Font font, String text, Color4 initialColor, int fontSize) {
        super(vertex_format.TRIANGLES);

        this.font = font;

        cachedString = "";

        this.bbox = new BoundingBox();
        colors = new HashMap<Integer, VecF4>();
        glyphs = new HashMap<Integer, GlyphShape>();

        setString(gl, text, initialColor, fontSize);
    }

    @Override
    public void init(GL3 gl) {
        // We override because we do not have the normals information.

        if (!initialized) {
            GLSLAttrib vAttrib = new GLSLAttrib(getVertices(), "MCvertex", GLSLAttrib.SIZE_FLOAT, 4);
            GLSLAttrib cAttrib = new GLSLAttrib(vertexColors, "MCvertexColor", GLSLAttrib.SIZE_FLOAT, 4);

            setVbo(new VBO(gl, vAttrib, cAttrib));
        }
        initialized = true;
    }

    /**
     * Setter for the string. Recalculates the {@link VBO} afterwards.
     * 
     * @param gl
     *            The global openGL instance.
     * @param str
     *            The new string to be presented by this model.
     * @param basicColor
     *            The (new) base color of this model.
     * @param size
     */
    public void setString(GL3 gl, String str, Color4 basicColor, int size) {
        if (cachedString.compareTo(str) != 0 || cachedSize != size || !cachedColor.equals(basicColor)) {
            colors.clear();
            glyphs.clear();

            // Get the outline shapes for the current string in this font
            ArrayList<OutlineShape> shapes = ((TypecastFont) font).getOutlineShapes(str, size, SVertex.factory());

            // Make a set of glyph shapes from the outlines
            int numGlyps = shapes.size();

            for (int index = 0; index < numGlyps; index++) {
                if (shapes.get(index) == null) {
                    colors.put(index, null);
                    glyphs.put(index, null);
                    continue;
                }
                GlyphShape glyphShape = new GlyphShape(SVertex.factory(), shapes.get(index));

                if (glyphShape.getNumVertices() < 3) {
                    colors.put(index, null);
                    glyphs.put(index, null);
                    continue;
                }
                colors.put(index, basicColor);
                glyphs.put(index, glyphShape);
            }

            initialized = false;
            makeVBO(gl);
            this.cachedString = str;
            this.cachedSize = size;
            this.cachedColor = basicColor;
        }
    }

    /**
     * Makes the VBO from the glyphs and colors. Finalizes and initializes the
     * VBO.
     * 
     * @param gl
     *            The global openGL instance.
     */
    private void makeVBO(GL3 gl) {
        if (!initialized) {
            // Create list of vertices based on the glyph shapes
            ArrayList<Vertex> vertices = new ArrayList<Vertex>();
            ArrayList<VecF4> tmpVertexColors = new ArrayList<VecF4>();
            for (int i = 0; i < glyphs.size(); i++) {
                if (glyphs.get(i) != null) {
                    GlyphShape glyph = glyphs.get(i);
                    VecF4 glypColor = colors.get(i);

                    ArrayList<Triangle> gtris = glyph.triangulate();
                    for (Triangle t : gtris) {
                        vertices.add(t.getVertices()[0]);
                        vertices.add(t.getVertices()[1]);
                        vertices.add(t.getVertices()[2]);

                        tmpVertexColors.add(glypColor);
                        tmpVertexColors.add(glypColor);
                        tmpVertexColors.add(glypColor);
                    }
                }
            }

            // Transform the vertices from Vertex objects to Vec4 objects
            // and
            // update BoundingBox.
            VecF4[] myVertices = new VecF4[vertices.size()];
            int i = 0;
            for (Vertex v : vertices) {
                VecF3 vec = new VecF3(v.getX(), v.getY(), v.getZ());
                bbox.resize(vec);

                myVertices[i] = new VecF4(vec, 1f);

                i++;
            }

            if (getVbo() != null) {
                getVbo().delete(gl);
            }
            this.setVertices(VectorFMath.toBuffer(myVertices));
            this.vertexColors = VectorFMath.vec4ListToBuffer(tmpVertexColors);
            GLSLAttrib vAttrib = new GLSLAttrib(this.getVertices(), "MCvertex", GLSLAttrib.SIZE_FLOAT, 4);
            GLSLAttrib cAttrib = new GLSLAttrib(this.vertexColors, "MCvertexColor", GLSLAttrib.SIZE_FLOAT, 4);
            setVbo(new VBO(gl, vAttrib, cAttrib));

            this.setNumVertices(vertices.size());

            initialized = true;
        }
    }

    /**
     * Color any instance of the given substrings in the given colors.
     * 
     * @param gl
     *            The global openGL instance.
     * @param map
     *            The map of substrings and colors.
     */
    public void setSubstringColors(GL3 gl, Map<String, Color4> map) {
        for (Map.Entry<String, Color4> entry : map.entrySet()) {
            setSubstringColorWordBounded(gl, entry.getKey(), entry.getValue());
        }
    }

    /**
     * Color any instance of the given substring in the given color.
     * 
     * @param gl
     *            The global openGL instance.
     * @param subString
     *            The substring to color.
     * @param newColor
     *            The new color.
     */
    public void setSubstringColorWordBounded(GL3 gl, String subString, Color4 newColor) {
        if (cachedString.contains(subString) && subString.compareTo("") != 0) {
            Pattern p = Pattern.compile("\\b" + subString + "\\b");
            Matcher m = p.matcher(cachedString);

            int startIndex = 0;
            while (m.find(startIndex)) {
                startIndex = m.start();
                for (int i = 0; i < subString.length(); i++) {
                    colors.put(startIndex + i, newColor);
                }
                startIndex++; // read past to avoid never-ending loop
            }
        }
    }

    /**
     * Color any instance of the given substring in the given color.
     * 
     * @param gl
     *            The global openGL instance.
     * @param subString
     *            The substring to color.
     * @param newColor
     *            The new color.
     */
    public void setSubstringColor(GL3 gl, String subString, Color4 newColor) {
        if (cachedString.contains(subString) && subString.compareTo("") != 0) {
            int startIndex = cachedString.indexOf(subString);
            while (startIndex > -1) {
                for (int i = 0; i < subString.length(); i++) {
                    colors.put(startIndex + i, newColor);
                }
                startIndex = cachedString.indexOf(subString, startIndex + 1);
            }
        }
    }

    /**
     * Color the instance of the given substring at the given index in the given
     * color.
     * 
     * @param gl
     *            The global openGL instance.
     * @param startIndex
     *            The starting index of this substring.
     * 
     * @param subString
     *            The substring to color.
     * @param newColor
     *            The new color.
     */
    public void setSubstringAtIndexColor(GL3 gl, int startIndex, String subString, Color4 newColor) {
        if (cachedString.contains(subString) && subString.compareTo("") != 0) {
            for (int i = 0; i < subString.length(); i++) {
                colors.put(startIndex + i, newColor);
            }
        }
    }

    public void finalizeColorScheme(GL3 gl) {
        initialized = false;
        makeVBO(gl);
    }

    public void draw(GL3 gl, ShaderProgram program, float canvasWidth, float canvasHeight, float rasterPosX,
            float rasterPosY) throws UninitializedException {
        if (initialized) {
            program.setUniformMatrix("MVMatrix", getMVMatrixForHUD(canvasWidth, canvasHeight, rasterPosX, rasterPosY));
            program.setUniformMatrix("PMatrix", getPMatrixForHUD(canvasWidth, canvasHeight));

            try {
                program.use(gl);
            } catch (UninitializedException e) {
                logger.error(e.getMessage());
            }

            getVbo().bind(gl);

            program.linkAttribs(gl, getVbo().getAttribs());

            gl.glDrawArrays(GL3.GL_TRIANGLES, 0, getNumVertices());
        } else {
            throw new UninitializedException();
        }
    }

    @Override
    public String toString() {
        return cachedString;
    }

    /**
     * Convenience method to create a Modelview Matrix useful for HUD
     * projection.
     * 
     * @param canvasWidth
     *            The width of the HUD (canvas).
     * @param canvasHeight
     *            The height of the HUD (canvas).
     * @param rasterPosX
     *            The X coordinate on the HUD to paint the model at.
     * @param rasterPosY
     *            The Y coordinate on the HUD to paint the model at.
     * @return the Modelview matrix needed to paint at the given coordinates on
     *         the HUD.
     */
    private MatF4 getMVMatrixForHUD(float canvasWidth, float canvasHeight, float rasterPosX, float rasterPosY) {
        return new MatF4().mul(MatrixFMath.translate((rasterPosX / canvasWidth), (rasterPosY / canvasHeight), 0f));
    }

    /**
     * Convenience method to create a Perspective Matrix useful for HUD
     * projection.
     * 
     * @param canvasWidth
     *            The width of the HUD (canvas).
     * @param canvasHeight
     *            The height of the HUD (canvas).
     * @return the Perspective matrix needed to paint on the HUD.
     */
    private MatF4 getPMatrixForHUD(float canvasWidth, float canvasHeight) {
        return MatrixFMath.ortho(0f, canvasWidth, 0f, canvasHeight, -1f, 1f);
    }

}
