package com.dadadadev.shader_example

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import com.dadadadev.shader_example.figures.Square
import com.dadadadev.shader_example.figures.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        mTriangle = Triangle()
        mSquare = Square(context)

        GLES31.glEnable(GLES31.GL_DEPTH_TEST)
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }

    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    @Volatile
    var angleX: Float = 0f

    @Volatile
    var angleY: Float = 0f

    override fun onDrawFrame(unused: GL10) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT or GLES31.GL_DEPTH_BUFFER_BIT)

        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 4f, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // rotate matrix
        Matrix.rotateM(vPMatrix, 0, angleX, 0f, 1f, 0f)
        Matrix.rotateM(vPMatrix, 0, angleY, 1f, 0f, 0f)

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

fun loadTexture(context: Context, resourceId: Int) : Int {
    val textureHandle = IntArray(1)

    GLES31.glGenTextures(1, textureHandle, 0)
    if (textureHandle[0] != 0) {
        val options = BitmapFactory.Options()
        options.inScaled = false

        val bitmap = BitmapFactory.decodeResource(context.resources ,resourceId, options)


        //flip bitmap
        val flipMatrix = android.graphics.Matrix().apply {
            postScale(1.0f, -1.0f)
        }
        val flippedBitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            flipMatrix,
            true
        )

        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureHandle[0])
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_LINEAR)
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR)

        GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, flippedBitmap, 0)
        bitmap.recycle()
        flippedBitmap.recycle()
    } else {
        throw RuntimeException("Error loading texture.")
    }

    return textureHandle[0]
}

fun loadCubeMap(context: Context, resources: IntArray): Int {
    val textureHandle = IntArray(1)
    GLES31.glGenTextures(1, textureHandle, 0)

    if (textureHandle[0] != 0) {
        GLES31.glBindTexture(GLES31.GL_TEXTURE_CUBE_MAP, textureHandle[0])

        GLES31.glTexParameteri(GLES31.GL_TEXTURE_CUBE_MAP, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_LINEAR)
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_CUBE_MAP, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR)
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_CUBE_MAP, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE)
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_CUBE_MAP, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE)

        for (i in 0 until 6) {
            val bitmap = BitmapFactory.decodeResource(context.resources, resources[i])
            GLUtils.texImage2D(GLES31.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, bitmap, 0)
            bitmap.recycle()
        }
    } else {
        throw RuntimeException("Error loading cube map.")
    }

    return textureHandle[0]
}