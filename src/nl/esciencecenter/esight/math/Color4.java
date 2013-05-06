package nl.esciencecenter.esight.math;

/* Copyright [2013] [Netherlands eScience Center]
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
 * More specific implementation of a color VecF4.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Color4 extends VecF4 {
    public static Color4 black = new Color4(0.0f, 0.0f, 0.0f, 1.0f);
    public static Color4 white = new Color4(1.0f, 1.0f, 1.0f, 1.0f);

    public static Color4 red = new Color4(1.0f, 0.0f, 0.0f, 1.0f);
    public static Color4 sickly_red = new Color4(0.8f, 0.2f, 0.2f, 1.0f);
    public static Color4 green = new Color4(0.0f, 1.0f, 0.0f, 1.0f);
    public static Color4 sickly_green = new Color4(0.2f, 0.8f, 0.2f, 1.0f);
    public static Color4 blue = new Color4(0.0f, 0.0f, 1.0f, 1.0f);
    public static Color4 sickly_blue = new Color4(0.2f, 0.2f, 0.8f, 1.0f);

    public static Color4 yellow = new Color4(1.0f, 1.0f, 0.0f, 1.0f);
    public static Color4 sickly_yellow = new Color4(0.8f, 0.8f, 0.2f, 1.0f);
    public static Color4 magenta = new Color4(1.0f, 0.0f, 1.0f, 1.0f);
    public static Color4 sickly_magenta = new Color4(0.8f, 0.2f, 0.8f, 1.0f);
    public static Color4 cyan = new Color4(0.0f, 1.0f, 1.0f, 1.0f);
    public static Color4 sickly_cyan = new Color4(0.2f, 0.8f, 0.8f, 1.0f);

    public static Color4 t_black = new Color4(0.0f, 0.0f, 0.0f, 0.2f);
    public static Color4 t_red = new Color4(1.0f, 0.0f, 0.0f, 0.2f);
    public static Color4 t_yellow = new Color4(1.0f, 1.0f, 0.0f, 0.2f);
    public static Color4 t_green = new Color4(0.0f, 1.0f, 0.0f, 0.2f);
    public static Color4 t_blue = new Color4(0.0f, 0.0f, 1.0f, 0.2f);
    public static Color4 t_magenta = new Color4(1.0f, 0.0f, 1.0f, 0.2f);
    public static Color4 t_white = new Color4(1.0f, 1.0f, 1.0f, 0.2f);
    public static Color4 t_cyan = new Color4(0.0f, 1.0f, 1.0f, 0.2f);

    /**
     * Stand-in for a vector, in which the places represent R, G, B and A
     * values.
     */
    public Color4() {
        super();
    }

    /**
     * Stand-in for a vector, in which the places represent R, G, B and A
     * values.
     * 
     * @param v
     *            A vector to be cloned as a color value.
     */
    public Color4(VecF4 v) {
        super(v);
    }

    /**
     * Stand-in for a vector, in which the places represent R, G, B and A
     * values.
     * 
     * @param r
     *            The Red component to this color.
     * @param g
     *            The Green component to this color.
     * @param b
     *            The Blue component to this color.
     * @param a
     *            The Alpha component to this color.
     */
    public Color4(float r, float g, float b, float a) {
        super(r, g, b, a);
    }
}
