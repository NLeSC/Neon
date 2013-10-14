package nl.esciencecenter.neon.models.graphs;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL3;

import nl.esciencecenter.neon.datastructures.GLSLAttribute;
import nl.esciencecenter.neon.datastructures.VertexBufferObject;
import nl.esciencecenter.neon.exceptions.UninitializedException;
import nl.esciencecenter.neon.math.Color4;
import nl.esciencecenter.neon.math.Float3Vector;
import nl.esciencecenter.neon.math.Float4Vector;
import nl.esciencecenter.neon.math.FloatVectorMath;
import nl.esciencecenter.neon.models.Model;
import nl.esciencecenter.neon.shaders.ShaderProgram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BezierLine extends Model {
    private final static Logger LOGGER = LoggerFactory.getLogger(BezierLine.class);

    private static final int NUMBER_OF_BEZIER_STEPS = 10;
    private final Float3Vector bezierControlStart;
    private final Float3Vector bezierControlEnd;

    private final Color4 color;
    private final List<Float4Vector> points;

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
        points = new ArrayList<Float4Vector>();
        for (int i = 0; i < numSegments + 1; i++) {
            points.add(new Float4Vector(i * widthPerSegment, 0f, 0f, 1f));
        }
        this.dataPoints = new ArrayList<DataPoint>();

        this.minHorizontal = Float.MAX_VALUE;
        this.minVertical = Float.MAX_VALUE;
        this.maxHorizontal = Float.MIN_VALUE;
        this.maxVertical = Float.MIN_VALUE;

        bezierControlStart = new Float3Vector(widthPerSegment, 0f, 0f);
        bezierControlEnd = new Float3Vector(widthPerSegment, 0f, 0f);
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

            Float4Vector segmentPoint = new Float4Vector(i * widthPerSegment, segmentHeight, 0f, 1f);

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
            Float4Vector thisPoint = points.get(i);
            Float4Vector nextPoint = points.get(i + 1);

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
        // Float4Vector prevPoint = points.get(i - 1);
        // Float4Vector thisPoint = points.get(i);
        // Float4Vector nextPoint = points.get(i + 1);
        // }

        Float4Vector lastBezierPointOfPreviousIteration = null;
        for (int i = 0; i < points.size() - 1; i++) {
            Float4Vector startPoint = points.get(i);
            Float4Vector endPoint = points.get(i + 1);

            Float4Vector[] bezierResult = FloatVectorMath.bezierCurve(NUMBER_OF_BEZIER_STEPS, startPoint, bezierControlStart,
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

        GLSLAttribute vAttrib = new GLSLAttribute(bezierBuffer, "MCvertex", GLSLAttribute.SIZE_FLOAT, 4);

        setVbo(new VertexBufferObject(gl, vAttrib));
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
