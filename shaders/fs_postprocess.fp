#version 140

out vec4 fragColor;

uniform sampler2D sphereTexture_0;
uniform sampler2D sphereTexture_1;
uniform sampler2D sphereTexture_2;
uniform sampler2D sphereTexture_3;

uniform int scrWidth;
uniform int scrHeight;
uniform int divs_x;
uniform int divs_y;
uniform int selection;

float getDiv3(float var) {
	float result = 0;
	if (var < (1.0/3.0)) {
		result = var*3.0;
	} else if (var < (2.0/3.0)) {
		result = (var - (1.0/3.0)) * 3.0;
	} else {
		result = (var - (2.0/3.0)) * 3.0;
	}

	return result;
}

float getDiv2(float var) {
	float result = 0;
	if (var < (1.0/2.0)) {
		result = var*2.0;
	} else {
		result = (var - (1.0/2.0)) * 2.0;
	}

	return result;
}

void main() {
	float width = float(scrWidth);
	float height = float(scrHeight);
	
	float x = gl_FragCoord.x/width;
	float y = gl_FragCoord.y/height;
	
	vec4 sphereColor;
	if (divs_x == 2 && divs_y == 2) {
		float conv_x = getDiv2(x);
		float conv_y = getDiv2(y);
		
		vec2 tCoord = vec2(conv_x,conv_y);
		
		if (y < (1.0/2.0)) {
			if (x < (1.0/2.0)) {
				sphereColor = vec4(texture(sphereTexture_0, tCoord).rgb, 1.0);	  			
			} else {
				sphereColor = vec4(texture(sphereTexture_1, tCoord).rgb, 1.0);
			}
		} else {
			if (x < (1.0/2.0)) {
				sphereColor = vec4(texture(sphereTexture_2, tCoord).rgb, 1.0);
			} else {
				sphereColor = vec4(texture(sphereTexture_3, tCoord).rgb, 1.0);
			}
		}
	} else if (divs_x == 2 && divs_y == 1) {
		float conv_x = getDiv2(x);
		
		vec2 tCoord = vec2(conv_x,y);		
		
		if (x < (1.0/2.0)) {
			sphereColor = vec4(texture(sphereTexture_0, tCoord).rgb, 1.0);
		} else {
			sphereColor = vec4(texture(sphereTexture_1, tCoord).rgb, 1.0);
		}
	} else if (divs_x == 1 && divs_y == 2) {
		float conv_y = getDiv2(y);
		
		vec2 tCoord = vec2(x,conv_y);		
		
		if (y < (1.0/2.0)) {
			sphereColor = vec4(texture(sphereTexture_1, tCoord).rgb, 1.0);
		} else {
			sphereColor = vec4(texture(sphereTexture_0, tCoord).rgb, 1.0);
		}
	} else {
		vec2 tCoord = vec2(x,y);
		if (selection == 1) {
			sphereColor = vec4(texture(sphereTexture_0, tCoord).rgb, 1.0);
		} else if (selection == 2) {
			sphereColor = vec4(texture(sphereTexture_1, tCoord).rgb, 1.0);
		} else if (selection == 3) {
			sphereColor = vec4(texture(sphereTexture_2, tCoord).rgb, 1.0);
		} else if (selection == 4) {
			sphereColor = vec4(texture(sphereTexture_3, tCoord).rgb, 1.0);
		}
	}
    
    fragColor = vec4(sphereColor.rgb, 1.0);
}
