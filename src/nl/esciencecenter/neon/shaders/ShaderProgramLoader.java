package nl.esciencecenter.neon.shaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL3;

import nl.esciencecenter.neon.exceptions.CompilationFailedException;
import nl.esciencecenter.neon.exceptions.UninitializedException;
import nl.esciencecenter.neon.math.MatrixF;
import nl.esciencecenter.neon.math.VectorF;

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
 * Loader/convenience class for {@link ShaderProgram} instances. Different
 * formats for shader source inclusion are possible (files, strings). Shader
 * programs can be constructed consisting of {@link VertexShader} and
 * {@link FragmentShader}, and an optional {@link GeometryShader}.
 * 
 * @see ShaderProgram
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public class ShaderProgramLoader {
    /** Storage for the Shader Programs */
    private List<ShaderProgram> programs;

    /**
     * Constructor, initializes storage.
     */
    public ShaderProgramLoader() {
        programs = new ArrayList<ShaderProgram>();
    }

    /**
     * Creation method for {@link ShaderProgram} instances. Reads, parses,
     * checks, compiles and initializes Shader source code. Then stores a
     * reference to it.
     * 
     * @param gl
     *            The global openGL instance.
     * @param programName
     *            Internal name for this Shader Program.
     * @param vsSourceFile
     *            Source file for {@link VertexShader}.
     * @param gsSourceFile
     *            Source file for {@link GeometryShader}.
     * @param fsSourceFile
     *            Source file for {@link FragmentShader}.
     * @return The shader program just created.
     * @throws FileNotFoundException
     *             if one of the source files can not be found.
     * @throws CompilationFailedException
     *             if one of the source compilations generates an error.
     */
    public ShaderProgram createProgram(GL3 gl, String programName, File vsSourceFile, File gsSourceFile,
            File fsSourceFile) throws FileNotFoundException, CompilationFailedException {
        VertexShader vs = new VertexShader(programName + " : Vertex Shader", vsSourceFile);
        vs.init(gl);
        GeometryShader gs = new GeometryShader(programName + " : Geometry Shader", gsSourceFile);
        gs.init(gl);
        FragmentShader fs = new FragmentShader(programName + " : Fragment Shader", fsSourceFile);
        fs.init(gl);

        ShaderProgram program = new ShaderProgram(vs, gs, fs);
        program.init(gl);
        programs.add(program);

        return program;
    }

    /**
     * Creation method for {@link ShaderProgram} instances. Reads, parses,
     * checks, compiles and initializes Shader source code. Then stores a
     * reference to it.
     * 
     * @param gl
     *            The global openGL instance.
     * @param programName
     *            Internal name for this Shader Program.
     * @param vsSourceFile
     *            Source for {@link VertexShader}.
     * @param fsSourceFile
     *            Source for {@link FragmentShader}.
     * @return The shader program just created.
     * @throws FileNotFoundException
     *             if one of the source files can not be found.
     * @throws CompilationFailedException
     *             if one of the source compilations generates an error.
     */
    public ShaderProgram createProgram(GL3 gl, String programName, File vsSourceFile, File fsSourceFile)
            throws FileNotFoundException, CompilationFailedException {
        VertexShader vs = new VertexShader(programName + " : Vertex Shader", vsSourceFile);
        vs.init(gl);
        FragmentShader fs = new FragmentShader(programName + " : Fragment Shader", fsSourceFile);
        fs.init(gl);

        ShaderProgram program = new ShaderProgram(vs, fs);
        program.init(gl);
        programs.add(program);

        return program;
    }

    /**
     * Creation method for {@link ShaderProgram} instances. Reads, parses,
     * checks, compiles and initializes Shader source code. Then stores a
     * reference to it.
     * 
     * @param gl
     *            The global openGL instance.
     * @param programName
     *            Internal name for this Shader Program.
     * @param vsSourceFile
     *            Source for {@link VertexShader}.
     * @param fsSourceFile
     *            Source for {@link FragmentShader}.
     * @return The shader program just created.
     * @throws FileNotFoundException
     *             if one of the source files can not be found.
     * @throws CompilationFailedException
     *             if one of the source compilations generates an error.
     */
    public ShaderProgram createProgram(GL3 gl, String programName, String vsSourceCode, File fsSourceFile)
            throws FileNotFoundException, CompilationFailedException {
        VertexShader vs = new VertexShader(programName + " : Vertex Shader", vsSourceCode);
        vs.init(gl);
        FragmentShader fs = new FragmentShader(programName + " : Fragment Shader", fsSourceFile);
        fs.init(gl);

        ShaderProgram program = new ShaderProgram(vs, fs);
        program.init(gl);
        programs.add(program);

        return program;
    }

    /**
     * Creation method for {@link ShaderProgram} instances. Reads, parses,
     * checks, compiles and initializes Shader source code. Then stores a
     * reference to it.
     * 
     * @param gl
     *            The global openGL instance.
     * @param programName
     *            Internal name for this Shader Program.
     * @param vsSourceFile
     *            Source for {@link VertexShader}.
     * @param fsSourceFile
     *            Source for {@link FragmentShader}.
     * @return The shader program just created.
     * @throws FileNotFoundException
     *             if one of the source files can not be found.
     * @throws CompilationFailedException
     *             if one of the source compilations generates an error.
     */
    public ShaderProgram createProgram(GL3 gl, String programName, File vsSourceFile, String fsSourceCode)
            throws FileNotFoundException, CompilationFailedException {
        VertexShader vs = new VertexShader(programName + " : Vertex Shader", vsSourceFile);
        vs.init(gl);
        FragmentShader fs = new FragmentShader(programName + " : Fragment Shader", fsSourceCode);
        fs.init(gl);

        ShaderProgram program = new ShaderProgram(vs, fs);
        program.init(gl);
        programs.add(program);

        return program;
    }

    /**
     * Creation method for {@link ShaderProgram} instances. Reads, parses,
     * checks, compiles and initializes Shader source code. Then stores a
     * reference to it.
     * 
     * @param gl
     *            The global openGL instance.
     * @param programName
     *            Internal name for this Shader Program.
     * @param vsSourceFile
     *            Source for {@link VertexShader}.
     * @param fsSourceFile
     *            Source for {@link FragmentShader}.
     * @return The shader program just created.
     * @throws FileNotFoundException
     *             if one of the source files can not be found.
     * @throws CompilationFailedException
     *             if one of the source compilations generates an error.
     */
    public ShaderProgram createProgram(GL3 gl, String programName, String vsSourceCode, String fsSourceCode)
            throws FileNotFoundException, CompilationFailedException {
        VertexShader vs = new VertexShader(programName + " : Vertex Shader", vsSourceCode);
        vs.init(gl);
        FragmentShader fs = new FragmentShader(programName + " : Fragment Shader", fsSourceCode);
        fs.init(gl);

        ShaderProgram program = new ShaderProgram(vs, fs);
        program.init(gl);
        programs.add(program);

        return program;
    }

    /**
     * Creation method for {@link ShaderProgram} instances. Compiles and
     * initializes predefined Shader source code. Then stores a reference to it.
     * 
     * @param gl
     *            The global openGL instance.
     * @param vs
     *            A predefined {@link VertexShader}
     * @param fs
     *            A predefined {@link FragmentShader}
     * @return The shader program just created.
     */
    public ShaderProgram createProgram(GL3 gl, VertexShader vs, FragmentShader fs) {
        ShaderProgram program = new ShaderProgram(vs, fs);
        program.init(gl);
        programs.add(program);
        return program;
    }

    /**
     * Deletes a {@link ShaderProgram} instance from the loader storage in the
     * proper manner.
     * 
     * @param gl
     *            The global openGL instance.
     * @param program
     *            The program to be deleted.
     * @throws UninitializedException
     */
    public void deleteProgram(GL3 gl, ShaderProgram program) throws UninitializedException {
        ArrayList<ShaderProgram> temp = new ArrayList<ShaderProgram>();
        for (ShaderProgram entry : programs) {
            if (entry == program) {
                entry.detachShaders(gl);
                entry.delete(gl);
            } else {
                temp.add(entry);
            }
        }
        programs = temp;
    }

    /**
     * Deletes ALL {@link ShaderProgram} instances from the loader storage in
     * the proper manner.
     * 
     * @param gl
     *            The global openGL instance.
     * @throws UninitializedException
     */
    public void cleanup(GL3 gl) throws UninitializedException {
        for (ShaderProgram entry : programs) {
            entry.detachShaders(gl);
            entry.delete(gl);
        }
        programs.clear();
    }

    /**
     * Stages a given variable for all currently loaded {@link ShaderProgram}
     * instances. (use only in exceptional circumstances)
     * 
     * @param pointerNameInShader
     *            The name of the variable in the GLSL source code.
     * @param var
     *            The value to be staged.
     */
    public void setUniformVector(String pointerNameInShader, VectorF var) {
        for (ShaderProgram p : programs) {
            p.setUniformVector(pointerNameInShader, var);
        }
    }

    /**
     * Stages a given variable for all currently loaded {@link ShaderProgram}
     * instances. (use only in exceptional circumstances)
     * 
     * @param pointerNameInShader
     *            The name of the variable in the GLSL source code.
     * @param var
     *            The value to be staged.
     */
    public void setUniformMatrix(String pointerNameInShader, MatrixF var) {
        for (ShaderProgram p : programs) {
            p.setUniformMatrix(pointerNameInShader, var);
        }
    }

    /**
     * Stages a given variable for all currently loaded {@link ShaderProgram}
     * instances. (use only in exceptional circumstances)
     * 
     * @param pointerNameInShader
     *            The name of the variable in the GLSL source code.
     * @param var
     *            The value to be staged.
     */
    public void setUniform(String pointerNameInShader, int var) {
        for (ShaderProgram p : programs) {
            p.setUniform(pointerNameInShader, var);
        }
    }

    /**
     * Stages a given variable for all currently loaded {@link ShaderProgram}
     * instances. (use only in exceptional circumstances)
     * 
     * @param pointerNameInShader
     *            The name of the variable in the GLSL source code.
     * @param var
     *            The value to be staged.
     */
    public void setUniform(String pointerNameInShader, float var) {
        for (ShaderProgram p : programs) {
            p.setUniform(pointerNameInShader, var);
        }
    }

    /**
     * Stages a given variable for all currently loaded {@link ShaderProgram}
     * instances. (use only in exceptional circumstances)
     * 
     * @param pointerNameInShader
     *            The name of the variable in the GLSL source code.
     * @param var
     *            The value to be staged.
     */
    public void setUniform(String pointerNameInShader, boolean var) {
        for (ShaderProgram p : programs) {
            if (var) {
                p.setUniform(pointerNameInShader, 1);
            } else {
                p.setUniform(pointerNameInShader, 0);
            }
        }
    }
}
