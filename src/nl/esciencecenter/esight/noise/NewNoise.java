package nl.esciencecenter.esight.noise;

/**
 * Currently unused class, stored for reference.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class NewNoise {
    int NOISE_MAGIC_X    = 1619;
    int NOISE_MAGIC_Y    = 31337;
    int NOISE_MAGIC_Z    = 52591;
    int NOISE_MAGIC_SEED = 1013;

    double triLinearInterpolation(
            double v000, double v100, double v010, double v110,
            double v001, double v101, double v011, double v111,
            double x, double y, double z)
    {
        /*
         * double tx = easeCurve(x);
         * double ty = easeCurve(y);
         * double tz = easeCurve(z);
         */
        double tx = x;
        double ty = y;
        double tz = z;
        return (v000 * (1 - tx) * (1 - ty) * (1 - tz) +
                v100 * tx * (1 - ty) * (1 - tz) +
                v010 * (1 - tx) * ty * (1 - tz) +
                v110 * tx * ty * (1 - tz) +
                v001 * (1 - tx) * (1 - ty) * tz +
                v101 * tx * (1 - ty) * tz +
                v011 * (1 - tx) * ty * tz + v111 * tx * ty * tz);
    }

    double noise3d_perlin(double x, double y, double z, int seed,
            int octaves, double persistence)
    {
        double a = 0;
        double f = 1.0;
        double g = 1.0;
        for (int i = 0; i < octaves; i++)
        {
            a += g * noise3d_gradient(x * f, y * f, z * f, seed + i);
            f *= 2.0;
            g *= persistence;
        }
        return a;
    }

    double noise3d_gradient(double x, double y, double z, int seed)
    {
        // Calculate the integer coordinates
        int x0 = (x > 0.0 ? (int) x : (int) x - 1);
        int y0 = (y > 0.0 ? (int) y : (int) y - 1);
        int z0 = (z > 0.0 ? (int) z : (int) z - 1);
        // Calculate the remaining part of the coordinates
        double xl = x - x0;
        double yl = y - y0;
        double zl = z - z0;
        // Get values for corners of cube
        double v000 = noise3d(x0, y0, z0, seed);
        double v100 = noise3d(x0 + 1, y0, z0, seed);
        double v010 = noise3d(x0, y0 + 1, z0, seed);
        double v110 = noise3d(x0 + 1, y0 + 1, z0, seed);
        double v001 = noise3d(x0, y0, z0 + 1, seed);
        double v101 = noise3d(x0 + 1, y0, z0 + 1, seed);
        double v011 = noise3d(x0, y0 + 1, z0 + 1, seed);
        double v111 = noise3d(x0 + 1, y0 + 1, z0 + 1, seed);
        // Interpolate
        return triLinearInterpolation(v000, v100, v010, v110, v001, v101, v011, v111, xl, yl, zl);
    }

    double noise3d(int x, int y, int z, int seed)
    {
        int n = (NOISE_MAGIC_X * x + NOISE_MAGIC_Y * y + NOISE_MAGIC_Z * z
                + NOISE_MAGIC_SEED * seed) & 0x7fffffff;
        n = (n >> 13) ^ n;
        n = (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;
        return 1.0 - (double) n / 1073741824;
    }
}
