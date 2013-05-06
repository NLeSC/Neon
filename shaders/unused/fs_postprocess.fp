#version 140

uniform sampler2D axesTexture;
uniform sampler2D gasTexture;
uniform sampler2D starTexture;
uniform sampler2D starHaloTexture;
uniform sampler2D hudTexture;

uniform float starBrightness;
uniform float starHaloBrightness;
uniform float gasBrightness;
uniform float axesBrightness;
uniform float overallBrightness;
uniform float hudBrightness;

uniform int scrWidth;
uniform int scrHeight;

out vec4 fragColor;

void main() {
	vec2 tCoord   = vec2(gl_FragCoord.x/float(scrWidth), gl_FragCoord.y/float(scrHeight));
		
	vec4 axesColor = vec4(texture(axesTexture, tCoord).rgb, 1.0);
  	vec4 gasColor  = vec4(texture(gasTexture, tCoord).rgb, 1.0);
	
	vec4 starColor = vec4(texture(starTexture, tCoord).rgb, 1.0);
	vec4 starHaloColor = vec4(texture(starHaloTexture, tCoord).rgb, 1.0);
	
	vec4 hudColor = vec4(texture(hudTexture, tCoord).rgb, 1.0);
    
    vec4 color = mix(starColor * starBrightness, starHaloColor * starHaloBrightness, 0.5); 
    	 color = mix(color, gasColor * gasBrightness, 0.1);
    	 color = mix(color, axesColor * axesBrightness, 0.1);
    	 color = mix(color, hudColor * hudBrightness, 0.1);
    
    fragColor = vec4(color.rgb * overallBrightness, 1.0);
}
