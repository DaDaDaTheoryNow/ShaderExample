#version 300 es

precision mediump float;

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord;

uniform mat4 uMVPMatrix;

out vec2 vTexCoord;
out float isTop;

void main() {
    if (position.y == 0.4f) {
        isTop = 1.0f;
    }

    gl_Position = uMVPMatrix * vec4(position, 1.0);
    vTexCoord = texCoord;
}
