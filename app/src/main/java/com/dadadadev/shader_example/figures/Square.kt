package com.dadadadev.shader_example.figures

import android.content.Context
import android.opengl.GLES31
import android.opengl.Matrix
import android.os.SystemClock
import com.dadadadev.shader_example.R
import com.dadadadev.shader_example.common.util.ShaderUtils
import com.dadadadev.shader_example.loadShader
import com.dadadadev.shader_example.loadTexture
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

private val cubeCoords = floatArrayOf(
    -0.5f, -0.5f, -0.5f,
    0.5f, -0.5f, -0.5f,
    0.5f,  0.5f, -0.5f,
    0.5f,  0.5f, -0.5f,
    -0.5f,  0.5f, -0.5f,
    -0.5f, -0.5f, -0.5f,

    -0.5f, -0.5f,  0.5f,
    0.5f, -0.5f,  0.5f,
    0.5f,  0.5f,  0.5f,
    0.5f,  0.5f,  0.5f,
    -0.5f,  0.5f,  0.5f,
    -0.5f, -0.5f,  0.5f,

    -0.5f,  0.5f,  0.5f,
    -0.5f,  0.5f, -0.5f,
    -0.5f, -0.5f, -0.5f,
    -0.5f, -0.5f, -0.5f,
    -0.5f, -0.5f,  0.5f,
    -0.5f,  0.5f,  0.5f,

    0.5f,  0.5f,  0.5f,
    0.5f,  0.5f, -0.5f,
    0.5f, -0.5f, -0.5f,
    0.5f, -0.5f, -0.5f,
    0.5f, -0.5f,  0.5f,
    0.5f,  0.5f,  0.5f,

    -0.5f, -0.5f, -0.5f,
    0.5f, -0.5f, -0.5f,
    0.5f, -0.5f,  0.5f,
    0.5f, -0.5f,  0.5f,
    -0.5f, -0.5f,  0.5f,
    -0.5f, -0.5f, -0.5f,

    -0.5f,  0.4f, -0.5f,
    0.5f,  0.4f, -0.5f,
    0.5f,  0.4f,  0.5f,
    0.5f,  0.4f,  0.5f,
    -0.5f,  0.4f,  0.5f,
    -0.5f,  0.4f, -0.5f,
)

private val texCoords = floatArrayOf(
    0.0f, 0.0f,
    1.0f, 0.0f,
    1.0f, 1.0f,
    1.0f, 1.0f,
    0.0f, 1.0f,
    0.0f, 0.0f,

    0.0f, 0.0f,
    1.0f, 0.0f,
    1.0f, 1.0f,
    1.0f, 1.0f,
    0.0f, 1.0f,
    0.0f, 0.0f,

    1.0f, 1.0f,
    0.0f, 1.0f,
    0.0f, 0.0f,
    0.0f, 0.0f,
    1.0f, 0.0f,
    1.0f, 1.0f,

    1.0f, 1.0f,
    0.0f, 1.0f,
    0.0f, 0.0f,
    0.0f, 0.0f,
    1.0f, 0.0f,
    1.0f, 1.0f,

    0.0f, 1.0f,
    1.0f, 1.0f,
    1.0f, 0.0f,
    1.0f, 0.0f,
    0.0f, 0.0f,
    0.0f, 1.0f,

    0.0f, 1.0f,
    1.0f, 1.0f,
    1.0f, 0.0f,
    1.0f, 0.0f,
    0.0f, 0.0f,
    0.0f, 1.0f
)

class Square(context: Context) {
    private val vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(cubeCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(cubeCoords)
                position(0)
            }
    }

    private val texCoordsBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(texCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(texCoords)
                position(0)
            }
        }

    private var mProgram: Int

    private val vertexShaderCode = ShaderUtils.getShaderSource("shaders/square/shader.vert")
    private val fragmentShaderCode = ShaderUtils.getShaderSource("shaders/square/shader.frag")

    init {
        val vertexShader = loadShader(GLES31.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES31.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram = GLES31.glCreateProgram().also {
            GLES31.glAttachShader(it, vertexShader)
            GLES31.glAttachShader(it, fragmentShader)
            GLES31.glLinkProgram(it)
        }
    }


    private val textureHandle = loadTexture(context, R.drawable.cat)
    private val secondTextureHandle = loadTexture(context, R.drawable.soil)

    fun draw(mvpMatrix: FloatArray) {
        GLES31.glUseProgram(mProgram)
        Matrix.scaleM(mvpMatrix, 0, 0.5f, 0.5f, 0.5f)

        val positionHandle = GLES31.glGetAttribLocation(mProgram, "position").also { positionHandle ->
            GLES31.glEnableVertexAttribArray(positionHandle)
            GLES31.glVertexAttribPointer(
                positionHandle,
                3,
                GLES31.GL_FLOAT,
                false,
                3*4,
                vertexBuffer
            )
        }

        val texCoordHandle = GLES31.glGetAttribLocation(mProgram, "texCoord").also { texCoordHandle ->
            GLES31.glEnableVertexAttribArray(texCoordHandle)
            GLES31.glVertexAttribPointer(
                texCoordHandle,
                2,
                GLES31.GL_FLOAT,
                false,
                2*4,
                texCoordsBuffer
            )
        }

        GLES31.glGetUniformLocation(mProgram, "texture").also { textureUniformHandle ->
            GLES31.glActiveTexture(GLES31.GL_TEXTURE0)
            GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureHandle)
            GLES31.glUniform1i(textureUniformHandle, 0)
        }

        GLES31.glGetUniformLocation(mProgram, "secondTexture").also { secondTextureUniformHandle ->
            GLES31.glActiveTexture(GLES31.GL_TEXTURE1)
            GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, secondTextureHandle)
            GLES31.glUniform1i(secondTextureUniformHandle, 1)
        }

        GLES31.glGetUniformLocation(mProgram, "uMVPMatrix").also { vPMatrixHandle ->
            GLES31.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)
        }

        GLES31.glGetUniformLocation(mProgram, "time").also {iTimeHandle ->
            val currentTime = SystemClock.uptimeMillis() % 1.001
            GLES31.glUniform1f(iTimeHandle, currentTime.toFloat())
        }

        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, 36)

        GLES31.glDisableVertexAttribArray(positionHandle)
        GLES31.glDisableVertexAttribArray(texCoordHandle)
    }
}