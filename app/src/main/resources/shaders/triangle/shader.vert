#version 300 es

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 vertexColor;

uniform mat4 uMVPMatrix;
out vec3 vColor;

void main() {
    gl_Position = uMVPMatrix * vec4(position, 1.0);
    vColor = vertexColor;
}