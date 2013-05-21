#version 140

out vec4 fragColor;

uniform sampler2D axesTexture;
uniform sampler2D hudTexture;

uniform int scrWidth;
uniform int scrHeight;


void main() {
	vec4 mixingColor = vec4(1.0,1.0,1.0,1.0);
	
	float width = float(scrWidth);
	float height = float(scrHeight);
	
	float x = gl_FragCoord.x/width;
	float y = gl_FragCoord.y/height;
	
	vec2 tCoord = vec2(x,y);
	
	vec4 axesTexColor = vec4(texture(axesTexture, tCoord).rgb, 1.0);
	vec4 hudTexColor = vec4(texture(hudTexture, tCoord).rgb, 1.0);
		
	vec4 finalColor = vec4(normalize(axesTexColor.xyz + hudTexColor.xyz), 1.0);
    
    fragColor = mix(finalColor, mixingColor, 0.1);
}
