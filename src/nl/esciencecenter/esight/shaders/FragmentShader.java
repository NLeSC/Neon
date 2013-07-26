package nl.esciencecenter.esight.shaders;

import java.io.File;
import java.io.FileNotFoundException;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.CompilationFailedException;

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
 * Fragment shader object representation. These shaders represent the final
 * stage of the rendering process, where every proto-pixel is given a color.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public class FragmentShader extends Shader {

    /**
     * Constructor for Fragment shader.
     * 
     * @param shaderName
     *            The library-internal name of this shader.
     * @param file
     *            The file containing the shader source code.
     * @throws FileNotFoundException
     *             If the file was absent.
     */
    public FragmentShader(String shaderName, File file) throws FileNotFoundException {
        super(shaderName, file);
    }

    /**
     * Constructor for Fragment shader.
     * 
     * @param shaderName
     *            The library-internal name of this shader.
     * @param shaderCode
     *            The string containing the code for this shader.
     * @throws FileNotFoundException
     *             If the file was absent.
     */
    public FragmentShader(String shaderName, String shaderCode) throws FileNotFoundException {
        super(shaderName, shaderCode);
    }

    @Override
    public void init(GL3 gl) throws CompilationFailedException {
        shaderPointer = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
        super.init(gl);
    }
}
