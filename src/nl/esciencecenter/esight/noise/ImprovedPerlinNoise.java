package nl.esciencecenter.esight.noise;

//JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE - COPYRIGHT 2002 KEN PERLIN.

/**
 * Improved perlin noise generator , see http://mrl.nyu.edu/~perlin/noise/
 */
public final class ImprovedPerlinNoise {
    private ImprovedPerlinNoise() {
        // Utility class
    }

    static public double noise(double x, double y, double z) {
        double tmpX = x, tmpY = y, tmpZ = z;

        // Find unit cube that contains point.
        int iX = (int) Math.floor(x) & 255;
        int iY = (int) Math.floor(tmpY) & 255;
        int iZ = (int) Math.floor(tmpZ) & 255;

        // Find relative X,Y,Z of point in cube.
        tmpX -= Math.floor(tmpX);
        tmpY -= Math.floor(tmpY);
        tmpZ -= Math.floor(tmpZ);

        // Compute fade curves for each X,Y,Z
        double u = fade(tmpX);
        double v = fade(tmpY);
        double w = fade(tmpZ);

        // Get hash coordinates of the 8 cube corners
        int iA = p[iX] + iY;
        int iAA = p[iA] + iZ;
        int iAB = p[iA + 1] + iZ;
        int iB = p[iX + 1] + iY;
        int iBA = p[iB] + iZ;
        int iBB = p[iB + 1] + iZ;

        // Add blended results from the 8 cube corners
        return lerp(
                w,
                lerp(v, lerp(u, grad(p[iAA], tmpX, tmpY, tmpZ), grad(p[iBA], tmpX - 1, tmpY, tmpZ)),
                        lerp(u, grad(p[iAB], tmpX, tmpY - 1, tmpZ), grad(p[iBB], tmpX - 1, tmpY - 1, tmpZ))),
                lerp(v,
                        lerp(u, grad(p[iAA + 1], tmpX, tmpY, tmpZ - 1), grad(p[iBA + 1], tmpX - 1, tmpY, tmpZ - 1)),
                        lerp(u, grad(p[iAB + 1], tmpX, tmpY - 1, tmpZ - 1),
                                grad(p[iBB + 1], tmpX - 1, tmpY - 1, tmpZ - 1))));
    }

    static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    static double grad(int hash, double x, double y, double z) {
        // Convert 4 bits of hash code into 12 gradient directions.
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    static final int p[] = new int[512];
    static final int permutation[] = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36,
            103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219,
            203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71,
            134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46,
            245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196,
            135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38,
            147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213,
            119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108,
            110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191,
            179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204,
            176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195,
            78, 66, 215, 61, 156, 180 };

    static {
        for (int i = 0; i < 256; i++) {
            p[256 + i] = permutation[i];
            p[i] = permutation[i];
        }
    }
}
