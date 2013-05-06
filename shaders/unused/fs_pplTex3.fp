#version 150

in vec3 vertex_pos;
in vec3 vertex_normal;

in vec2 tCoord;

uniform sampler2D world_texture;
//uniform sampler2D world_bump;
uniform sampler2D mapping_texture;
uniform sampler2D my_texture;

uniform float Shininess;

uniform vec4 LightPos;

void main() {
	float map = texture(mapping_texture, tCoord).a;
	vec4 matColor; 
	if (map < 0) {
		matColor = vec4(0,0,0,0);
	} else {
		matColor = vec4(texture(my_texture, vec2(tCoord.x, map)).rgb, 1.0);
	}
	
	vec4 worldColor = vec4(texture(world_texture, tCoord).rgb, 1.0);
	
	vec3 light_direction = normalize(LightPos.xyz - vertex_pos);	
	vec3 eyespace_normal = normalize(-vertex_pos);
	
	//vec3 bump = texture(world_bump, tCoord).xyz;
	//bump = (bump - 0.5) * 2.0;    
	
    //vec3 bumped_normal = normalize(NormalMatrix * bump);
    
	vec3 reflection = normalize(-reflect(light_direction, vertex_normal)); 
      
	vec4 ambient_factor = vec4(0,0,0,0);//matColor;	
		
	vec4 diffuse_factor;
	float dotP = max(dot(vertex_normal, light_direction), 0.0);
	if (matColor.r > 0.000001 || matColor.g > 0.000001 || matColor.b > 0.000001) {
		diffuse_factor = dotP * matColor;
	} else {
		diffuse_factor = dotP * worldColor;
	}
		
	diffuse_factor = clamp(diffuse_factor, 0.0, 1.0);
	
 	vec4 specular_factor = pow(max(dot(reflection, eyespace_normal), 0.0), 0.3 * Shininess) * matColor;
 	specular_factor = vec4(0,0,0,0);//clamp(specular_factor, 0.0, 1.0);

	gl_FragColor = ambient_factor + diffuse_factor + specular_factor;
} 

