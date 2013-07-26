package nl.esciencecenter.esight.experimental;

import nl.esciencecenter.esight.math.MatF4;
import nl.esciencecenter.esight.math.VecF2;
import nl.esciencecenter.esight.math.VecF4;

/* Copyright 2013 Netherlands eScience Center
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
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
 * Experimental class, use at your own risk.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
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

    public static VecF2 calc(int numCols, int numRows, float scrWidth, float scrHeight, float x, float y) {
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
