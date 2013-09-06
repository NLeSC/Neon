#version 140

out vec4 fragColor;

uniform sampler2D sceneTex;
uniform sampler2D hudTexture;

uniform int scrWidth;
uniform int scrHeight;


void main() {		
	float width = float(scrWidth);
	float height = float(scrHeight);
	
	float x = gl_FragCoord.x/width;
	float y = gl_FragCoord.y/height;
	
	vec2 tCoord = vec2(x,y);
	
	vec3 hudTexColor = texture(hudTexture, tCoord).rgb;
	vec3 sceneTexColor = texture(sceneTex, tCoord).rgb;
	
	vec4 finalColor = vec4(sceneTexColor, 1.0);
	
	if (length(hudTexColor) > 0.0) { 
		finalColor = vec4(hudTexColor, 1.0);
	} 		
    
    fragColor = finalColor;
}
