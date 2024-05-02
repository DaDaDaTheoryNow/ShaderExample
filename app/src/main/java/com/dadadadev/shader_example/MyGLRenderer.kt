package com.dadadadev.shader_example

import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import com.dadadadev.shader_example.figures.Square
import com.dadadadev.shader_example.figures.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer : GLSurfaceView.Renderer {
    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        mTriangle = Triangle()
        mSquare = Square()

        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }

    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    @Volatile
    var angle: Float = 0f;

    override fun onDrawFrame(unused: GL10) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)

        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 4f, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // rotate matrix
        Matrix.rotateM(vPMatrix, 0, angle, 0f, 1f, 1f);

        //mTriangle.draw(vPMatrix)
        mSquare.draw(vPMatrix)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES31.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }
}

fun loadShader(type: Int, shaderCode: String): Int {
    return GLES31.glCreateShader(type).also { shader ->
        GLES31.glShaderSource(shader, shaderCode)
        GLES31.glCompileShader(shader)
    }
}