package nl.esciencecenter.neon.shaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.media.opengl.GL3;

import nl.esciencecenter.neon.exceptions.CompilationFailedException;
import nl.esciencecenter.neon.exceptions.UninitializedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.common.nio.Buffers;

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
 * Generic abstract shader class with most of the implementation specifics
 * shared by all shaders.
 * 
 * Provides object-oriented interface, I/O, compilation and error checking for
 * GLSL shaders.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
@SuppressWarnings("rawtypes")
public abstract class Shader {
    private static final Logger logger = LoggerFactory.getLogger(Shader.class);

    /** The library-internal name for this shader. */
    private final String shaderName;
    /** The source-file name for this shader. */
    private final String filename;
    /** The source code for this shader. */
    private final String[] source;

    private final Map<String, Class> ins, outs, uniforms;

    /**
     * The OpenGL-internal pointer of this shader, set by initializing said
     * shader.
     */
    private int shaderPointer = -1;

    /**
     * Constructor, reads the GLSL shader source code from file.
     * 
     * @param shaderName
     *            The internal name for this shader.
     * @param file
     *            The file with the source code for this shader.
     * @throws FileNotFoundException
     *             If the file given was not found.
     */
    public Shader(String shaderName, File file) throws FileNotFoundException {
        this.shaderName = shaderName;
        this.filename = file.getName();

        // Read file
        StringBuffer buf = new StringBuffer();
        Scanner scan = new Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream(file.getPath()));

        ins = new HashMap<String, Class>();
        outs = new HashMap<String, Class>();
        uniforms = new HashMap<String, Class>();

        while (scan.hasNext()) {
            String line = scan.nextLine();
            buf.append(line);
            buf.append("\n");

            parseVariables(line);
        }

        scan.close();

        source = new String[] { buf.toString() };
    }

    /**
     * Constructor, reads the given GLSL shader source code.
     * 
     * @param shaderName
     *            The internal name for this shader.
     * @param shaderCode
     *            The source code for this shader.
     */
    public Shader(String shaderName, String shaderCode) {
        this.shaderName = shaderName;
        this.filename = "";

        StringBuffer buf = new StringBuffer();
        Scanner scan = new Scanner(shaderCode);

        ins = new HashMap<String, Class>();
        outs = new HashMap<String, Class>();
        uniforms = new HashMap<String, Class>();

        while (scan.hasNext()) {
            String line = scan.nextLine();
            buf.append(line);
            buf.append("\n");

            parseVariables(line);
        }

        scan.close();

        source = new String[] { buf.toString() };
    }

    /**
     * Preparation method for Error-checking code that matches uniform variables
     * set with the setUniform methods of the {@link ShaderProgram} class to
     * their GLSL shader code counterparts.
     * 
     * @param line
     *            The line of shader code to check.
     */
    private void parseVariables(String line) {
        String[] trimmedLine = line.trim().split(";");
        String[] words = trimmedLine[0].split("[\\s,;]+");
        if (words[0].compareTo("in") == 0) {
            for (int i = 2; i < words.length; i++) {
                Class clazz = extractShaderParameterType(words[1]);
                ins.put(words[i], clazz);
            }
        } else if (words[0].compareTo("out") == 0) {
            for (int i = 2; i < words.length; i++) {
                Class clazz = extractShaderParameterType(words[1]);
                outs.put(words[i], clazz);
            }
        } else if (words[0].compareTo("uniform") == 0) {
            for (int i = 2; i < words.length; i++) {
                Class clazz = extractShaderParameterType(words[1]);
                uniforms.put(words[i], clazz);
            }
        }
    }

    private Class extractShaderParameterType(String word) {
        Class clazz = null;
        if (word.compareTo("vec2") == 0) {
            clazz = FloatBuffer.class;
        } else if (word.compareTo("vec3") == 0) {
            clazz = FloatBuffer.class;
        } else if (word.compareTo("vec4") == 0) {
            clazz = FloatBuffer.class;
        } else if (word.compareTo("float") == 0) {
            clazz = Float.class;
        } else if (word.compareTo("sampler2D") == 0) {
            clazz = Integer.class;
        } else if (word.compareTo("sampler3D") == 0) {
            clazz = Integer.class;
        } else if (word.compareTo("int") == 0) {
            clazz = Integer.class;
        } else if (word.compareTo("mat3") == 0) {
            clazz = FloatBuffer.class;
        } else if (word.compareTo("mat4") == 0) {
            clazz = FloatBuffer.class;
        }
        return clazz;
    }

    /**
     * Initialization method for any shader. Compiles and checks code.
     * 
     * @param gl
     *            The global openGL instance.
     * @throws CompilationFailedException
     *             If the compilation of the GLSL code generated any errors.
     */
    public void init(GL3 gl) throws CompilationFailedException {
        try {
            // First, give the source to OpenGL and compile
            gl.glShaderSource(getShaderPointer(), 1, source, (int[]) null, 0);
            gl.glCompileShader(getShaderPointer());

            // Receive compilation status
            IntBuffer buf = Buffers.newDirectIntBuffer(1);
            gl.glGetShaderiv(getShaderPointer(), GL3.GL_COMPILE_STATUS, buf);
            int status = buf.get(0);

            // Check the status
            if (status == GL3.GL_FALSE) {
                // Prepare for additional information
                gl.glGetShaderiv(getShaderPointer(), GL3.GL_INFO_LOG_LENGTH, buf);
                int logLength = buf.get(0);
                byte[] reason = new byte[logLength];

                // Get additional information
                gl.glGetShaderInfoLog(getShaderPointer(), logLength, null, 0, reason, 0);

                throw new CompilationFailedException("Compilation of " + filename + " failed, " + new String(reason));
            }
        } catch (UninitializedException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Getter for the OpenGL-internal pointer.
     * 
     * @return the OpenGL-internal Pointer to this shader.
     * @throws UninitializedException
     *             If the shader is not initialized before this method is
     *             called.
     */
    public int getShaderPointer() throws UninitializedException {
        if (shaderPointer == -1) {
            throw new UninitializedException();
        }
        return shaderPointer;
    }

    /**
     * Get the input attribute names (and their raw types) of this shader.
     * 
     * @return the HashMap containing the input attribute names (and their raw
     *         types) of this shader.
     */
    public Map<String, Class> getIns() {
        return ins;
    }

    /**
     * Get the output attribute names (and their raw types) of this shader.
     * 
     * @return the HashMap containing the output attribute names (and their raw
     *         types) of this shader.
     */
    public Map<String, Class> getOuts() {
        return outs;
    }

    /**
     * Get the uniform names (and their raw types) of this shader.
     * 
     * @return the HashMap containing the uniform names (and their raw types) of
     *         this shader.
     */
    public Map<String, Class> getUniforms() {
        return uniforms;
    }

    /**
     * Getter for this shader's library-internal name.
     * 
     * @return this shader's library-internal name.
     */
    public String getName() {
        return shaderName;
    }

    /**
     * Setter for shaderPointer.
     * 
     * @param shaderPointer
     *            the shaderPointer to set
     */
    public void setShaderPointer(int shaderPointer) {
        this.shaderPointer = shaderPointer;
    }
}
