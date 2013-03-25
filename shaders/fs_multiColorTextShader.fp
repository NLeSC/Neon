#version 150

in vec3 vColor;
in float vAlpha;

out vec4 fragColor;

void main (void)
{    
    fragColor = vec4(vColor, vAlpha);
}