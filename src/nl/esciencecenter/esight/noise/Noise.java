package nl.esciencecenter.esight.noise;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

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
    private final FloatBuffer tempBuf;
    private final int pixels;

    public Noise(int channels, int width, int height, int depth) {
        pixels = width * height * depth;

        tempBuf = Buffers.newDirectFloatBuffer(pixels);

        if (depth == 0 || depth == 1) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    float noise = perlinNoise2D(channels, x, y);
                    tempBuf.put(noise);
                }
            }

        } else {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    for (int z = 0; z < depth; z++) {
                        float noise = perlinNoise3D(channels, x, y, z);
                        tempBuf.put(noise);
                    }
                }
            }
        }

        tempBuf.rewind();
    }

    private float perlinNoise3D(int channels, int x, int y, int z) {
        float total = 0f;
        float p = .75f;

        float amplitude = 128.0f;
        float frequency = 0.05f;

        for (int i = 0; i < channels; i++) {
            total += (float) ImprovedPerlinNoise.noise(x * frequency, y * frequency, z * frequency) * amplitude;

            amplitude *= p;
            frequency *= 2;
        }
        return total + 128f;
    }

    private float perlinNoise2D(int channels, int x, int y) {
        float total = 0f;
        float p = .75f;

        float amplitude = 128.0f;
        float frequency = 0.05f;

        for (int i = 0; i < channels; i++) {
            total += interpolatedNoise(x * frequency, y * frequency) * amplitude;

            amplitude *= p;
            frequency *= 2;
        }

        return total + 128f;
    }

    public FloatBuffer getFloats() {
        return tempBuf.duplicate();
    }

    public ByteBuffer getPixelBuffer() {
        ByteBuffer result = ByteBuffer.allocate(pixels * 4);

        for (int i = 0; i < tempBuf.capacity(); i++) {
            float val = tempBuf.get(i);
            result.put((byte) (val));
            result.put((byte) (val));
            result.put((byte) (val));
            result.put((byte) (val));
        }

        result.rewind();
        tempBuf.rewind();

        return result;
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

        return (1.0f - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824f);
    }

    private float cosInterpolate(float a, float b, float x) {
        float ft = x * 3.1415927f;
        float f = (1f - (float) Math.cos(ft)) * .5f;

        return a * (1 - f) + b * f;
    }

}
