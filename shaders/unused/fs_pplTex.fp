#version 140

in vec3 EyespaceNormal;
in vec2 tCoord;

uniform sampler2D my_texture;
uniform vec3 LightPos;
uniform float Shininess;

out vec4 fragColor;

void main() {
	vec4 texColor = vec4(texture(my_texture, tCoord).rgb, 1.0);
	texColor = vec4(.5,.5,.5,1);

    vec3 N = normalize(EyespaceNormal);
    vec3 L = normalize(LightPos);
    vec3 E = vec3(0, 0, 1);
    vec3 H = normalize(L + E);

    float df = max(0.0, dot(N, L));
    float sf = max(0.0, dot(N, H));
    sf = pow(sf, Shininess);

    vec4 color = texColor + df * texColor + sf * texColor;
    fragColor = color;
}
