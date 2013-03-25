package nl.esciencecenter.esight.datastructures;

import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VecF4;

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
        VecF4 ambient = new VecF4((float) Math.random(), (float) Math.random(),
                (float) Math.random(), 1f);
        VecF4 diffuse = new VecF4((float) Math.random(), (float) Math.random(),
                (float) Math.random(), 1f);
        VecF4 specular = new VecF4((float) Math.random(),
                (float) Math.random(), (float) Math.random(), 1f);
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
