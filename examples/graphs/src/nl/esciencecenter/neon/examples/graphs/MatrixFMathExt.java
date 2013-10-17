package nl.esciencecenter.neon.examples.graphs;

import nl.esciencecenter.neon.exceptions.InverseNotAvailableException;
import nl.esciencecenter.neon.math.Float4Matrix;
import nl.esciencecenter.neon.math.FloatMatrixMath;
import nl.esciencecenter.neon.math.Float3Vector;
import nl.esciencecenter.neon.math.Float4Vector;

public class MatrixFMathExt {
    public MatrixFMathExt() {
    }

    public static Float4Vector unProject(Float4Matrix projection, Float4Matrix modelView, float[] viewPort, Float3Vector windowCoords)
            throws InverseNotAvailableException {
        float calcX = 2f * (windowCoords.getX() - viewPort[0]) / viewPort[2] - 1f;
        float calcY = 2f * (windowCoords.getY() - viewPort[1]) / viewPort[3] - 1f;
        float calcZ = 2f * windowCoords.getZ() - 1f;

        return FloatMatrixMath.inverse(projection.mul(modelView)).mul(new Float4Vector(calcX, calcY, calcZ, 1f));
    }
}
