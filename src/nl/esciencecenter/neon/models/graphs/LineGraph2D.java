package nl.esciencecenter.neon.models.graphs;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL3;

import nl.esciencecenter.neon.exceptions.UninitializedException;
import nl.esciencecenter.neon.math.Color4;
import nl.esciencecenter.neon.math.Float3Vector;
import nl.esciencecenter.neon.math.Float4Matrix;
import nl.esciencecenter.neon.math.FloatMatrixMath;
import nl.esciencecenter.neon.shaders.ShaderProgram;
import nl.esciencecenter.neon.text.MultiColorText;
import nl.esciencecenter.neon.text.jogampexperimental.Font;
import nl.esciencecenter.neon.text.jogampexperimental.FontFactory;
import nl.esciencecenter.neon.util.ModelViewStack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* Copyright 2013 Netherlands eScience Center
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Convenience class to create a 2D Line graph model.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class LineGraph2D {
    private final static Logger LOGGER = LoggerFactory.getLogger(LineGraph2D.class);

    private static final int NR_OF_HORIZONTAL_LABELS = 10;

    /** Ubuntu fontset is used for HUD elements */
    private static final int fontSet = FontFactory.UBUNTU;
    /** font is used for HUD elements @see fontSet */
    private final Font font;
    private final int FONTSIZE = 20;

    private final Color4[] colors;
    private final Map<Integer, SegmentedLine> segmentedLines;

    private static final float DEFAULT_WIDTH = 1f;
    private static final float DEFAULT_HEIGHT = 1f;

    private final int horizontalSegments;

    private final String[] seperateColorNames;
    private final MultiColorText[] horizontalLabels, seperateColorLabels;

    private final String horizontalAxisString, verticalAxisString;
    private MultiColorText horizontalAxisText, verticalAxisText;

    public LineGraph2D(int horizontalSegments, Color4[] colors, String[] seperateColorNames,
            String horizontalAxisString, String verticalAxisString) {
        this.horizontalAxisString = horizontalAxisString;
        this.verticalAxisString = verticalAxisString;
        this.seperateColorNames = seperateColorNames;

        this.colors = colors;
        this.horizontalSegments = horizontalSegments;

        this.segmentedLines = new HashMap<Integer, SegmentedLine>();
        for (int i = 0; i < colors.length; i++) {
            segmentedLines.put(i, new SegmentedLine(horizontalSegments, DEFAULT_WIDTH / horizontalSegments, colors[i]));
        }

        this.horizontalLabels = new MultiColorText[NR_OF_HORIZONTAL_LABELS];
        this.seperateColorLabels = new MultiColorText[seperateColorNames.length];

        this.font = FontFactory.get(fontSet).getDefault();
    }

    public void addData(int colorIndex, float horizontal, float vertical) {
        SegmentedLine sl = segmentedLines.get(colorIndex);
        boolean dimensionsChanged = sl.addData(horizontal, vertical);

        if (dimensionsChanged) {
            float minHorizontal = sl.getMinHorizontal();
            float maxHorizontal = sl.getMaxHorizontal();
            float minVertical = sl.getMinVertical();
            float maxVertical = sl.getMaxVertical();

            // Change the dimensions of all other colored graphs to the same as
            // the current one.
            for (SegmentedLine sl2 : segmentedLines.values()) {
                if (!sl2.equals(sl)) {
                    sl2.applyNewDimensions(minHorizontal, maxHorizontal, minVertical, maxVertical);
                }
            }
        }
    }

    public void init(GL3 gl) {
        for (SegmentedLine sl : segmentedLines.values()) {
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

        for (int i = 0; i < NR_OF_HORIZONTAL_LABELS; i++) {
            int segmentIndex = (horizontalSegments / NR_OF_HORIZONTAL_LABELS) * i;
            if (horizontalLabels[i] != null) {
                horizontalLabels[i].delete(gl);
            }

            float rawValue = segmentedLines.get(0).getSegmentValue(segmentIndex);
            int neatValue = 0;
            if (segmentedLines.get(0).getMaxHorizontal() > 1000) {
                neatValue = ((int) Math.floor(rawValue / 100) * 100);
            } else if (segmentedLines.get(0).getMaxHorizontal() > 100) {
                neatValue = ((int) Math.floor(rawValue / 10) * 10);
            } else {
                neatValue = (int) Math.floor(rawValue);
            }

            String text = Integer.toString(neatValue);

            horizontalLabels[i] = new MultiColorText(gl, font, text, Color4.WHITE, FONTSIZE);
            horizontalLabels[i].setString(gl, text, Color4.WHITE, FONTSIZE);
        }
    }

    public void draw(GL3 gl, Float4Matrix mv, ModelViewStack mvStack, ShaderProgram program)
            throws UninitializedException {
        int i = 0;
        for (SegmentedLine sl : segmentedLines.values()) {
            ModelViewStack linesStack = new ModelViewStack(mvStack);
            linesStack.putBottom(FloatMatrixMath.translate(0f, 0f, i * (DEFAULT_WIDTH / colors.length)));
            program.setUniformMatrix("MVMatrix", linesStack.calc(mv));

            gl.glLineWidth(3f);

            sl.draw(gl, program);
            i++;
        }
    }

    public void drawLabels(GL3 gl, Float4Matrix mv, ModelViewStack mvStack, ShaderProgram program)
            throws UninitializedException {
        float widthPerSegment = DEFAULT_WIDTH / horizontalSegments;

        float scale = .0025f;

        Float4Matrix scaleMatrix = FloatMatrixMath.scale(scale);

        ModelViewStack horizontalAxisTextStack = new ModelViewStack(mvStack);
        Float4Matrix horizontalAxisTranslation = FloatMatrixMath.translate(new Float3Vector(0f, -0.15f, DEFAULT_WIDTH));
        horizontalAxisTextStack.putTop(horizontalAxisTranslation);
        horizontalAxisTextStack.putTop(scaleMatrix);
        program.setUniformMatrix("MVMatrix", horizontalAxisTextStack.calc(mv));
        horizontalAxisText.draw(gl, program);

        for (int i = 0; i < seperateColorLabels.length; i++) {
            MultiColorText label = seperateColorLabels[i];

            ModelViewStack colorLabelStack = new ModelViewStack(mvStack);
            Float4Matrix colorLabelTranslation = FloatMatrixMath.translate(new Float3Vector(0f, -0.2f, i
                    * (DEFAULT_WIDTH / colors.length)));
            colorLabelStack.putTop(colorLabelTranslation);
            colorLabelStack.putTop(scaleMatrix);
            program.setUniformMatrix("MVMatrix", colorLabelStack.calc(mv));
            label.draw(gl, program);
        }

        ModelViewStack verticalAxisTextStack = new ModelViewStack(mvStack);
        Float4Matrix verticalAxisRotation = FloatMatrixMath.rotationZ(90f);
        verticalAxisTextStack.putTop(verticalAxisRotation);

        Float4Matrix verticalAxisTranslation = FloatMatrixMath.translate(new Float3Vector(.15f, 0.05f, DEFAULT_WIDTH));
        verticalAxisTextStack.putTop(verticalAxisTranslation);

        verticalAxisTextStack.putTop(scaleMatrix);
        program.setUniformMatrix("MVMatrix", verticalAxisTextStack.calc(mv));
        verticalAxisText.draw(gl, program);

        Float4Matrix horizontalLabelRotationMatrix = FloatMatrixMath.rotationZ(-90f);

        for (int i = 0; i < NR_OF_HORIZONTAL_LABELS; i++) {
            MultiColorText label = horizontalLabels[i];

            Float4Matrix horizontalLabelTranslation = FloatMatrixMath.translate(0.2f, (widthPerSegment * i
                    * horizontalSegments / NR_OF_HORIZONTAL_LABELS)
                    + (.2f * widthPerSegment), DEFAULT_WIDTH);

            ModelViewStack horizontalLabelStack = new ModelViewStack(mvStack);
            horizontalLabelStack.putTop(horizontalLabelRotationMatrix);
            horizontalLabelStack.putTop(horizontalLabelTranslation);
            horizontalLabelStack.putTop(scaleMatrix);
            program.setUniformMatrix("MVMatrix", horizontalLabelStack.calc(mv));
            label.draw(gl, program);
        }
    }
}
