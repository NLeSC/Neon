#version 140

out vec4 fragColor;

uniform sampler2D axesTexture;

uniform int scrWidth;
uniform int scrHeight;


void main() {
	vec4 mixingColor = vec4(1.0,1.0,1.0,1.0);
	
	float width = float(scrWidth);
	float height = float(scrHeight);
	
	float x = gl_FragCoord.x/width;
	float y = gl_FragCoord.y/height;
	
	vec2 tCoord = vec2(x,y);
	
	vec4 texColor = vec4(texture(axesTexture, tCoord).rgb, 1.0);
    
    fragColor = mix(texColor, mixingColor, 0.1);
}
