package nl.esciencecenter.esight.shaders;

import java.io.File;
import java.io.FileNotFoundException;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.CompilationFailedException;

/**
 * Vertex shader object representation.
 * These shaders represent the first step of the rendering
 * process, where every vertex is f.e. transformed and rotated.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public class VertexShader extends Shader {
    /**
     * Constructor for Geometry shader.
     * 
     * @param shaderName
     *            The library-internal name of this shader.
     * @param file
     *            The file containing the shader source code.
     * @throws FileNotFoundException
     *             If the file was absent.
     */
    public VertexShader(String shaderName, File file) throws FileNotFoundException {
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
    public VertexShader(String shaderName, String shaderCode) throws FileNotFoundException {
        super(shaderName, shaderCode);
    }

    @Override
    public void init(GL3 gl) throws CompilationFailedException {
        shaderPointer = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        super.init(gl);
    }
}
