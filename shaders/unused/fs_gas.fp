#version 140

uniform vec4 AmbientMaterial;

out vec4 fragColor;

void main() {
	fragColor = AmbientMaterial;
}
