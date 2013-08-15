package nl.esciencecenter.esight.examples.graphs;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.esciencecenter.esight.math.Color4;
import nl.esciencecenter.esight.math.VecF4;
import nl.esciencecenter.esight.math.VectorFMath;
import nl.esciencecenter.esight.models.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LineGraph2D extends Model {
    private final static Logger LOGGER = LoggerFactory.getLogger(LineGraph2D.class);

    private FloatBuffer vertexColors;

    private final Color4[] colors;
    private final Map<Integer, SegmentedLine> segmentedLines;

    private float minHorizontal, maxHorizontal, minVertical, maxVertical;

    private final float width;

    private final float height;
    private final int horizontalSegments;

    private class SegmentedLine {
        private final Color4 color;
        private final List<VecF4> points;

        public SegmentedLine(int numSegments, float widthPerSegment, Color4 color) {
            this.color = color;
            points = new ArrayList<VecF4>();
            for (int i = 0; i < numSegments + 1; i++) {
                points.add(new VecF4(i * widthPerSegment, 0f, 0f, 1f));
            }
        }

        public void changeHeightOfPoint(int pointIndex, float newHeight) {
            points.get(pointIndex).setY(newHeight);
        }

        public FloatBuffer pointsAsBuffer() {
            return VectorFMath.vec4ListToBuffer(points);
        }

        public FloatBuffer colorAsBuffer() {
            return color.asBuffer();
        }
    }

    public LineGraph2D(float width, float height, int horizontalSegments, Color4[] colors) {
        super(VertexFormat.LINES);

        this.width = width;
        this.height = height;

        this.colors = colors;
        this.horizontalSegments = horizontalSegments;

        this.minHorizontal = Float.MAX_VALUE;
        this.minVertical = Float.MAX_VALUE;
        this.maxHorizontal = Float.MIN_VALUE;
        this.maxVertical = Float.MIN_VALUE;

        this.segmentedLines = new HashMap<Integer, SegmentedLine>();
        for (int i = 0; i < colors.length; i++) {
            segmentedLines.put(i, new SegmentedLine(horizontalSegments, width / horizontalSegments, colors[i]));
        }

    }

    public void addData(int colorIndex, float horizontal, float vertical) {
        if (horizontal < minHorizontal) {
            minHorizontal = horizontal;
        }
        if (vertical < minVertical) {
            minVertical = vertical;
        }
        if (horizontal > maxHorizontal) {
            maxHorizontal = horizontal;
        }
        if (vertical > maxVertical) {
            maxVertical = vertical;
        }

        SegmentedLine sl = segmentedLines.get(colorIndex);

        // sl.changeHeightOfPoint(pointIndex, newHeight);

    }

    // @Override
    // public void init(GL3 gl) {
    // // delete(gl);
    //
    // GLSLAttrib vAttrib = new GLSLAttrib(this.getVertices(), "MCvertex",
    // GLSLAttrib.SIZE_FLOAT, 4);
    // GLSLAttrib cAttrib = new GLSLAttrib(this.vertexColors, "MCvertexColor",
    // GLSLAttrib.SIZE_FLOAT, 4);
    //
    // setVbo(new VBO(gl, vAttrib, cAttrib));
    //
    // this.setNumVertices(lines.size() * 2 * 4);
    // }
    //
    // @Override
    // public void draw(GL3 gl, ShaderProgram program) throws
    // UninitializedException {
    // try {
    // program.use(gl);
    // } catch (UninitializedException e) {
    // LOGGER.error(e.getMessage());
    // }
    //
    // getVbo().bind(gl);
    //
    // program.linkAttribs(gl, getVbo().getAttribs());
    //
    // gl.glDrawArrays(GL3.GL_LINES, 0, getNumVertices());
    // }

}
