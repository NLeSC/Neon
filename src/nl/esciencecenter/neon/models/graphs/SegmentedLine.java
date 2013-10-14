package nl.esciencecenter.neon.models.graphs;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL3;

import nl.esciencecenter.neon.datastructures.GLSLAttrib;
import nl.esciencecenter.neon.datastructures.VBO;
import nl.esciencecenter.neon.exceptions.UninitializedException;
import nl.esciencecenter.neon.math.Color4;
import nl.esciencecenter.neon.math.VecF4;
import nl.esciencecenter.neon.models.Model;
import nl.esciencecenter.neon.shaders.ShaderProgram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SegmentedLine extends Model {
    private final static Logger LOGGER = LoggerFactory.getLogger(SegmentedLine.class);

    private final Color4 color;
    private final List<VecF4> points;

    private final float widthPerSegment;

    private class DataPoint {
        private final float horizontal, vertical;

        public DataPoint(float horizontal, float vertical) {
            this.horizontal = horizontal;
            this.vertical = vertical;
        }

        public float getHorizontal() {
            return horizontal;
        }

        public float getVertical() {
            return vertical;
        }
    }

    private final List<DataPoint> dataPoints;

    private float minHorizontal, maxHorizontal, minVertical, maxVertical;

    public SegmentedLine(int numSegments, float widthPerSegment, Color4 color) {
        super(VertexFormat.LINES);

        this.widthPerSegment = widthPerSegment;

        this.color = color;
        points = new ArrayList<VecF4>();
        for (int i = 0; i < numSegments + 1; i++) {
            points.add(new VecF4(i * widthPerSegment, 0f, 0f, 1f));
        }
        this.dataPoints = new ArrayList<DataPoint>();

        this.minHorizontal = Float.MAX_VALUE;
        this.minVertical = Float.MAX_VALUE;
        this.maxHorizontal = Float.MIN_VALUE;
        this.maxVertical = Float.MIN_VALUE;
    }

    public boolean addData(float horizontal, float vertical) {
        boolean dimensionsChanged = false;

        if (horizontal < minHorizontal) {
            minHorizontal = horizontal;
            dimensionsChanged = true;
        }
        if (vertical < minVertical) {
            minVertical = vertical;
            dimensionsChanged = true;
        }
        if (horizontal > maxHorizontal) {
            maxHorizontal = horizontal;
            dimensionsChanged = true;
        }
        if (vertical > maxVertical) {
            maxVertical = vertical;
            dimensionsChanged = true;
        }

        dataPoints.add(new DataPoint(horizontal, vertical));

        recalculatePoints();

        return dimensionsChanged;
    }

    public void applyNewDimensions(float minHorizontal, float maxHorizontal, float minVertical, float maxVertical) {
        this.minHorizontal = minHorizontal;
        this.maxHorizontal = maxHorizontal;
        this.minVertical = minVertical;
        this.maxVertical = maxVertical;

        recalculatePoints();
    }

    private void recalculatePoints() {
        int numSegments = points.size();

        float diffHorizontal = maxHorizontal - minHorizontal;

        float segmentWidth = diffHorizontal / numSegments;

        points.clear();

        for (int i = 0; i < numSegments; i++) {
            float lowerSegmentBoundary = i * segmentWidth;
            float upperSegmentBoundary = (i + 1) * segmentWidth;

            float qualifyingDataTotal = 0f;
            for (DataPoint d : dataPoints) {
                if (d.getHorizontal() > lowerSegmentBoundary && d.getHorizontal() < upperSegmentBoundary) {
                    qualifyingDataTotal += d.getVertical();
                }
            }

            float segmentHeight = qualifyingDataTotal / dataPoints.size();

            VecF4 segmentPoint = new VecF4(i * widthPerSegment, segmentHeight, 0f, 1f);

            points.add(segmentPoint);
        }
    }

    public float getSegmentValue(int segmentIndex) {
        int numSegments = points.size();
        float diffHorizontal = maxHorizontal - minHorizontal;
        float segmentWidth = diffHorizontal / numSegments;

        return segmentIndex * segmentWidth;
    }

    public FloatBuffer pointsAsBuffer() {
        FloatBuffer result = FloatBuffer.allocate(points.size() * 2 * 4);

        for (int i = 0; i < points.size() - 1; i++) {
            VecF4 thisPoint = points.get(i);
            VecF4 nextPoint = points.get(i + 1);

            result.put(thisPoint.asBuffer());
            result.put(nextPoint.asBuffer());
        }
        result.rewind();

        return result;
    }

    public FloatBuffer colorAsBuffer() {
        return color.asBuffer();
    }

    public float getMinHorizontal() {
        return minHorizontal;
    }

    public float getMaxHorizontal() {
        return maxHorizontal;
    }

    public float getMinVertical() {
        return minVertical;
    }

    public float getMaxVertical() {
        return maxVertical;
    }

    @Override
    public void init(GL3 gl) {
        delete(gl);

        setNumVertices(points.size() * 2);
        GLSLAttrib vAttrib = new GLSLAttrib(pointsAsBuffer(), "MCvertex", GLSLAttrib.SIZE_FLOAT, 4);

        setVbo(new VBO(gl, vAttrib));
    }

    @Override
    public void draw(GL3 gl, ShaderProgram program) throws UninitializedException {
        program.setUniformVector("Color", color);

        getVbo().bind(gl);

        program.linkAttribs(gl, getVbo().getAttribs());

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        try {
            program.use(gl);
        } catch (UninitializedException e) {
            LOGGER.error(e.getMessage());
        }

        gl.glDrawArrays(GL3.GL_LINES, 0, getNumVertices());
    }
}
