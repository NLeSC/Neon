package nl.esciencecenter.esight.models;

import java.nio.FloatBuffer;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.ESightGLEventListener;
import nl.esciencecenter.esight.datastructures.GLSLAttrib;
import nl.esciencecenter.esight.datastructures.VBO;
import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.shaders.ShaderProgram;

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
 * General extendible class representing a model with a {@link VBO}.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public abstract class Model {
    /**
     * The OpenGL-internal vertex format rules the vertexes in the VBO can
     * follow.
     */
    public static enum VertexFormat {
        TRIANGLES, POINTS, LINES
    };

    /**
     * The OpenGL-internal vertex format rules the vertexes in the VBO are going
     * to follow.
     */
    private VertexFormat format;

    /** The storage buffers for the standard elements of a model. */
    private FloatBuffer vertices;

    private FloatBuffer normals;

    private FloatBuffer texCoords;

    /** The resulting {@link VBO}. */
    private VBO vbo;

    /** The number of vertices stored in this model. */
    private int numVertices;

    /** The state of this model. */
    private boolean initialized = false;

    /**
     * General (extensible) constructor for this class. Initializes storage to 0
     * / null.
     * 
     * @param format
     *            The {@link VertexFormat} used by this model.
     */
    public Model(VertexFormat format) {
        vertices = null;
        normals = null;
        texCoords = null;
        numVertices = 0;
        this.format = format;
    }

    /**
     * Initializes the model by constructing the {@link VBO} out of the
     * vertices, normals and texCoords buffers.
     * 
     * @param gl
     *            The global openGL instance.
     */
    public void init(GL3 gl) {
        if (!initialized) {
            GLSLAttrib vAttrib = new GLSLAttrib(getVertices(), "MCvertex", GLSLAttrib.SIZE_FLOAT, 4);
            GLSLAttrib nAttrib = new GLSLAttrib(getNormals(), "MCnormal", GLSLAttrib.SIZE_FLOAT, 3);
            GLSLAttrib tAttrib = new GLSLAttrib(getTexCoords(), "MCtexCoord", GLSLAttrib.SIZE_FLOAT, 3);

            setVbo(new VBO(gl, vAttrib, nAttrib, tAttrib));

            initialized = true;
        }
    }

    /**
     * Deletes this model and its {@link VBO} nicely from memory.
     * 
     * @param gl
     *            The global openGL instance.
     */
    public void delete(GL3 gl) {
        setVertices(null);
        setNormals(null);
        setTexCoords(null);

        if (initialized) {
            getVbo().delete(gl);
        }
    }

    /**
     * Getter method for the {@link VBO}.
     * 
     * @return the {@link VBO} constructed by this class.
     */
    public VBO getVBO() throws UninitializedException {
        if (initialized) {
            return getVbo();
        } else {
            throw new UninitializedException();
        }
    }

    /**
     * Getter method for the number of vertices represented by this Model and
     * stored in its {@link ESightGLEventListener}
     * 
     * @return The number of vertices.
     */
    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Draw method for this model. Links its VBO attributes and calls OpenGL
     * DrawArrays.
     * 
     * @param gl
     *            The global openGL instance.
     * @param program
     *            The shader program to be used for this drawing instance.
     * @throws UninitializedException
     */
    public void draw(GL3 gl, ShaderProgram program) throws UninitializedException {
        if (initialized) {
            getVbo().bind(gl);

            program.linkAttribs(gl, getVbo().getAttribs());

            if (getFormat() == VertexFormat.TRIANGLES) {
                gl.glDrawArrays(GL3.GL_TRIANGLES, 0, getNumVertices());
            } else if (getFormat() == VertexFormat.POINTS) {
                gl.glDrawArrays(GL3.GL_POINTS, 0, getNumVertices());
            } else if (getFormat() == VertexFormat.LINES) {
                gl.glDrawArrays(GL3.GL_LINES, 0, getNumVertices());
            }
        } else {
            throw new UninitializedException();
        }
    }

    /**
     * Getter for format.
     * 
     * @return the format.
     */
    public VertexFormat getFormat() {
        return format;
    }

    /**
     * Setter for format.
     * 
     * @param format
     *            the format to set
     */
    public void setFormat(VertexFormat format) {
        this.format = format;
    }

    /**
     * Getter for vertices.
     * 
     * @return the vertices.
     */
    public FloatBuffer getVertices() {
        return vertices;
    }

    /**
     * Setter for vertices.
     * 
     * @param vertices
     *            the vertices to set
     */
    public void setVertices(FloatBuffer vertices) {
        this.vertices = vertices;
    }

    /**
     * Getter for normals.
     * 
     * @return the normals.
     */
    public FloatBuffer getNormals() {
        return normals;
    }

    /**
     * Setter for normals.
     * 
     * @param normals
     *            the normals to set
     */
    public void setNormals(FloatBuffer normals) {
        this.normals = normals;
    }

    /**
     * Getter for texCoords.
     * 
     * @return the texCoords.
     */
    public FloatBuffer getTexCoords() {
        return texCoords;
    }

    /**
     * Setter for texCoords.
     * 
     * @param texCoords
     *            the texCoords to set
     */
    public void setTexCoords(FloatBuffer texCoords) {
        this.texCoords = texCoords;
    }

    /**
     * Getter for vbo.
     * 
     * @return the vbo.
     */
    public VBO getVbo() {
        return vbo;
    }

    /**
     * Setter for vbo.
     * 
     * @param vbo
     *            the vbo to set
     */
    public void setVbo(VBO vbo) {
        this.vbo = vbo;
    }

    /**
     * Setter for numVertices.
     * 
     * @param numVertices
     *            the numVertices to set
     */
    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
    }
}
