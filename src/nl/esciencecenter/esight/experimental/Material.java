package nl.esciencecenter.esight.experimental;

import nl.esciencecenter.esight.math.Color4;

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
 * Experimental class, use at your own risk.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Material {
    public Color4 ambient, diffuse, specular;

    public Material() {
        this.ambient = new Color4(0, 0, 0, 0);
        this.diffuse = new Color4(0, 0, 0, 0);
        this.specular = new Color4(0, 0, 0, 0);
    }

    public Material(Color4 ambient, Color4 diffuse, Color4 specular) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    public static Material random() {
        Color4 ambient = new Color4((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
        Color4 diffuse = new Color4((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
        Color4 specular = new Color4((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
        return new Material(ambient, diffuse, specular);
    }

    public void setColor(Color4 newColor) {
        ambient = newColor;
        diffuse = newColor;
        specular = newColor;
    }

    public Color4 getColor() {
        return new Color4(diffuse.getR(), diffuse.getG(), diffuse.getB(), diffuse.getA());
    }

    public float getAlpha() {
        return diffuse.getA();
    }

    public void setTransparency(float newTransparency) {
        ambient.setA(newTransparency);
        diffuse.setA(newTransparency);
        specular.setA(newTransparency);
    }
}
