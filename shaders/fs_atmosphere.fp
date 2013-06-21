#version 140

in vec3 vertex_normal;

out vec4 fragColor;

void main() {
	vec3 matColor = vec3(0.3, 0.6, 1.0);	
	vec3 eye_direction = vec3(0.0, 0.0, 1.0);
	
	float dotP = dot(vertex_normal, eye_direction);	

	fragColor = vec4(matColor,0.5-(dotP/2.0));
} 