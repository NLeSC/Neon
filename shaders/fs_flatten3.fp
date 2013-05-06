#version 150

uniform sampler2D textTex;
uniform sampler2D legendTex;
uniform sampler2D dataTex;
uniform sampler2D atmosphereTex;

uniform int scrWidth;
uniform int scrHeight;

out vec4 fragColor;

void main() {
	float width = float(scrWidth);
	float height = float(scrHeight);
	
	float x = gl_FragCoord.x/width;
	float y = gl_FragCoord.y/height;
	
	vec2 tCoord = vec2(x,y);
	
	vec4 cText 		 = vec4(texture(textTex, 		tCoord).rgb, 1.0);
	vec4 cLegend 	 = vec4(texture(legendTex, 		tCoord).rgb, 1.0);
	vec4 cData 		 = vec4(texture(dataTex, 		tCoord).rgb, 1.0);
	vec4 cAtmosphere = vec4(texture(atmosphereTex, 	tCoord).rgb, 1.0);
	
	vec4 color;
	if (length(cText.rgb) > 0) {
		color = cText;
		
		if (length(cData.rgb) > 0.2) { 
			color = 1.0 - cText;	
		}
	} else if (length(cLegend.rgb) > 0) {
		color = cLegend;
	} else if (length(cData.rgb) > 0) {
		color = cData;
	} else {
		color = cAtmosphere;
	}	
	
	fragColor = color;
}
