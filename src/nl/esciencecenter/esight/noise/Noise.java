package nl.esciencecenter.esight.noise;

import java.nio.ByteBuffer;


import com.jogamp.common.nio.Buffers;

/**
 * Noise creation class for visual effects. Didn't write this as much as
 * scrounged it together from various internet sources. Typical useage is
 * demonstrated in the {@link Perlin3D} class.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Noise {
    public ByteBuffer pixelBuffer;

    public Noise(int channels, int width, int height, int depth) {
        int pixels = width * height * depth;
        pixelBuffer = Buffers.newDirectByteBuffer(pixels * 4);

        for (int x = 0; x < width; x++) {
            // System.out.print(".");
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    PerlinNoise_3D(channels, x, y, z);
                }
            }
        }
        pixelBuffer.rewind();
    }

    double PerlinNoise_3D(int channels, int x, int y, int z) {
        double total = 0;
        double p = .25;

        double amplitude = 128.0;
        double frequency = 0.1;
        for (int i = 0; i < channels; i++) {
            pixelBuffer.put((byte) (ImprovedPerlinNoise.noise(x * frequency, y
                    * frequency, z * frequency) * amplitude));

            amplitude *= p;
            frequency *= 2;
        }
        return total;
    }

    public ByteBuffer getImage() {
        return pixelBuffer;
    }

    // 3D Noise map
    @SuppressWarnings("unused")
    private float interpolatedNoise3(float xf, float yf, float zf) {
        int x = (int) xf;
        int y = (int) yf;
        int z = (int) zf;
        float fracX = xf - x;
        float fracY = yf - y;
        float fracZ = zf - z;

        float v1 = smoothNoise3(x, y, z);
        float v2 = smoothNoise3(x + 1, y, z);
        float v3 = smoothNoise3(x, y + 1, z);
        float v4 = smoothNoise3(x + 1, y + 1, z);
        float v5 = smoothNoise3(x, y, z + 1);
        float v6 = smoothNoise3(x + 1, y, z + 1);
        float v7 = smoothNoise3(x, y + 1, z + 1);
        float v8 = smoothNoise3(x + 1, y + 1, z + 1);

        float iX1 = cosInterpolate(v1, v2, fracX);
        float iX2 = cosInterpolate(v3, v4, fracX);
        float iX3 = cosInterpolate(v5, v6, fracX);
        float iX4 = cosInterpolate(v7, v8, fracX);

        float iY1 = cosInterpolate(iX1, iX2, fracY);
        float iY2 = cosInterpolate(iX3, iX4, fracY);

        float result = cosInterpolate(iY1, iY2, fracZ);

        return result;
    }

    private float smoothNoise3(int x, int y, int z) {
        float corners = (noise3(x - 1, y - 1, z - 1)
                + noise3(x + 1, y - 1, z - 1) + noise3(x - 1, y + 1, z - 1)
                + noise3(x + 1, y + 1, z - 1) + noise3(x - 1, y - 1, z + 1)
                + noise3(x + 1, y - 1, z + 1) + noise3(x - 1, y + 1, z + 1) + noise3(
                x + 1, y + 1, z + 1)) / 32f;

        float sides = (noise3(x - 1, y, z) + noise3(x + 1, y, z)
                + noise3(x, y - 1, z) + noise3(x, y + 1, z)
                + noise3(x, y, z - 1) + noise3(x, y, z + 1)) / 12f;

        float center = noise3(x, y, z) / 4f;

        return corners + sides + center;
    }

    private float noise3(int x, int y, int z) {
        int n = x + y * 57 + z * 93;
        n = (n << 13) ^ n;
        float noise = 1.0f - (((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0f);
        return noise;
    }

    // 2D Noise maps
    @SuppressWarnings("unused")
    private float interpolatedNoise(int octave, float xf, float yf) {
        int x = (int) xf;
        int y = (int) yf;
        float fracX = xf - x;
        float fracY = yf - y;

        float v1 = smoothNoise(octave, x, y);
        float v2 = smoothNoise(octave, x + 1, y);
        float v3 = smoothNoise(octave, x, y + 1);
        float v4 = smoothNoise(octave, x + 1, y + 1);

        float i1 = cosInterpolate(v1, v2, fracX);
        float i2 = cosInterpolate(v3, v4, fracX);

        return cosInterpolate(i1, i2, fracY);
    }

    private float smoothNoise(int octave, int x, int y) {
        float corners = (noise2(octave, x - 1, y - 1)
                + noise2(octave, x + 1, y - 1) + noise2(octave, x - 1, y + 1) + noise2(
                octave, x + 1, y + 1)) / 16;

        float sides = (noise2(octave, x - 1, y) + noise2(octave, x + 1, y)
                + noise2(octave, x, y - 1) + noise2(octave, x, y + 1)) / 8;

        float center = noise2(octave, x, y) / 4;

        return corners + sides + center;
    }

    private float noise2(int octave, int x, int y) {
        int[] primes = new int[4];
        if (octave == 0) {
            primes[0] = 15731;
            primes[1] = 789221;
            primes[2] = 1376312589;
            primes[3] = 1073741824;
        } else if (octave == 1) {
            primes[0] = 15733;
            primes[1] = 789227;
            primes[2] = 1376312627;
            primes[3] = 1073741827;
        } else if (octave == 2) {
            primes[0] = 15737;
            primes[1] = 789251;
            primes[2] = 1376312629;
            primes[3] = 1073741831;
        } else if (octave == 3) {
            primes[0] = 15739;
            primes[1] = 789311;
            primes[2] = 1376312657;
            primes[3] = 1073741833;
        }

        int n = x + y * 57;
        n = (n << 13) ^ n;

        float noise = (1.0f - ((n * (n * n * primes[0] + primes[1]) + primes[2]) & 0x7fffffff)
                / primes[3]);

        System.out.println("noise: " + noise);
        return noise;
    }

    private float cosInterpolate(float a, float b, float x) {
        float ft = x * 3.1415927f;
        float f = (1f - (float) Math.cos(ft)) * .5f;

        return a * (1 - f) + b * f;
    }

}
