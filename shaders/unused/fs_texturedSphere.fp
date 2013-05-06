#version 150

uniform sampler2D my_texture;

in vec2 tCoord;

void main() { 		
	vec4 color = vec4(texture(my_texture, tCoord).rgb, 1.0);
    gl_FragColor = vec4(color.rgb, 1.0);
} 

