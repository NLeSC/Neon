#version 140

in vec2 vTextureCoord;
in vec3 vTransformedNormal;
in vec4 vPosition;

uniform sampler2D cloudTex;
uniform sampler2D cloudTransparencyTex;

void main(void) {
	vec3 AmbientColor = vec3(0.25,0.25,0.25);
	vec3 PointLightingLocation = vec3(-30.0,4.0,-20.0);
	vec3 PointLightingDiffuseColor = vec3(0.8,0.8,0.8);

    vec3 lightWeighting;
    
    vec3 lightDirection = normalize(PointLightingLocation - vPosition.xyz);
    vec3 normal = normalize(vTransformedNormal);

    float diffuseLightWeighting = max(dot(normal, lightDirection), 0.0);
    lightWeighting = AmbientColor
        + PointLightingDiffuseColor * diffuseLightWeighting;
    
    float cloudAlpha = 1.0-texture2D(cloudTransparencyTex, vTextureCoord).r;    
    vec4 cloudColor = texture2D(cloudTex, vTextureCoord);
    
    vec4 atmoColor = vec4((cloudColor * vec4(lightWeighting, 1.0)).rgb,  cloudAlpha);
    
    gl_FragColor = atmoColor;
}