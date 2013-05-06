package nl.esciencecenter.esight.shaders;

import java.io.File;
import java.io.FileNotFoundException;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.CompilationFailedException;

/**
 * Geometry shader object representation.
 * These shaders represent an optional intermediate step of the rendering
 * process, where every
 * vertex is potentially transformed into multiple vertices.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public class GeometryShader extends Shader {
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
    public GeometryShader(String shaderName, File file)
            throws FileNotFoundException {
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
    public GeometryShader(String shaderName, String shaderCode)
            throws FileNotFoundException {
        super(shaderName, shaderCode);
    }

    @Override
    public void init(GL3 gl) throws CompilationFailedException {
        shaderPointer = gl.glCreateShader(GL3.GL_GEOMETRY_SHADER);
        super.init(gl);
    }
}
