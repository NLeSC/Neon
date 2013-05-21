package nl.esciencecenter.esight.experimental;

import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VecF4;

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
 * Experimental class, use at your own risk.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Material {
    public VecF4 ambient, diffuse, specular;

    public Material() {
        this.ambient = new VecF4(0, 0, 0, 0);
        this.diffuse = new VecF4(0, 0, 0, 0);
        this.specular = new VecF4(0, 0, 0, 0);
    }

    public Material(VecF4 ambient, VecF4 diffuse, VecF4 specular) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    public static Material random() {
        VecF4 ambient = new VecF4((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
        VecF4 diffuse = new VecF4((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
        VecF4 specular = new VecF4((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
        return new Material(ambient, diffuse, specular);
    }

    public void setColor(VecF4 newColor) {
        ambient = newColor;
        diffuse = newColor;
        specular = newColor;
    }

    public VecF3 getColor() {
        return new VecF3(diffuse.get(0), diffuse.get(1), diffuse.get(2));
    }

    public float getAlpha() {
        return diffuse.get(3);
    }

    public void setTransparency(float newTransparency) {
        ambient.set(3, newTransparency);
        diffuse.set(3, newTransparency);
        specular.set(3, newTransparency);
    }
}
