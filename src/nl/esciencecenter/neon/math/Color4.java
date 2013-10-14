package nl.esciencecenter.neon.math;

/* Copyright [2013] [Netherlands eScience Center]
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
 * More specific implementation of a color VecF4.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Color4 extends VecF4 {
    public static final Color4 BLACK = new Color4(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color4 WHITE = new Color4(1.0f, 1.0f, 1.0f, 1.0f);

    public static final Color4 RED = new Color4(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color4 GREEN = new Color4(0.0f, 1.0f, 0.0f, 1.0f);
    public static final Color4 BLUE = new Color4(0.0f, 0.0f, 1.0f, 1.0f);

    public static final Color4 YELLOW = new Color4(1.0f, 1.0f, 0.0f, 1.0f);
    public static final Color4 MAGENTA = new Color4(1.0f, 0.0f, 1.0f, 1.0f);
    public static final Color4 CYAN = new Color4(0.0f, 1.0f, 1.0f, 1.0f);

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
        super();

        boolean rSmall = (r >= 0.0f && r <= 1.0f);
        boolean gSmall = (g >= 0.0f && g <= 1.0f);
        boolean bSmall = (b >= 0.0f && b <= 1.0f);
        boolean aSmall = (a >= 0.0f && a <= 1.0f);

        if (rSmall && gSmall && bSmall && aSmall) {
            // Interpret values as 0.0f < X < 1.0f
            setR(r);
            setG(g);
            setB(b);
            setA(a);
        } else {
            // Interpret values as 0.0f < X < 255.0f
            setR(r / 255f);
            setG(g / 255f);
            setB(b / 255f);
            setA(a / 255f);
        }
    }

    /**
     * Make a color using a HTML-style hex code with 6 or 8 places. Do not use a
     * \# before the code.
     * 
     * @param hex
     *            The Hex code to convert to a color, use 8 or 6 characters,
     *            depending on alpha yes/no.
     * @throws IllegalArgumentException
     *             If the hex string has an incorrect length.
     */
    public Color4(String hex) throws IllegalArgumentException {
        super();

        if (hex.length() == 6) {
            String rHex = hex.substring(0, 2);
            String gHex = hex.substring(2, 4);
            String bHex = hex.substring(4, 6);

            int rInt = Integer.parseInt(rHex, 16);
            int gInt = Integer.parseInt(gHex, 16);
            int bInt = Integer.parseInt(bHex, 16);

            float rFloat = rInt / 255f;
            float gFloat = gInt / 255f;
            float bFloat = bInt / 255f;

            setR(rFloat);
            setG(gFloat);
            setB(bFloat);
            setA(1.0f);
        } else if (hex.length() == 8) {
            String rHex = hex.substring(0, 2);
            String gHex = hex.substring(2, 4);
            String bHex = hex.substring(4, 6);
            String aHex = hex.substring(6, 8);

            int rInt = Integer.parseInt(rHex, 16);
            int gInt = Integer.parseInt(gHex, 16);
            int bInt = Integer.parseInt(bHex, 16);
            int aInt = Integer.parseInt(aHex, 16);

            float rFloat = rInt / 255f;
            float gFloat = gInt / 255f;
            float bFloat = bInt / 255f;
            float aFloat = aInt / 255f;

            setR(rFloat);
            setG(gFloat);
            setB(bFloat);
            setA(aFloat);
        } else {
            throw new IllegalArgumentException("wrong number of elements for hex code color");
        }
    }

    /**
     * Getter for Red.
     * 
     * @return the r.
     */
    public float getR() {
        return getX();
    }

    /**
     * Setter for Red.
     * 
     * @param r
     *            the r to set
     */
    public void setR(float r) {
        setX(r);
    }

    /**
     * Getter for Green.
     * 
     * @return the g.
     */
    public float getG() {
        return getY();
    }

    /**
     * Setter for Green.
     * 
     * @param g
     *            the g to set
     */
    public void setG(float g) {
        setY(g);
    }

    /**
     * Getter for Blue.
     * 
     * @return the b.
     */
    public float getB() {
        return getZ();
    }

    /**
     * Setter for Blue.
     * 
     * @param b
     *            the b to set
     */
    public void setB(float b) {
        setZ(b);
    }

    /**
     * Getter for Alpha.
     * 
     * @return the a.
     */
    public float getA() {
        return getW();
    }

    /**
     * Setter for alpha.
     * 
     * @param a
     *            the a to set
     */
    public void setA(float a) {
        setW(a);
    }

}
