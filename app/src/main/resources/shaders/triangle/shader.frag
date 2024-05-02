#version 300 es

precision mediump float;

uniform float time;

in vec3 vColor;
out vec4 fragColor;

void main() {
    //vec2 uv = fragCoord/iResolution.xy;
    //vec3 col = 0.5 + 0.5*cos(iTime+uv.xyx+vec3(0,2,4));

    vec3 shiftedColor = vColor.rgb + vec3(time, time * 2.0, time * 3.0);
    fragColor = vec4(mod(shiftedColor, 1.0), 1.0);
}