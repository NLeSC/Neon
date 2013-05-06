#version 140

in vec3 EyespaceNormal;

uniform vec4 AmbientMaterial;
uniform vec4 SpecularMaterial;
uniform vec4 DiffuseMaterial;

uniform vec3 LightPos;
uniform float Shininess;

uniform int StarDrawMode;
uniform vec4 HaloColor;

out vec4 fragColor;

void main() {
	if (StarDrawMode == 0) {
	    vec3 N = normalize(EyespaceNormal);
	    vec3 L = normalize(LightPos);
	    vec3 E = vec3(0, 0, 1);
	    vec3 H = normalize(L + E);
	
	    float df = max(0.0, dot(N, L));
	    float sf = max(0.0, dot(N, H));
	    sf = pow(sf, Shininess);
	
	    vec4 color = AmbientMaterial + df * DiffuseMaterial + sf * SpecularMaterial;
	    fragColor = color;
	} else {		
		fragColor = HaloColor;
		
		if (HaloColor.a > 1.0) {
			fragColor.rgb = HaloColor.rgb * HaloColor.a;
		}
	}
}
