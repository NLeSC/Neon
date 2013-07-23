#version 140

in vec2 vTextureCoord;
in vec3 vTransformedNormal;
in vec4 vPosition;

uniform sampler2D colorTex;
uniform sampler2D specularTex;
uniform sampler2D cityLightsTex;

void main(void) {
	vec3 AmbientColor = vec3(0.15,0.15,0.15);
	vec3 PointLightingLocation = vec3(-30.0,4.0,-20.0);
	vec3 PointLightingSpecularColor = vec3(5.0,5.0,5.0);
	vec3 PointLightingDiffuseColor = vec3(0.8,0.8,0.8);

    vec3 lightWeighting;
    
    vec3 lightDirection = normalize(PointLightingLocation - vPosition.xyz);
    vec3 normal = normalize(vTransformedNormal);

    float specularLightWeighting = 0.0;
    float shininess = 32.0;

    shininess = 255.0 -(texture2D(specularTex, vTextureCoord).r *220.0) ;

    if (shininess < 220.0) {
        vec3 eyeDirection = normalize(-vPosition.xyz);
        vec3 reflectionDirection = reflect(-lightDirection, normal);

        specularLightWeighting = pow(max(dot(reflectionDirection, eyeDirection), 0.0), shininess);
    }

    float diffuseLightWeighting = max(dot(normal, lightDirection), 0.0);
    lightWeighting = AmbientColor
        + PointLightingSpecularColor * specularLightWeighting
        + PointLightingDiffuseColor * diffuseLightWeighting;

    vec4 earthColor;
    earthColor = texture2D(colorTex, vTextureCoord);
    
    vec4 finalColor = vec4(earthColor.rgb * lightWeighting, earthColor.a);
        
    vec4 cityLightsColor = texture2D(cityLightsTex, vTextureCoord);
    
    if (length(lightWeighting) < 0.7) {
    	finalColor = finalColor + (0.7 - length(lightWeighting)) *cityLightsColor;
    }
    
    gl_FragColor = finalColor;
}