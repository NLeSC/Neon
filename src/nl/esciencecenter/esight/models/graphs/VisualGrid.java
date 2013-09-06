package nl.esciencecenter.esight.models.graphs;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.datastructures.GLSLAttrib;
import nl.esciencecenter.esight.datastructures.VBO;
import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VecF4;
import nl.esciencecenter.esight.math.VectorFMath;
import nl.esciencecenter.esight.models.Model;
import nl.esciencecenter.esight.shaders.ShaderProgram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisualGrid extends Model {
    private final static Logger LOGGER = LoggerFactory.getLogger(VisualGrid.class);

    private final int width, height;

    private final List<VecF4> points;
    private final List<VecF3> normals;
    private final List<VecF4> colors;

    private FloatBuffer vertexColors;

    private boolean initialized = false;

    public VisualGrid(int width, int height) {
        super(VertexFormat.TRIANGLES);

        this.width = width;
        this.height = height;

        points = new ArrayList<VecF4>();
        normals = new ArrayList<VecF3>();
        colors = new ArrayList<VecF4>();

        this.setVertices(FloatBuffer.allocate(0));
        vertexColors = FloatBuffer.allocate(0);

        this.setNumVertices(0);
    }

    public void setGrid(VecF4[][] gridPoints, VecF4[][] gridColors) {
        points.clear();
        normals.clear();
        colors.clear();

        for (int latIndex = 0; latIndex < height - 1; latIndex++) {
            VecF4[] thisLinePoints = gridPoints[latIndex];
            VecF4[] nextLinePoints = gridPoints[latIndex + 1];

            VecF4[] thisLineColors = gridColors[latIndex];
            VecF4[] nextLineColors = gridColors[latIndex + 1];

            for (int lonIndex = 0; lonIndex < width - 1; lonIndex++) {
                VecF4 point00 = thisLinePoints[lonIndex];
                VecF4 point01 = thisLinePoints[lonIndex + 1];
                VecF4 point10 = nextLinePoints[lonIndex];
                VecF4 point11 = nextLinePoints[lonIndex + 1];

                VecF4 color00 = thisLineColors[lonIndex];
                VecF4 color01 = thisLineColors[lonIndex + 1];
                VecF4 color10 = nextLineColors[lonIndex];
                VecF4 color11 = nextLineColors[lonIndex + 1];

                // First triangle
                points.add(point00);
                points.add(point01);
                points.add(point10);

                VecF3 normal0 = getNormal(point00, point01, point10);
                normals.add(normal0);
                normals.add(normal0);
                normals.add(normal0);

                colors.add(color00);
                colors.add(color01);
                colors.add(color10);

                // Second triangle
                points.add(point10);
                points.add(point01);
                points.add(point11);

                VecF3 normal1 = getNormal(point10, point01, point11);
                normals.add(normal1);
                normals.add(normal1);
                normals.add(normal1);

                colors.add(color10);
                colors.add(color01);
                colors.add(color11);
            }
        }
    }

    private VecF3 getNormal(VecF4 point0, VecF4 point1, VecF4 point2) {
        VecF4 side0 = point1.sub(point0);
        VecF4 side1 = point2.sub(point0);

        return VectorFMath.cross(side0, side1).stripAlpha();
    }

    @Override
    public void init(GL3 gl) {
        delete(gl);

        this.setVertices(VectorFMath.vec4ListToBuffer(points));
        this.setNormals(VectorFMath.vec3ListToBuffer(normals));
        this.vertexColors = VectorFMath.vec4ListToBuffer(colors);

        GLSLAttrib vAttrib = new GLSLAttrib(this.getVertices(), "MCvertex", GLSLAttrib.SIZE_FLOAT, 4);
        GLSLAttrib nAttrib = new GLSLAttrib(this.getNormals(), "MCnormal", GLSLAttrib.SIZE_FLOAT, 3);
        GLSLAttrib cAttrib = new GLSLAttrib(this.vertexColors, "MCvertexColor", GLSLAttrib.SIZE_FLOAT, 4);

        setVbo(new VBO(gl, vAttrib, nAttrib, cAttrib));

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

            gl.glDrawArrays(GL3.GL_TRIANGLES, 0, getNumVertices());
        } else {
            throw new UninitializedException();
        }
    }

}
