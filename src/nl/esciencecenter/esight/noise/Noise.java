package nl.esciencecenter.esight.noise;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.common.nio.Buffers;

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
 * Noise creation class for visual effects. Didn't write this as much as
 * scrounged it together from various internet sources. Typical useage is
 * demonstrated in the {@link Perlin3D} class.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Noise {
    private final static Logger logger = LoggerFactory.getLogger(Noise.class);
    public ByteBuffer pixelBuffer;
    public FloatBuffer tempBuf;

    public Noise(int channels, int width, int height, int depth) {
        int pixels = width * height * depth;
        pixelBuffer = Buffers.newDirectByteBuffer(pixels * 4);

        tempBuf = Buffers.newDirectFloatBuffer(pixels);

        if (depth == 0 || depth == 1) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    float noise = PerlinNoise_2D(channels, x, y);
                    tempBuf.put(noise);
                }
            }

        } else {
            for (int x = 0; x < width; x++) {
                // System.out.print(".");
                for (int y = 0; y < height; y++) {
                    for (int z = 0; z < depth; z++) {
                        PerlinNoise_3D(pixelBuffer, channels, x, y, z);
                    }
                }
            }
        }
        pixelBuffer.rewind();
    }

    double PerlinNoise_3D(ByteBuffer buffer, int channels, int x, int y, int z) {
        double total = 0;
        double p = .25;

        double amplitude = 128.0;
        double frequency = 0.1;
        for (int i = 0; i < channels; i++) {
            buffer.put((byte) (ImprovedPerlinNoise.noise(x * frequency, y * frequency, z * frequency) * amplitude));

            amplitude *= p;
            frequency *= 2;
        }
        return total;
    }

    float PerlinNoise_2D(int channels, int x, int y) {
        float total = 0f;
        for (int i = 0; i < channels; i++) {
            float amplitude = (float) Math.pow(2.0, i);
            float frequency = (float) Math.pow(0.5, i);

            float noise = interpolatedNoise(x * frequency, y * frequency) * amplitude;

            total += noise;
        }

        return total;
    }

    public ByteBuffer getImage() {
        return pixelBuffer;
    }

    public FloatBuffer getFloats() {
        return tempBuf;
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
        float corners = (noise3(x - 1, y - 1, z - 1) + noise3(x + 1, y - 1, z - 1) + noise3(x - 1, y + 1, z - 1)
                + noise3(x + 1, y + 1, z - 1) + noise3(x - 1, y - 1, z + 1) + noise3(x + 1, y - 1, z + 1)
                + noise3(x - 1, y + 1, z + 1) + noise3(x + 1, y + 1, z + 1)) / 32f;

        float sides = (noise3(x - 1, y, z) + noise3(x + 1, y, z) + noise3(x, y - 1, z) + noise3(x, y + 1, z)
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
    private float interpolatedNoise(float xf, float yf) {
        int x = (int) xf;
        int y = (int) yf;
        float fracX = xf - x;
        float fracY = yf - y;

        float noiseCurvePoint00 = smoothNoise(x, y);
        float noiseCurvePoint10 = smoothNoise(x + 1, y);

        float noiseCurvePoint01 = smoothNoise(x, y + 1);
        float noiseCurvePoint11 = smoothNoise(x + 1, y + 1);

        float i1 = cosInterpolate(noiseCurvePoint00, noiseCurvePoint10, fracX);
        float i2 = cosInterpolate(noiseCurvePoint01, noiseCurvePoint11, fracX);

        return cosInterpolate(i1, i2, fracY);
    }

    private float smoothNoise(int x, int y) {
        float corners = (noise2(x - 1, y - 1) + noise2(x + 1, y - 1) + noise2(x - 1, y + 1) + noise2(x + 1, y + 1)) / 16f;
        float sides = (noise2(x - 1, y) + noise2(x + 1, y) + noise2(x, y - 1) + noise2(x, y + 1)) / 8f;
        float center = noise2(x, y) / 4f;

        return corners + sides + center;
    }

    private float noise2(int x, int y) {
        int n = x + y * 57;
        n = (n << 13) ^ n;

        float noise = (1.0f - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824);
        return noise;
    }

    private float cubicInterpolate(float v0, float v1, float v2, float v3, float x) {
        float P = (v3 - v2) - (v0 - v1);
        float Q = (v0 - v1) - P;
        float R = v2 - v0;
        float S = v1;

        return (float) (P * Math.pow(x, 3) + Q * Math.pow(x, 2) + R * x + S);
    }

    private float cosInterpolate(float a, float b, float x) {
        float ft = x * 3.1415927f;
        float f = (1f - (float) Math.cos(ft)) * .5f;

        return a * (1 - f) + b * f;
    }

    @SuppressWarnings("unused")
    private float linearInterpolate(float a, float b, float x) {
        return (b - a) * x + a;
    }

}
