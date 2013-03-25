#version 150

in vec2 tCoord;

uniform sampler2D texture_map;

out vec4 fragColor;

void main() {
    fragColor = vec4(texture(texture_map, tCoord).rgb, 1.0);
} 
