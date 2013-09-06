package nl.esciencecenter.esight.examples.graphs;

import nl.esciencecenter.esight.exceptions.InverseNotAvailableException;
import nl.esciencecenter.esight.math.MatF4;
import nl.esciencecenter.esight.math.MatrixFMath;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VecF4;

public class MatrixFMathExt {
    public MatrixFMathExt() {
    }

    public static VecF4 unProject(MatF4 projection, MatF4 modelView, float[] viewPort, VecF3 windowCoords)
            throws InverseNotAvailableException {
        float calcX = 2f * (windowCoords.getX() - viewPort[0]) / viewPort[2] - 1f;
        float calcY = 2f * (windowCoords.getY() - viewPort[1]) / viewPort[3] - 1f;
        float calcZ = 2f * windowCoords.getZ() - 1f;

        return MatrixFMath.inverse(projection.mul(modelView)).mul(new VecF4(calcX, calcY, calcZ, 1f));
    }
}
