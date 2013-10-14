package nl.esciencecenter.neon.models.graphs;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL3;

import nl.esciencecenter.neon.datastructures.GLSLAttrib;
import nl.esciencecenter.neon.datastructures.VBO;
import nl.esciencecenter.neon.exceptions.UninitializedException;
import nl.esciencecenter.neon.math.Color4;
import nl.esciencecenter.neon.math.VecF3;
import nl.esciencecenter.neon.math.VecF4;
import nl.esciencecenter.neon.math.VectorFMath;
import nl.esciencecenter.neon.models.Model;
import nl.esciencecenter.neon.shaders.ShaderProgram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BezierLine extends Model {
    private final static Logger LOGGER = LoggerFactory.getLogger(BezierLine.class);

    private static final int NUMBER_OF_BEZIER_STEPS = 10;
    private final VecF3 bezierControlStart;
    private final VecF3 bezierControlEnd;

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

    public BezierLine(int numSegments, float widthPerSegment, Color4 color) {
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

        bezierControlStart = new VecF3(widthPerSegment, 0f, 0f);
        bezierControlEnd = new VecF3(widthPerSegment, 0f, 0f);
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

    private synchronized void recalculatePoints() {
        int numSegments = points.size();

        float diffHorizontal = maxHorizontal - minHorizontal;

        float segmentWidth = diffHorizontal / numSegments;

        points.clear();

        for (int i = 0; i < numSegments; i++) {
            float lowerSegmentBoundary = i * segmentWidth + minHorizontal;
            float upperSegmentBoundary = (i + 1) * segmentWidth + minHorizontal;

            float qualifyingDataTotal = 0f;
            for (DataPoint d : dataPoints) {
                if (d.getHorizontal() > lowerSegmentBoundary && d.getHorizontal() < upperSegmentBoundary) {
                    qualifyingDataTotal++;
                }
            }

            float segmentHeight = qualifyingDataTotal / dataPoints.size();
            if (Float.isNaN(segmentHeight)) {
                segmentHeight = 0f;
            }

            VecF4 segmentPoint = new VecF4(i * widthPerSegment, segmentHeight, 0f, 1f);

            points.add(segmentPoint);
        }
    }

    public float getSegmentValue(int segmentIndex) {
        int numSegments = points.size();
        float diffHorizontal = maxHorizontal - minHorizontal;
        float segmentWidth = diffHorizontal / numSegments;

        return segmentIndex * segmentWidth + minHorizontal;
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
    public synchronized void init(GL3 gl) {
        delete(gl);

        setNumVertices(points.size() * (2 * NUMBER_OF_BEZIER_STEPS));

        FloatBuffer bezierBuffer = FloatBuffer.allocate(getNumVertices() * 4);

        // for (int i = 1; i < points.size() - 1; i++) {
        // VecF4 prevPoint = points.get(i - 1);
        // VecF4 thisPoint = points.get(i);
        // VecF4 nextPoint = points.get(i + 1);
        // }

        VecF4 lastBezierPointOfPreviousIteration = null;
        for (int i = 0; i < points.size() - 1; i++) {
            VecF4 startPoint = points.get(i);
            VecF4 endPoint = points.get(i + 1);

            VecF4[] bezierResult = VectorFMath.bezierCurve(NUMBER_OF_BEZIER_STEPS, startPoint, bezierControlStart,
                    bezierControlEnd, endPoint);

            for (int j = 0; j < bezierResult.length - 1; j++) {
                if (j == 0 && lastBezierPointOfPreviousIteration != null) {
                    bezierBuffer.put(lastBezierPointOfPreviousIteration.asBuffer());
                    bezierBuffer.put(bezierResult[j].asArray());
                }

                bezierBuffer.put(bezierResult[j].asArray());
                bezierBuffer.put(bezierResult[j + 1].asArray());
            }

            lastBezierPointOfPreviousIteration = bezierResult[bezierResult.length - 1];
        }

        bezierBuffer.rewind();

        GLSLAttrib vAttrib = new GLSLAttrib(bezierBuffer, "MCvertex", GLSLAttrib.SIZE_FLOAT, 4);

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
