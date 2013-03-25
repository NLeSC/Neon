package nl.esciencecenter.esight.input;

import nl.esciencecenter.esight.math.MatF4;
import nl.esciencecenter.esight.math.VecF2;
import nl.esciencecenter.esight.math.VecF4;

public class ClickCoordinateCalculator {
    static float getDiv3(float var) {
        float result = 0;
        if (var < .33333f) {
            result = var * 3f;
        } else if (var < .66666f) {
            result = (var - .33333f) * 3f;
        } else {
            result = (var - .66666f) * 3f;
        }

        return result;
    }

    static float getDiv2(float var) {
        float result = 0;
        if (var < .5f) {
            result = var * 2f;
        } else {
            result = (var - .5f) * 2f;
        }

        return result;
    }

    public static VecF2 calc(int numCols, int numRows, float scrWidth,
            float scrHeight, float x, float y) {
        VecF2 tCoord;

        float relativeX = x / scrWidth;
        float relativeY = y / scrHeight;

        if (numCols == 2 && numRows == 2) {
            float conv_x = getDiv2(relativeX);
            float conv_y = getDiv2(relativeY);

            tCoord = new VecF2(conv_x, conv_y);
        } else if (numCols == 2 && numRows == 1) {
            float conv_x = getDiv2(relativeX);

            tCoord = new VecF2(conv_x, relativeY);
            // }
        } else if (numCols == 1 && numRows == 2) {
            float conv_y = getDiv2(relativeY);

            tCoord = new VecF2(relativeX, conv_y);
        } else {
            tCoord = new VecF2(relativeX, relativeY);
        }

        return tCoord;
    }

    public static VecF4 surfaceCoord(VecF2 clickCoords, MatF4 p, MatF4 mv) {
        VecF4 result = new VecF4();

        return result;
    }
}
