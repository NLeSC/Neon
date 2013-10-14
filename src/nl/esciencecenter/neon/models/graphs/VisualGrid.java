package nl.esciencecenter.neon.models.graphs;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL3;

import nl.esciencecenter.neon.datastructures.GLSLAttribute;
import nl.esciencecenter.neon.datastructures.VertexBufferObject;
import nl.esciencecenter.neon.exceptions.UninitializedException;
import nl.esciencecenter.neon.math.Float3Vector;
import nl.esciencecenter.neon.math.Float4Vector;
import nl.esciencecenter.neon.math.FloatVectorMath;
import nl.esciencecenter.neon.models.Model;
import nl.esciencecenter.neon.shaders.ShaderProgram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisualGrid extends Model {
    private final static Logger LOGGER = LoggerFactory.getLogger(VisualGrid.class);

    private final int width, height;

    private final List<Float4Vector> points;
    private final List<Float3Vector> normals;
    private final List<Float4Vector> colors;

    private FloatBuffer vertexColors;

    private boolean initialized = false;

    public VisualGrid(int width, int height) {
        super(VertexFormat.TRIANGLES);

        this.width = width;
        this.height = height;

        points = new ArrayList<Float4Vector>();
        normals = new ArrayList<Float3Vector>();
        colors = new ArrayList<Float4Vector>();

        this.setVertices(FloatBuffer.allocate(0));
        vertexColors = FloatBuffer.allocate(0);

        this.setNumVertices(0);
    }

    public void setGrid(Float4Vector[][] gridPoints, Float4Vector[][] gridColors) {
        points.clear();
        normals.clear();
        colors.clear();

        for (int latIndex = 0; latIndex < height - 1; latIndex++) {
            Float4Vector[] thisLinePoints = gridPoints[latIndex];
            Float4Vector[] nextLinePoints = gridPoints[latIndex + 1];

            Float4Vector[] thisLineColors = gridColors[latIndex];
            Float4Vector[] nextLineColors = gridColors[latIndex + 1];

            for (int lonIndex = 0; lonIndex < width - 1; lonIndex++) {
                Float4Vector point00 = thisLinePoints[lonIndex];
                Float4Vector point01 = thisLinePoints[lonIndex + 1];
                Float4Vector point10 = nextLinePoints[lonIndex];
                Float4Vector point11 = nextLinePoints[lonIndex + 1];

                Float4Vector color00 = thisLineColors[lonIndex];
                Float4Vector color01 = thisLineColors[lonIndex + 1];
                Float4Vector color10 = nextLineColors[lonIndex];
                Float4Vector color11 = nextLineColors[lonIndex + 1];

                // First triangle
                points.add(point00);
                points.add(point01);
                points.add(point10);

                Float3Vector normal0 = getNormal(point00, point01, point10);
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

                Float3Vector normal1 = getNormal(point10, point01, point11);
                normals.add(normal1);
                normals.add(normal1);
                normals.add(normal1);

                colors.add(color10);
                colors.add(color01);
                colors.add(color11);
            }
        }
    }

    private Float3Vector getNormal(Float4Vector point0, Float4Vector point1, Float4Vector point2) {
        Float4Vector side0 = point1.sub(point0);
        Float4Vector side1 = point2.sub(point0);

        return FloatVectorMath.cross(side0, side1).stripAlpha();
    }

    @Override
    public void init(GL3 gl) {
        delete(gl);

        this.setVertices(FloatVectorMath.vec4ListToBuffer(points));
        this.setNormals(FloatVectorMath.vec3ListToBuffer(normals));
        this.vertexColors = FloatVectorMath.vec4ListToBuffer(colors);

        GLSLAttribute vAttrib = new GLSLAttribute(this.getVertices(), "MCvertex", GLSLAttribute.SIZE_FLOAT, 4);
        GLSLAttribute nAttrib = new GLSLAttribute(this.getNormals(), "MCnormal", GLSLAttribute.SIZE_FLOAT, 3);
        GLSLAttribute cAttrib = new GLSLAttribute(this.vertexColors, "MCvertexColor", GLSLAttribute.SIZE_FLOAT, 4);

        setVbo(new VertexBufferObject(gl, vAttrib, nAttrib, cAttrib));

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
