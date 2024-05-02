package com.dadadadev.shader_example

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: MyGLRenderer

    init {
        setEGLContextClientVersion(3)
        renderer = MyGLRenderer()
        setRenderer(renderer)

        renderMode = RENDERMODE_CONTINUOUSLY
    }

    private val scaleFactor = 180.0f / 320f
    private var previousX: Float = 0f
    private var previousY: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x: Float = event.x
        val y: Float = event.y

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                var dx: Float = x - previousX
                var dy: Float = y - previousY

                if (y > height / 2) {
                    dx *= -1
                }

                if (x < width / 2) {
                    dy *= -1
                }

                renderer.angle += (dx + dy) * scaleFactor
                requestRender()
            }
        }


        previousX = x
        previousY = y
        return true
    }
}