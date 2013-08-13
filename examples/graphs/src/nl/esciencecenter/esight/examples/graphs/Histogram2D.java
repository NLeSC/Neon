package nl.esciencecenter.esight.examples.graphs;

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

public class Histogram2D {
    private final float[] data;
    private final LeftBottomQuad[] bars;
    private final Color4[] colors;
    private final MultiColorText[] barLabels;
    private final String[] barLabelTexts;

    private final float width, height;
    private final VecF3 leftBottomCoordinates;

    /** Ubuntu fontset is used for HUD elements */
    private static final int fontSet = FontFactory.UBUNTU;
    /** font is used for HUD elements @see fontSet */
    private final Font font;
    private final int FONTSIZE = 30;

    public Histogram2D(float width, float height, VecF3 leftBottomCoordinates, Color4[] barColors, String[] labels) {
        this.width = width;
        this.height = height;
        this.leftBottomCoordinates = leftBottomCoordinates;

        int numBars = barColors.length;

        float widthPerQuad = width / numBars;

        data = new float[numBars];
        bars = new LeftBottomQuad[numBars];
        for (int i = 0; i < numBars; i++) {
            data[i] = 0f;

            VecF3 newLeftBottom = leftBottomCoordinates.add(new VecF3(i * widthPerQuad, 0f, 0f));
            bars[i] = new LeftBottomQuad(height, widthPerQuad, newLeftBottom);
        }

        this.colors = barColors;

        this.font = FontFactory.get(fontSet).getDefault();
        this.barLabels = new MultiColorText[numBars];
        this.barLabelTexts = labels;
    }

    public void init(GL3 gl) {
        for (LeftBottomQuad q : bars) {
            q.init(gl);
        }

        int numBars = data.length;
        for (int i = 0; i < numBars; i++) {
            barLabels[i] = new MultiColorText(gl, font, barLabelTexts[i], Color4.WHITE, FONTSIZE);
            barLabels[i].setString(gl, barLabelTexts[i], Color4.WHITE, FONTSIZE);
        }
    }

    public void drawBars(GL3 gl, ShaderProgram program) throws UninitializedException {
        for (int i = 0; i < bars.length; i++) {
            LeftBottomQuad q = bars[i];

            // Set the color for the current Quad
            program.setUniformVector("Color", colors[i]);

            // Load all staged variables into the GPU, check for errors and
            // omissions.
            program.use(gl);
            q.draw(gl, program);
        }
    }

    public void drawLabels(GL3 gl, MatF4 mv, ShaderProgram program) throws UninitializedException {
        float widthPerQuad = width / bars.length;

        for (int i = 0; i < bars.length; i++) {
            MultiColorText label = barLabels[i];
            VecF3 newLeftBottom = leftBottomCoordinates.add(new VecF3(i * widthPerQuad, -0.5f, 0f));

            program.setUniformMatrix("MVMatrix", mv.mul(MatrixFMath.translate(newLeftBottom)));

            label.draw(gl, program);
        }
    }

    public void setValues(GL3 gl, float[] newData) throws IllegalArgumentException {
        int numBars = data.length;
        float widthPerQuad = width / numBars;

        if (newData.length != numBars) {
            throw new IllegalArgumentException("The size of this histogram was " + numBars
                    + " while you gave me data for a size " + newData.length);
        }

        for (int i = 0; i < bars.length; i++) {
            bars[i].delete(gl);

            VecF3 newLeftBottom = leftBottomCoordinates.add(new VecF3(i * widthPerQuad, 0f, 0f));
            bars[i] = new LeftBottomQuad(newData[i] * height, widthPerQuad, newLeftBottom);
            bars[i].init(gl);
        }
    }

}
