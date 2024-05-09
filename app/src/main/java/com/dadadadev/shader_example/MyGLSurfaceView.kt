package com.dadadadev.shader_example

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: MyGLRenderer

    init {
        setEGLContextClientVersion(3)
        renderer = MyGLRenderer(context)
        setRenderer(renderer)

        renderMode = RENDERMODE_CONTINUOUSLY
    }

    private val scaleFactor = 0.4f
    private var previousX: Float = 0f
    private var previousY: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x: Float = event.x
        val y: Float = event.y

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val dx: Float = x - previousX
                val dy: Float = y - previousY

                renderer.angleX += dx * scaleFactor
                renderer.angleY -= -dy * scaleFactor
                renderer.angleY = renderer.angleY.coerceIn(-90.0f, 90.0f)

                requestRender()
            }
        }

        previousX = x
        previousY = y
        return true
    }
}