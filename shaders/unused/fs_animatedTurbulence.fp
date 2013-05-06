#version 140

in vec3  MCposition;

uniform sampler3D Noise;  
uniform vec4 AmbientMaterial;     
uniform float Offset;

uniform int StarDrawMode;
uniform vec4 HaloColor;

out vec4 fragColor;

const float brightnessMultiplyer = 6.0;

void main() {	
	vec3 Color1 = vec3(0,0,0);
	vec3 Color2 = AmbientMaterial.rgb;

	if (StarDrawMode == 0) { 
	    vec4 noisevecX 		= texture(Noise,       (MCposition  + .33 * vec3(Offset*3,        0,      0)));
	    vec4 noisevecY 		= texture(Noise, .50 * (MCposition  + .50 * vec3(0       , Offset*2,      0)));
	    vec4 noisevecZ 		= texture(Noise, .33 * (MCposition  +       vec3(0       ,        0, Offset)));
	
	    float intensity = ((noisevecX[0] +
	                        noisevecX[1] +
	                        noisevecX[2] +
	                        noisevecX[3]) * 0.33) +
	                      ((noisevecY[0] +
	                        noisevecY[1] +
	                        noisevecY[2] +
	                        noisevecY[3]) * 0.33) +
	                      ((noisevecZ[0] +
	                        noisevecZ[1] +
	                        noisevecZ[2] +
	                        noisevecZ[3]) * 0.33);
	
		vec3 color   	= mix(Color1, Color2, intensity);
	    
		fragColor = vec4(color * brightnessMultiplyer, 1.0);
	} else {
		fragColor = HaloColor;
	}
}
