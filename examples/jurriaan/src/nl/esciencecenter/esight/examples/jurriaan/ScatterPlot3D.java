package nl.esciencecenter.esight.examples.jurriaan;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.datastructures.GLSLAttrib;
import nl.esciencecenter.esight.datastructures.VBO;
import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.math.Color4;
import nl.esciencecenter.esight.math.Point4;
import nl.esciencecenter.esight.math.VecF4;
import nl.esciencecenter.esight.math.VectorFMath;
import nl.esciencecenter.esight.models.Model;
import nl.esciencecenter.esight.shaders.ShaderProgram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScatterPlot3D extends Model {
    private final static Logger LOGGER = LoggerFactory.getLogger(ScatterPlot3D.class);

    private final List<VecF4> points;
    private final List<VecF4> colors;

    private final List<VecF4> drawableVertices;

    private FloatBuffer vertexColors;

    private boolean initialized = false;

    private float minX, maxX, minY, maxY, minZ, maxZ;

    public ScatterPlot3D() {
        super(VertexFormat.POINTS);

        points = new ArrayList<VecF4>();
        colors = new ArrayList<VecF4>();
        drawableVertices = new ArrayList<VecF4>();

        this.setVertices(FloatBuffer.allocate(0));
        vertexColors = FloatBuffer.allocate(0);

        this.setNumVertices(0);

        this.minX = Float.MAX_VALUE;
        this.minY = Float.MAX_VALUE;
        this.minZ = Float.MAX_VALUE;
        this.maxX = Float.MIN_VALUE;
        this.maxY = Float.MIN_VALUE;
        this.maxZ = Float.MIN_VALUE;
    }

    public void add(Point4 point, Color4 color) {
        if (point.getX() < minX) {
            minX = point.getX();
        }
        if (point.getX() > maxX) {
            maxX = point.getX();
        }
        if (point.getY() < minX) {
            minY = point.getY();
        }
        if (point.getY() > maxX) {
            maxY = point.getY();
        }
        if (point.getZ() < minZ) {
            minZ = point.getZ();
        }
        if (point.getZ() > maxZ) {
            maxZ = point.getZ();
        }

        points.add(point);
        colors.add(color);

        recalculatePoints();
    }

    public void simpleAdd(Point4 point, Color4 color) {
        points.add(point);
        colors.add(color);
    }

    public void finalizeSimpleAdd() {
        for (VecF4 point : points) {
            if (point.getX() < minX) {
                minX = point.getX();
            }
            if (point.getX() > maxX) {
                maxX = point.getX();
            }
            if (point.getY() < minY) {
                minY = point.getY();
            }
            if (point.getY() > maxY) {
                maxY = point.getY();
            }
            if (point.getZ() < minZ) {
                minZ = point.getZ();
            }
            if (point.getZ() > maxZ) {
                maxZ = point.getZ();
            }
        }

        recalculatePoints();

    }

    private synchronized void recalculatePoints() {
        float diffX = maxX - minX;
        float diffY = maxY - minY;
        float diffZ = maxZ - minZ;

        drawableVertices.clear();

        int numPoints = points.size();

        for (int i = 0; i < numPoints; i++) {
            VecF4 current = points.get(i);
            float newX = ((current.getX() - minX) / diffX);
            float newY = ((current.getY() - minY) / diffY);
            float newZ = ((current.getZ() - minZ) / diffZ);

            drawableVertices.add(new VecF4(newX, newY, newZ, 1f));
        }
    }

    @Override
    public void init(GL3 gl) {
        delete(gl);

        this.setVertices(VectorFMath.vec4ListToBuffer(drawableVertices));
        this.vertexColors = VectorFMath.vec4ListToBuffer(colors);

        GLSLAttrib vAttrib = new GLSLAttrib(this.getVertices(), "MCvertex", GLSLAttrib.SIZE_FLOAT, 4);
        GLSLAttrib cAttrib = new GLSLAttrib(this.vertexColors, "MCvertexColor", GLSLAttrib.SIZE_FLOAT, 4);

        setVbo(new VBO(gl, vAttrib, cAttrib));

        this.setNumVertices(points.size());

        initialized = true;
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
