package nl.esciencecenter.esight.shaders;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.datastructures.GLSLAttrib;
import nl.esciencecenter.esight.datastructures.VBO;
import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.math.MatrixF;
import nl.esciencecenter.esight.math.VectorF;
import nl.esciencecenter.esight.math.VectorS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.common.nio.Buffers;

/* Copyright 2013 Netherlands eScience Center
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
 * This library's interpretation of a Shader program. Different formats for
 * shader source inclusion are possible (files, strings). Shader programs can be
 * constructed consisting of vertex shader and fragment shader, and an optional
 * geometry shader.
 * 
 * Shader Programs made with this class have an internal storage for GLSL
 * uniform variables, and perform checks on use to see if these variables have
 * been set by the user via the setUniform methods.
 * 
 * The typical/correct lifecycle of a {@link ShaderProgram} is:
 * 
 * <pre>
 * <code> 
 * 1. Create, @see {@link ShaderProgramLoader}
 * 2. Init
 * 
 * while(displayCycle) { 
 *   3. Set Uniforms
 *   4. Link attributes / Vertex Buffer Object, @see {@link VBO}
 *   5. Use
 * }
 * 
 * 6. delete
 * </code>
 * </pre>
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class ShaderProgram {
    private final static Logger logger = LoggerFactory.getLogger(ShaderProgram.class);

    public int pointer;
    private final VertexShader vs;
    private GeometryShader gs;
    private final FragmentShader fs;

    private final HashMap<String, FloatBuffer> uniformFloatMatrices;
    private final HashMap<String, FloatBuffer> uniformFloatVectors;
    private final HashMap<String, ShortBuffer> uniformShortVectors;
    private final HashMap<String, Boolean> uniformBooleans;
    private final HashMap<String, Integer> uniformInts;
    private final HashMap<String, Float> uniformFloats;

    private boolean geometry_enabled = false;
    private boolean warningsGiven = false;

    /**
     * Basic constructor for ShaderProgram with Vertex and Fragment shaders.
     * 
     * @param vs
     *            The vertex shader to use for this program.
     * @param fs
     *            The fragment shader to use for this program.
     */
    public ShaderProgram(VertexShader vs, FragmentShader fs) {
        pointer = 0;
        this.vs = vs;
        this.fs = fs;
        uniformFloatMatrices = new HashMap<String, FloatBuffer>();
        uniformFloatVectors = new HashMap<String, FloatBuffer>();
        uniformShortVectors = new HashMap<String, ShortBuffer>();
        uniformBooleans = new HashMap<String, Boolean>();
        uniformInts = new HashMap<String, Integer>();
        uniformFloats = new HashMap<String, Float>();
    }

    /**
     * Basic constructor for ShaderProgram with Vertex and Fragment shaders.
     * 
     * @param vs
     *            The vertex shader to use for this program.
     * @param gs
     *            The geometry shader to use for this program.
     * @param fs
     *            The fragment shader to use for this program.
     */
    public ShaderProgram(VertexShader vs, GeometryShader gs, FragmentShader fs) {
        pointer = 0;
        this.vs = vs;
        this.gs = gs;
        this.fs = fs;
        uniformFloatMatrices = new HashMap<String, FloatBuffer>();
        uniformFloatVectors = new HashMap<String, FloatBuffer>();
        uniformShortVectors = new HashMap<String, ShortBuffer>();
        uniformBooleans = new HashMap<String, Boolean>();
        uniformInts = new HashMap<String, Integer>();
        uniformFloats = new HashMap<String, Float>();

        geometry_enabled = true;
    }

    /**
     * OpenGL initialization method.
     * 
     * @param gl
     *            The opengl instance.
     */
    public void init(GL3 gl) {
        pointer = gl.glCreateProgram();

        try {
            gl.glAttachShader(pointer, vs.getShaderPointer());
            if (geometry_enabled) {
                gl.glAttachShader(pointer, gs.getShaderPointer());
            }
            gl.glAttachShader(pointer, fs.getShaderPointer());
        } catch (UninitializedException e) {
            System.out.println("Shaders not initialized properly");
            System.exit(0);
        }

        gl.glLinkProgram(pointer);

        // Check for errors
        IntBuffer buf = Buffers.newDirectIntBuffer(1);
        gl.glGetProgramiv(pointer, GL3.GL_LINK_STATUS, buf);
        if (buf.get(0) == 0) {
            logger.error("Link error");
            printError(gl);
        }

        warningsGiven = false;
        checkCompatibility(vs, fs);
    }

    /**
     * Internal checking method to provide debugging information about shader
     * programming. Checks for compatibility of outputs of the vertex shader and
     * inputs for the fragment shader. logs warnings if not.
     * 
     * @param vs
     *            The vertex shader to check.
     * @param fs
     *            the fragment shader to check.
     * @return true if the given shaders are compatible.
     */
    @SuppressWarnings("rawtypes")
    private boolean checkCompatibility(Shader vs, Shader fs) {
        HashMap<String, Class> outs = vs.getOuts();
        HashMap<String, Class> ins = fs.getIns();

        boolean compatible = true;

        if (!warningsGiven || logger.isDebugEnabled()) {
            for (Map.Entry<String, Class> outEntry : outs.entrySet()) {
                if (!ins.containsKey(outEntry.getKey())) {
                    compatible = false;
                    logger.warn("SHADER WARNING: " + vs.getName() + " output variable " + outEntry.getKey()
                            + " has no matching input variable.");
                } else if (!ins.get(outEntry.getKey()).equals(outs.get(outEntry.getKey()))) {
                    compatible = false;
                    logger.warn("SHADER WARNING: " + vs.getName() + " Type of output variable " + outEntry.getKey()
                            + " does not match with type of input variable");
                }
            }
            for (Map.Entry<String, Class> inEntry : ins.entrySet()) {
                if (!outs.containsKey(inEntry.getKey())) {
                    compatible = false;
                    logger.warn("SHADER WARNING: " + fs.getName() + " input variable " + inEntry.getKey()
                            + " has no matching output variable.");
                } else if (!ins.get(inEntry.getKey()).equals(outs.get(inEntry.getKey()))) {
                    compatible = false;
                    logger.warn("SHADER WARNING: " + fs.getName() + " Type of input variable " + inEntry.getKey()
                            + " does not match with type of output variable");
                }
            }
        }

        return compatible;
    }

    /**
     * Check whether the uniform variables for this shader were set before use.
     * logs warnings if not.
     * 
     * @param vs
     *            The vertex shader to check.
     * @param fs
     *            the fragment shader to check.
     * @return true if the shader's uniforms are all present.
     */
    @SuppressWarnings("rawtypes")
    private boolean checkUniforms(Shader vs, Shader fs) {
        HashMap<String, Class> vsUniforms = vs.getUniforms();
        HashMap<String, Class> fsUniforms = fs.getUniforms();

        boolean allPresent = true;

        if (!warningsGiven || logger.isDebugEnabled()) {
            HashMap<String, Class> neededUniforms = new HashMap<String, Class>();
            neededUniforms.putAll(vsUniforms);
            neededUniforms.putAll(fsUniforms);

            for (Map.Entry<String, Class> uniformEntry : neededUniforms.entrySet()) {
                boolean thisEntryAvailable = false;
                if (uniformFloatMatrices.containsKey(uniformEntry.getKey())) {
                    thisEntryAvailable = true;
                } else if (uniformFloats.containsKey(uniformEntry.getKey())) {
                    thisEntryAvailable = true;
                } else if (uniformFloatVectors.containsKey(uniformEntry.getKey())) {
                    thisEntryAvailable = true;
                } else if (uniformInts.containsKey(uniformEntry.getKey())) {
                    thisEntryAvailable = true;
                } else if (uniformBooleans.containsKey(uniformEntry.getKey())) {
                    thisEntryAvailable = true;
                }

                if (!thisEntryAvailable) {
                    allPresent = false;
                    logger.warn("SHADER WARNING: " + fs.getName() + " uniform variable " + uniformEntry.getKey()
                            + " not present at use.");
                }
            }
        }

        return allPresent;
    }

    /**
     * Check whether the input attributes for this shader were set before use.
     * logs warnings if not.
     * 
     * @param vs
     *            The vertex shader to check.
     * @param attribs
     *            the list of attributes to check for.
     * @return true if the shader's uniforms are all present.
     */
    @SuppressWarnings("rawtypes")
    private boolean checkIns(Shader vs, GLSLAttrib... attribs) {
        HashMap<String, Class> vsIns = vs.getIns();
        boolean allPresent = true;

        if (!warningsGiven || logger.isDebugEnabled()) {
            for (Map.Entry<String, Class> inEntry : vsIns.entrySet()) {
                boolean thisEntryAvailable = false;
                for (GLSLAttrib attr : attribs) {
                    if (attr.name.compareTo(inEntry.getKey()) == 0) {
                        thisEntryAvailable = true;
                    }
                }

                if (!thisEntryAvailable) {
                    allPresent = false;
                    logger.warn("SHADER WARNING: " + vs.getName() + " input variable " + inEntry.getKey()
                            + " not present at use.");
                }
            }
        }

        return allPresent;
    }

    /**
     * OpenGL detachment method for the shaders of this program.
     * 
     * @param gl
     *            The openGL instance.
     */
    public void detachShaders(GL3 gl) {
        try {
            gl.glDetachShader(pointer, vs.getShaderPointer());
            gl.glDeleteShader(vs.getShaderPointer());

            if (geometry_enabled) {
                gl.glDetachShader(pointer, gs.getShaderPointer());
                gl.glDeleteShader(gs.getShaderPointer());
            }

            gl.glDetachShader(pointer, fs.getShaderPointer());
            gl.glDeleteShader(fs.getShaderPointer());

            // Check for errors
            IntBuffer buf = Buffers.newDirectIntBuffer(1);
            gl.glGetProgramiv(pointer, GL3.GL_LINK_STATUS, buf);
            if (buf.get(0) == 0) {
                logger.error("Link error");
                printError(gl);
            }
        } catch (UninitializedException e) {
            System.out.println("Shaders not initialized properly");
            System.exit(0);
        }
    }

    /**
     * use method for this shader. sets this shader as the current rendering
     * shader and loads all staged uniform variables. Also checks for the
     * presence of all uniforms and checks for additional opengl errors.
     * 
     * @param gl
     *            The opengl instance.
     * @throws UninitializedException
     *             if this ShaderProgram was used without initialization.
     */
    public void use(GL3 gl) throws UninitializedException {
        if (pointer == 0)
            throw new UninitializedException();

        gl.glUseProgram(pointer);

        for (Entry<String, FloatBuffer> var : uniformFloatMatrices.entrySet()) {
            passUniformMat(gl, var.getKey(), var.getValue());
        }
        for (Entry<String, FloatBuffer> var : uniformFloatVectors.entrySet()) {
            passUniformVec(gl, var.getKey(), var.getValue());
        }
        for (Entry<String, Boolean> var : uniformBooleans.entrySet()) {
            passUniform(gl, var.getKey(), var.getValue());
        }
        for (Entry<String, Integer> var : uniformInts.entrySet()) {
            passUniform(gl, var.getKey(), var.getValue());
        }
        for (Entry<String, Float> var : uniformFloats.entrySet()) {
            passUniform(gl, var.getKey(), var.getValue());
        }

        checkUniforms(vs, fs);

        // Check for errors
        IntBuffer buf = Buffers.newDirectIntBuffer(1);
        gl.glGetProgramiv(pointer, GL3.GL_LINK_STATUS, buf);
        if (buf.get(0) == 0) {
            logger.error("Use error");
            printError(gl);
        }
    }

    /**
     * Link method for linking the attribute objects to the shader inputs.
     * 
     * @param gl
     *            The opengl instance.
     * @param attribs
     *            The list of attributes to link.
     */
    public void linkAttribs(GL3 gl, GLSLAttrib... attribs) {
        int nextStart = 0;
        for (GLSLAttrib attrib : attribs) {
            int ptr = gl.glGetAttribLocation(pointer, attrib.name);
            gl.glVertexAttribPointer(ptr, attrib.vectorSize, GL3.GL_FLOAT, false, 0, nextStart);
            gl.glEnableVertexAttribArray(ptr);

            nextStart += attrib.buffer.capacity() * Buffers.SIZEOF_FLOAT;
        }

        checkIns(vs, attribs);

        warningsGiven = true;
    }

    /**
     * Internal method for getting the error code and message from opengl and
     * logging them.
     * 
     * @param gl
     *            The opengl instance
     */
    private void printError(GL3 gl) {
        IntBuffer buf = Buffers.newDirectIntBuffer(1);
        gl.glGetProgramiv(pointer, GL3.GL_INFO_LOG_LENGTH, buf);
        int logLength = buf.get(0);
        ByteBuffer reason = ByteBuffer.wrap(new byte[logLength]);
        gl.glGetProgramInfoLog(pointer, logLength, null, reason);

        logger.error(new String(reason.array()));
    }

    /**
     * Staging method for a Uniform Float Vector variable. This will be given to
     * the GPU upon {@link ShaderProgram#use(GL3 gl)} of this ShaderProgram.
     * 
     * @param name
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The Vector to stage.
     */
    public void setUniformVector(String name, VectorF var) {
        if (!uniformFloatVectors.containsKey(name)) {
            warningsGiven = false;
        }
        uniformFloatVectors.put(name, var.asBuffer());

    }

    /**
     * Staging method for a Uniform Short Vector variable. This will be given to
     * the GPU upon {@link ShaderProgram#use(GL3 gl)} of this ShaderProgram.
     * 
     * @param name
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The Vector to stage.
     */
    public void setUniformVector(String name, VectorS var) {
        if (!uniformFloatVectors.containsKey(name)) {
            warningsGiven = false;
        }
        uniformShortVectors.put(name, var.asBuffer());

    }

    /**
     * Staging method for a Uniform Float Matrix variable. This will be given to
     * the GPU upon {@link ShaderProgram#use(GL3 gl)} of this ShaderProgram.
     * 
     * @param name
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The Matrix to stage.
     */
    public void setUniformMatrix(String name, MatrixF var) {
        if (!uniformFloatMatrices.containsKey(name)) {
            warningsGiven = false;
        }
        uniformFloatMatrices.put(name, var.asBuffer());
    }

    /**
     * Staging method for a Uniform Boolean variable. This will be given to the
     * GPU upon {@link ShaderProgram#use(GL3 gl)} of this ShaderProgram.
     * 
     * @param name
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The Boolean to stage.
     */

    public void setUniform(String name, Boolean var) {
        if (!uniformBooleans.containsKey(name)) {
            warningsGiven = false;
        }
        uniformBooleans.put(name, var);
    }

    /**
     * Staging method for a Uniform Integer variable. This will be given to the
     * GPU upon {@link ShaderProgram#use(GL3 gl)} of this ShaderProgram.
     * 
     * @param name
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The Integer to stage.
     */

    public void setUniform(String name, Integer var) {
        if (!uniformInts.containsKey(name)) {
            warningsGiven = false;
        }
        uniformInts.put(name, var);
    }

    /**
     * Staging method for a Uniform Float variable. This will be given to the
     * GPU upon {@link ShaderProgram#use(GL3 gl)} of this ShaderProgram.
     * 
     * @param name
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The Float to stage.
     */
    public void setUniform(String name, Float var) {
        if (!uniformFloats.containsKey(name)) {
            warningsGiven = false;
        }
        uniformFloats.put(name, var);
    }

    /**
     * Method used to pass uniform variables directly to the shader without
     * staging them beforehand. Using this method to set shader variables
     * interferes directly with the error checking code present in this class.
     * Use is not recommended. Use {@link #setUniformVector(String, VectorF)}
     * instead.
     * 
     * @param gl
     *            The OpenGL instance.
     * @param pointerNameInShader
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The uniform variable to pas to the shader.
     */
    public void passUniformVec(GL3 gl, String pointerNameInShader, FloatBuffer var) {
        int ptr = gl.glGetUniformLocation(pointer, pointerNameInShader);

        int vecSize = var.capacity();
        if (vecSize == 1) {
            gl.glUniform1fv(ptr, 1, var);
        } else if (vecSize == 2) {
            gl.glUniform2fv(ptr, 1, var);
        } else if (vecSize == 3) {
            gl.glUniform3fv(ptr, 1, var);
        } else if (vecSize == 4) {
            gl.glUniform4fv(ptr, 1, var);
        }
    }

    /**
     * Method used to pass uniform variables directly to the shader without
     * staging them beforehand. Using this method to set shader variables
     * interferes directly with the error checking code present in this class.
     * Use is not recommended. Use {@link #setUniformVector(String, VectorF)}
     * instead.
     * 
     * @param gl
     *            The OpenGL instance.
     * @param pointerNameInShader
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The uniform variable to pas to the shader.
     */
    public void passUniformVecArray(GL3 gl, String pointerNameInShader, FloatBuffer var, int vecSize, int count) {
        int ptr = gl.glGetUniformLocation(pointer, pointerNameInShader);

        if (vecSize == 1) {
            gl.glUniform1fv(ptr, count, var);
        } else if (vecSize == 2) {
            gl.glUniform2fv(ptr, count, var);
        } else if (vecSize == 3) {
            gl.glUniform3fv(ptr, count, var);
        } else if (vecSize == 4) {
            gl.glUniform4fv(ptr, count, var);
        }
    }

    /**
     * Method used to pass uniform variables directly to the shader without
     * staging them beforehand. Using this method to set shader variables
     * interferes directly with the error checking code present in this class.
     * Use is not recommended. Use {@link #setUniformVector(String, VectorS)}
     * instead.
     * 
     * @param gl
     *            The OpenGL instance.
     * @param pointerNameInShader
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The uniform variable to pas to the shader.
     */
    public void passUniformVecArray(GL3 gl, String pointerNameInShader, IntBuffer var, int vecSize, int count) {
        int ptr = gl.glGetUniformLocation(pointer, pointerNameInShader);

        if (vecSize == 1) {
            gl.glUniform1iv(ptr, count, var);
        } else if (vecSize == 2) {
            gl.glUniform2iv(ptr, count, var);
        } else if (vecSize == 3) {
            gl.glUniform3iv(ptr, count, var);
        } else if (vecSize == 4) {
            gl.glUniform4iv(ptr, count, var);
        }
    }

    /**
     * Method used to pass uniform variables directly to the shader without
     * staging them beforehand. Using this method to set shader variables
     * interferes directly with the error checking code present in this class.
     * Use is not recommended. Use {@link #setUniformMatrix(String, MatrixF)}
     * instead.
     * 
     * @param gl
     *            The OpenGL instance.
     * @param pointerNameInShader
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The uniform variable to pas to the shader.
     */
    public void passUniformMat(GL3 gl, String pointerNameInShader, FloatBuffer var) {
        int ptr = gl.glGetUniformLocation(pointer, pointerNameInShader);

        int matSize = var.capacity();
        if (matSize == 4) {
            gl.glUniformMatrix2fv(ptr, 1, true, var);
        } else if (matSize == 9) {
            gl.glUniformMatrix3fv(ptr, 1, true, var);
        } else if (matSize == 16) {
            gl.glUniformMatrix4fv(ptr, 1, true, var);
        }
    }

    /**
     * Method used to pass uniform variables directly to the shader without
     * staging them beforehand. Using this method to set shader variables
     * interferes directly with the error checking code present in this class.
     * Use is not recommended. Use {@link #setUniform(String, boolean)} instead.
     * 
     * @param gl
     *            The OpenGL instance.
     * @param pointerNameInShader
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The uniform variable to pas to the shader.
     */
    public void passUniform(GL3 gl, String pointerNameInShader, boolean var) {
        int ptr = gl.glGetUniformLocation(pointer, pointerNameInShader);
        int passable = 0;
        if (var == true) {
            passable = 1;
        }
        gl.glUniform1i(ptr, passable);
    }

    /**
     * Method used to pass uniform variables directly to the shader without
     * staging them beforehand. Using this method to set shader variables
     * interferes directly with the error checking code present in this class.
     * Use is not recommended. Use {@link #setUniform(String, int)} instead.
     * 
     * @param gl
     *            The OpenGL instance.
     * @param pointerNameInShader
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The uniform variable to pas to the shader.
     */

    public void passUniform(GL3 gl, String pointerNameInShader, int var) {
        int ptr = gl.glGetUniformLocation(pointer, pointerNameInShader);
        gl.glUniform1i(ptr, var);
    }

    /**
     * Method used to pass uniform variables directly to the shader without
     * staging them beforehand. Using this method to set shader variables
     * interferes directly with the error checking code present in this class.
     * Use is not recommended. Use {@link #setUniform(String, float)} instead.
     * 
     * @param gl
     *            The OpenGL instance.
     * @param pointerNameInShader
     *            The name in the GLSL code for this uniform variable.
     * @param var
     *            The uniform variable to pas to the shader.
     */

    public void passUniform(GL3 gl, String pointerNameInShader, float var) {
        int ptr = gl.glGetUniformLocation(pointer, pointerNameInShader);
        gl.glUniform1f(ptr, var);
    }

    /**
     * OpenGL deletion method.
     * 
     * @param gl
     *            The opengl intance.
     */
    public void delete(GL3 gl) {
        gl.glDeleteProgram(pointer);
    }
}
