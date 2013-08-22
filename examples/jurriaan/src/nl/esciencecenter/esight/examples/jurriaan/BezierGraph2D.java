package nl.esciencecenter.esight.examples.jurriaan;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.math.Color4;
import nl.esciencecenter.esight.math.MatF4;
import nl.esciencecenter.esight.math.MatrixFMath;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.shaders.ShaderProgram;
import nl.esciencecenter.esight.text.MultiColorText;
import nl.esciencecenter.esight.text.jogampexperimental.Font;
import nl.esciencecenter.esight.text.jogampexperimental.FontFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BezierGraph2D {
    private final static Logger LOGGER = LoggerFactory.getLogger(BezierGraph2D.class);

    private static final int NR_OF_HORIZONTAL_LABELS = 10;

    /** Ubuntu fontset is used for HUD elements */
    private static final int fontSet = FontFactory.UBUNTU;
    /** font is used for HUD elements @see fontSet */
    private final Font font;
    private final int FONTSIZE = 20;

    private FloatBuffer vertexColors;

    private final Color4[] colors;
    private final Map<Integer, BezierLine> segmentedLines;

    private final float width;

    private final float height;
    private final int horizontalSegments;

    private final String[] seperateColorNames;
    private final MultiColorText[] horizontalLabels, seperateColorLabels;

    private final String horizontalAxisString, verticalAxisString;
    private MultiColorText horizontalAxisText, verticalAxisText;

    public BezierGraph2D(float width, float height, int horizontalSegments, Color4[] colors,
            String[] seperateColorNames, String horizontalAxisString, String verticalAxisString) {
        this.width = width;
        this.height = height;
        this.horizontalAxisString = horizontalAxisString;
        this.verticalAxisString = verticalAxisString;
        this.seperateColorNames = seperateColorNames;

        this.colors = colors;
        this.horizontalSegments = horizontalSegments;

        this.segmentedLines = new HashMap<Integer, BezierLine>();
        for (int i = 0; i < colors.length; i++) {
            segmentedLines.put(i, new BezierLine(horizontalSegments, width / horizontalSegments, colors[i]));
        }

        this.horizontalLabels = new MultiColorText[NR_OF_HORIZONTAL_LABELS];
        this.seperateColorLabels = new MultiColorText[seperateColorNames.length];

        this.font = FontFactory.get(fontSet).getDefault();
    }

    public void addData(int colorIndex, float horizontal, float vertical) {
        BezierLine sl = segmentedLines.get(colorIndex);
        boolean dimensionsChanged = sl.addData(horizontal, vertical);

        if (dimensionsChanged) {
            float minHorizontal = sl.getMinHorizontal();
            float maxHorizontal = sl.getMaxHorizontal();
            float minVertical = sl.getMinVertical();
            float maxVertical = sl.getMaxVertical();

            // Change the dimensions of all other colored graphs to the same as
            // the current one.
            for (BezierLine sl2 : segmentedLines.values()) {
                if (!sl2.equals(sl)) {
                    sl2.applyNewDimensions(minHorizontal, maxHorizontal, minVertical, maxVertical);
                }
            }
        }
    }

    public void simpleAdd(int colorIndex, float horizontal, float vertical) {
        BezierLine sl = segmentedLines.get(colorIndex);
        sl.simpleAdd(horizontal, vertical);
    }

    public void finalizeSimpleAdd() {
        for (BezierLine bl : segmentedLines.values()) {
            bl.finalizeSimpleAdd();
        }
    }

    public void init(GL3 gl) {
        for (BezierLine sl : segmentedLines.values()) {
            sl.init(gl);
        }

        if (horizontalAxisText == null) {
            horizontalAxisText = new MultiColorText(gl, font, horizontalAxisString, Color4.WHITE, FONTSIZE);
            horizontalAxisText.setString(gl, horizontalAxisString, Color4.WHITE, FONTSIZE);
        }
        if (verticalAxisText == null) {
            verticalAxisText = new MultiColorText(gl, font, verticalAxisString, Color4.WHITE, FONTSIZE);
            verticalAxisText.setString(gl, verticalAxisString, Color4.WHITE, FONTSIZE);
        }

        if (seperateColorLabels[0] == null) {
            for (int i = 0; i < seperateColorLabels.length; i++) {
                seperateColorLabels[i] = new MultiColorText(gl, font, seperateColorNames[i], colors[i], FONTSIZE);
                seperateColorLabels[i].setString(gl, seperateColorNames[i], colors[i], FONTSIZE);
            }
        }

        for (int i = 0; i < segmentedLines.size(); i++) {
            BezierLine sl = segmentedLines.get(i);

            for (int j = 0; j < NR_OF_HORIZONTAL_LABELS; j++) {
                int segmentIndex = (horizontalSegments / NR_OF_HORIZONTAL_LABELS) * j;
                if (horizontalLabels[j] != null) {
                    horizontalLabels[j].delete(gl);
                }

                float rawValue = sl.getSegmentValue(segmentIndex);
                String text = "";
                if (sl.getMaxHorizontal() > 10f || sl.getMinHorizontal() < -10f) {
                    text = String.format("%.0f", rawValue);
                } else {
                    text = String.format("%1.1f", rawValue);
                }

                horizontalLabels[j] = new MultiColorText(gl, font, text, Color4.WHITE, FONTSIZE);
                horizontalLabels[j].setString(gl, text, Color4.WHITE, FONTSIZE);
            }
        }
    }

    public void draw(GL3 gl, MatF4 mv, ModelViewStack mvStack, ShaderProgram program) throws UninitializedException {
        int i = 0;
        for (BezierLine bl : segmentedLines.values()) {
            ModelViewStack linesStack = new ModelViewStack(mvStack);
            linesStack.putBottom(MatrixFMath.translate(0f, 0f, i * (width / colors.length)));
            program.setUniformMatrix("MVMatrix", linesStack.calc(mv));

            gl.glLineWidth(3f);

            bl.draw(gl, program);
            i++;
        }
    }

    public void drawLabels(GL3 gl, MatF4 mv, ModelViewStack mvStack, ShaderProgram program)
            throws UninitializedException {
        float widthPerSegment = width / horizontalSegments;

        float scale = .0025f;

        MatF4 scaleMatrix = MatrixFMath.scale(scale);

        ModelViewStack horizontalAxisTextStack = new ModelViewStack(mvStack);
        MatF4 horizontalAxisTranslation = MatrixFMath.translate(new VecF3(0f, -0.15f, width));
        horizontalAxisTextStack.putTop(horizontalAxisTranslation);
        horizontalAxisTextStack.putTop(scaleMatrix);
        program.setUniformMatrix("MVMatrix", horizontalAxisTextStack.calc(mv));
        horizontalAxisText.draw(gl, program);

        for (int i = 0; i < seperateColorLabels.length; i++) {
            MultiColorText label = seperateColorLabels[i];

            ModelViewStack colorLabelStack = new ModelViewStack(mvStack);
            MatF4 colorLabelTranslation = MatrixFMath.translate(new VecF3(0f, -0.2f, i * (width / colors.length)));
            colorLabelStack.putTop(colorLabelTranslation);
            colorLabelStack.putTop(scaleMatrix);
            program.setUniformMatrix("MVMatrix", colorLabelStack.calc(mv));
            label.draw(gl, program);
        }

        ModelViewStack verticalAxisTextStack = new ModelViewStack(mvStack);
        MatF4 verticalAxisRotation = MatrixFMath.rotationZ(90f);
        verticalAxisTextStack.putTop(verticalAxisRotation);

        MatF4 verticalAxisTranslation = MatrixFMath.translate(new VecF3(.15f, 0.05f, width));
        verticalAxisTextStack.putTop(verticalAxisTranslation);

        verticalAxisTextStack.putTop(scaleMatrix);
        program.setUniformMatrix("MVMatrix", verticalAxisTextStack.calc(mv));
        verticalAxisText.draw(gl, program);

        MatF4 horizontalLabelRotationMatrix = MatrixFMath.rotationZ(-90f);

        for (int i = 0; i < NR_OF_HORIZONTAL_LABELS; i++) {
            MultiColorText label = horizontalLabels[i];

            MatF4 horizontalLabelTranslation = MatrixFMath.translate(0.2f,
                    (widthPerSegment * i * horizontalSegments / NR_OF_HORIZONTAL_LABELS) + (.2f * widthPerSegment),
                    width);

            ModelViewStack horizontalLabelStack = new ModelViewStack(mvStack);
            horizontalLabelStack.putTop(horizontalLabelRotationMatrix);
            horizontalLabelStack.putTop(horizontalLabelTranslation);
            horizontalLabelStack.putTop(scaleMatrix);
            program.setUniformMatrix("MVMatrix", horizontalLabelStack.calc(mv));
            label.draw(gl, program);
        }
    }
}
