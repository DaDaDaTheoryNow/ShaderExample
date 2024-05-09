#version 300 es

precision mediump float;

uniform float time;
uniform sampler2D texture;
uniform sampler2D secondTexture;

in float isTop;
in vec2 vTexCoord;

out vec4 fragColor;

void main() {
    //vec3 shiftedColor = vColor.rgb + vec3(time, time * 2.0, time * 3.0);
    //fragColor = vec4(1.0, 0.5, 0.0, 1.0);

    if (isTop == 1.0f) {
        fragColor = texture(secondTexture, vTexCoord);
    } else {
        fragColor = texture(texture, vTexCoord);
    }
}