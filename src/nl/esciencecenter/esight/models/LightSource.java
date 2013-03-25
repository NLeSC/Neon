package nl.esciencecenter.esight.models;

import nl.esciencecenter.esight.math.Color4;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.shaders.ShaderProgram;

public class LightSource {
    private Color4 color;
    private VecF3 position;

    public LightSource(Color4 color, VecF3 position) {
        this.color = color;
        this.position = position;
    }

    public void use(ShaderProgram p) {
        p.setUniformVector("LightColor", color);
        p.setUniformVector("LightPos", position);
    }
}
