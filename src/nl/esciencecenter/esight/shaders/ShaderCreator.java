package nl.esciencecenter.esight.shaders;

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
 * Example class for shader-source string creation.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public class ShaderCreator {
    private final int glslVersion;

    /**
     * Constructor.
     * 
     * @param glslVersion
     *            the OpenGL shader language version number to be used for
     *            shaders created by this class.
     */
    public ShaderCreator(int glslVersion) {
        this.glslVersion = glslVersion;
    }

    public String generatePostProcessShader(int rows, int columns) {
        String shaderText = "#version " + glslVersion + "\n\n";

        for (int i = 0; i < rows * columns; i++) {
            shaderText += "uniform sampler2D sphereTexture_" + i + ";\n";
        }

        shaderText += "uniform float sphereBrightness;\n";
        shaderText += "uniform int scrWidth;\n";
        shaderText += "uniform int scrHeight;\n\n";
        shaderText += "uniform int selection;\n";

        shaderText += "out vec4 fragColor;\n\n";

        shaderText += "const float overallBrightness = 2.0;\n\n";

        shaderText += "" + "float getDiv3(float var) {\n" + "    if (var < (1.0/3.0)) {\n" + "        var = var*3.0;\n"
                + "    } else if (var < (2.0/3.0)) {\n" + "        var = (var - (1.0/3.0)) * 3.0;\n" + "    } else {\n"
                + "        var = (var - (2.0/3.0)) * 3.0;\n" + "    }\n" + "    return var;\n" + "}\n\n";

        shaderText += "" + "float getDiv2(float var) {\n" + "    if (var < (1.0/2.0)) {\n" + "        var = var*2.0;\n"
                + "    } else {\n" + "        var = (var - (1.0/2.0)) * 2.0;\n" + "    }\n" + "    return var;\n"
                + "}\n\n";

        shaderText += "" + "void main() {\n" + "    float width = float(scrWidth);\n"
                + "    float height = float(scrHeight);\n" + "    float x = gl_FragCoord.x/width;\n"
                + "    float y = gl_FragCoord.y/height;\n" + "\n" + "    vec4 sphereColor;\n"
                + "    if (selection == 0) {\n";

        if (columns == 3 && rows == 3) {
            shaderText += "" + "        float conv_x = getDiv3(x);\n" + "        float conv_y = getDiv3(y);\n"
                    + "        \n" + "        vec2 tCoord = vec2(conv_x,conv_y);\n" + "    \n"
                    + "        if (y < (1.0/3.0)) {\n" + "            if (x < (1.0/3.0)) {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_0, tCoord).rgb, 1.0);\n"
                    + "            } else if (x < (2.0/3.0)) {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_1, tCoord).rgb, 1.0);\n"
                    + "            } else {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_2, tCoord).rgb, 1.0);\n"
                    + "            }\n" + "        } else if (y < (2.0/3.0)) {\n"
                    + "            if (x < (1.0/3.0)) {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_3, tCoord).rgb, 1.0);\n"
                    + "            } else if (x < (2.0/3.0)) {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_4, tCoord).rgb, 1.0);\n"
                    + "            } else {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_5, tCoord).rgb, 1.0);\n"
                    + "            }\n" + "        } else {\n" + "            if (x < (1.0/3.0)) {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_6, tCoord).rgb, 1.0);\n"
                    + "            } else if (x < (2.0/3.0)) {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_7, tCoord).rgb, 1.0);\n"
                    + "            } else {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_8, tCoord).rgb, 1.0);\n"
                    + "            }\n" + "        }\n";
        } else if (columns == 2 && rows == 2) {
            shaderText += "" + "        float conv_x = getDiv2(x);\n" + "        float conv_y = getDiv2(y);\n"
                    + "        vec2 tCoord = vec2(conv_x,conv_y);\n" + "    \n" + "        if (y < (1.0/2.0)) {\n"
                    + "            if (x < (1.0/2.0)) {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_0, tCoord).rgb, 1.0);\n"
                    + "            } else {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_1, tCoord).rgb, 1.0);\n"
                    + "            }\n" + "        } else {\n" + "            if (x < (1.0/2.0)) {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_2, tCoord).rgb, 1.0);\n"
                    + "            } else {\n"
                    + "                sphereColor = vec4(texture(sphereTexture_3, tCoord).rgb, 1.0);\n"
                    + "            }\n" + "        }\n";
        } else if (columns == 2 && rows == 1) {
            shaderText += "" + "        float conv_x = getDiv2(x);\n" + "        vec2 tCoord = vec2(conv_x,y);\n"
                    + "        \n" + "        if (x < (1.0/2.0)) {\n"
                    + "            sphereColor = vec4(texture(sphereTexture_0, tCoord).rgb, 1.0);\n"
                    + "        } else {\n"
                    + "            sphereColor = vec4(texture(sphereTexture_1, tCoord).rgb, 1.0);\n" + "        }\n";
        } else if (columns == 1 && rows == 2) {
            shaderText += "" + "        float conv_y = getDiv2(y);\n" + "        vec2 tCoord = vec2(x,conv_y);\n"
                    + "        \n" + "        if (y < (1.0/2.0)) {\n"
                    + "            sphereColor = vec4(texture(sphereTexture_1, tCoord).rgb, 1.0);\n"
                    + "        } else {\n"
                    + "            sphereColor = vec4(texture(sphereTexture_0, tCoord).rgb, 1.0);\n" + "        }\n";
        }

        shaderText += "" + "    } else if (selection == 1) {\n" + "        vec2 tCoord = vec2(x,y);\n"
                + "        sphereColor = vec4(texture(sphereTexture_0, tCoord).rgb, 1.0);\n";

        for (int i = 1; i < rows * columns; i++) {
            shaderText += "    } else if (selection == " + (i + 1) + ") {\n" + "        vec2 tCoord = vec2(x,y);\n"
                    + "        sphereColor = vec4(texture(sphereTexture_" + (i) + ", tCoord).rgb, 1.0);\n";
        }

        shaderText += "    }\n" + "    vec4 color = sphereColor;\n" + "    fragColor = vec4(color.rgb, 1.0);\n" + "}\n";

        return shaderText;
    }
}
