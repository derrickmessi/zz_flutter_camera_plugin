package com.example.zz_camera_plugin

import android.opengl.GLES20
import kotlin.random.Random

/**
 * Created by ZhaoZhao,
 * Date on 2021/11/7.
 *
 */
class SimpleRenderer : CameraGLThread.Renderer {
    override fun onCreate() {
    }

    override fun onDraw(): Boolean {
        GLES20.glClearColor(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        return true
    }

    override fun onDispose() {
    }
}