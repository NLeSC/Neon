package nl.esciencecenter.neon.models.graphs;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL3;

import nl.esciencecenter.neon.datastructures.GLSLAttrib;
import nl.esciencecenter.neon.datastructures.VBO;
import nl.esciencecenter.neon.exceptions.UninitializedException;
import nl.esciencecenter.neon.math.Color4;
import nl.esciencecenter.neon.math.Point4;
import nl.esciencecenter.neon.math.VecF4;
import nl.esciencecenter.neon.math.VectorFMath;
import nl.esciencecenter.neon.models.Model;
import nl.esciencecenter.neon.shaders.ShaderProgram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScatterPlot3D extends Model {
    private final static Logger LOGGER = LoggerFactory.getLogger(ScatterPlot3D.class);

    private final List<VecF4> points;
    private final List<VecF4> colors;

    private FloatBuffer vertexColors;
    private FloatBuffer newVertices, newColors;

    private boolean initialized = false;

    public ScatterPlot3D() {
        super(VertexFormat.POINTS);

        points = new ArrayList<VecF4>();
        colors = new ArrayList<VecF4>();

        this.setVertices(FloatBuffer.allocate(0));
        vertexColors = FloatBuffer.allocate(0);

        this.setNumVertices(0);
    }

    public void add(Point4 point, Color4 color) {
        points.add(point);
        colors.add(color);

        initialized = false;
    }

    public void addAll(List<Point4> newPoints, List<Color4> newColors) {
        points.addAll(newPoints);
        colors.addAll(newColors);

        initialized = false;
    }

    public synchronized void addAll(FloatBuffer inPoints, FloatBuffer inColors) {
        FloatBuffer newVertices = FloatBuffer.allocate(getVertices().capacity() + inPoints.capacity());
        FloatBuffer newVertexColors = FloatBuffer.allocate(vertexColors.capacity() + inColors.capacity());

        newVertices.put(getVertices());
        newVertices.put(inPoints);

        newVertexColors.put(vertexColors);
        newVertexColors.put(inColors);

        newVertices.rewind();
        newVertexColors.rewind();

        this.newVertices = newVertices;
        this.newColors = newVertexColors;

        initialized = false;
    }

    public void prepareBuffers() {
        newVertices = VectorFMath.vec4ListToBuffer(points);
        newColors = VectorFMath.vec4ListToBuffer(colors);
    }

    @Override
    public synchronized void init(GL3 gl) {
        if (!initialized) {
            delete(gl);

            if (newVertices == null) {
                this.setVertices(VectorFMath.vec4ListToBuffer(points));
            } else {
                this.setVertices(newVertices);
            }

            if (newColors == null) {
                this.vertexColors = VectorFMath.vec4ListToBuffer(colors);
            } else {
                this.vertexColors = newColors;
            }

            GLSLAttrib vAttrib = new GLSLAttrib(this.getVertices(), "MCvertex", GLSLAttrib.SIZE_FLOAT, 4);
            GLSLAttrib cAttrib = new GLSLAttrib(this.vertexColors, "MCvertexColor", GLSLAttrib.SIZE_FLOAT, 4);

            setVbo(new VBO(gl, vAttrib, cAttrib));

            this.setNumVertices(getVertices().capacity() / 4);

            initialized = true;
        }
    }

    @Override
    public void draw(GL3 gl, ShaderProgram program) throws UninitializedException {
        if (initialized) {
            try {
                program.use(gl);
            } catch (UninitializedException e) {
                LOGGER.error(e.getMessage());
            }

            getVbo().bind(gl);

            program.linkAttribs(gl, getVbo().getAttribs());

            gl.glDrawArrays(GL3.GL_POINTS, 0, getNumVertices());
        } else {
            throw new UninitializedException();
        }
    }

}
