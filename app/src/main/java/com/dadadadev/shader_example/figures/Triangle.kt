package com.dadadadev.shader_example.figures

import android.opengl.GLES31
import android.os.SystemClock
import com.dadadadev.shader_example.common.util.ShaderUtils
import com.dadadadev.shader_example.loadShader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

private const val COORDS_PER_VERTEX = 3
private val triangleCoords = floatArrayOf(
    0.0f, 0.62200844f, 0.0f,      // top
    -0.5f, -0.31100425f, 0.0f,    // bottom left
    0.5f, -0.31100425f, 0.0f      // bottom right
)

private const val COLORS_PER_VERTEX = 3
private val vertexColors = floatArrayOf(
    1.0f, 0.0f, 0.0f,
    0.0f, 1.0f, 0.0f,
    0.0f, 0.0f, 1.0f,
)

class Triangle {
    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(triangleCoords)
                position(0)
            }
        }

    private var colorBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertexColors.size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(vertexColors)
                position(0)
            }
        }

    private var mProgram: Int

    private val vertexShaderCode =
        ShaderUtils.getShaderSource("shaders/triangle/shader.vert")

    private val fragmentShaderCode =
        ShaderUtils.getShaderSource("shaders/triangle/shader.frag")

    init {
        val vertexShader: Int = loadShader(GLES31.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES31.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram = GLES31.glCreateProgram().also {
            GLES31.glAttachShader(it, vertexShader)
            GLES31.glAttachShader(it, fragmentShader)
            GLES31.glLinkProgram(it)
        }
    }

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX

    private val vertexStride: Int = COORDS_PER_VERTEX * 4
    private val colorStride: Int = COLORS_PER_VERTEX * 4

    fun draw(mvpMatrix: FloatArray) {
        GLES31.glUseProgram(mProgram)

        val positionHandle = GLES31.glGetAttribLocation(mProgram, "position").also { positionHandle ->
            GLES31.glEnableVertexAttribArray(positionHandle)
            GLES31.glVertexAttribPointer(
                positionHandle,
                COORDS_PER_VERTEX,
                GLES31.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )
        }

        GLES31.glGetUniformLocation(mProgram, "uMVPMatrix").also { vPMatrixHandle ->
            GLES31.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)
        }

        GLES31.glGetUniformLocation(mProgram, "time").also {iTimeHandle ->
            val currentTime = SystemClock.uptimeMillis() % 1.001
            GLES31.glUniform1f(iTimeHandle, currentTime.toFloat())
        }

        val vertexColorHandle = GLES31.glGetAttribLocation(mProgram, "vertexColor").also { vertexColorHandle ->
            GLES31.glEnableVertexAttribArray(vertexColorHandle)
            GLES31.glVertexAttribPointer(
                vertexColorHandle,
                COLORS_PER_VERTEX,
                GLES31.GL_FLOAT,
                false,
                colorStride,
                colorBuffer
            )
        }

        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, vertexCount)

        GLES31.glDisableVertexAttribArray(positionHandle)
        GLES31.glDisableVertexAttribArray(vertexColorHandle)
    }
}