package com.example.zz_camera_plugin

import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.GLUtils
import android.util.Log
import java.lang.RuntimeException
import javax.microedition.khronos.egl.*

/**
 * Created by ZhaoZhao,
 * Date on 2021/11/7.
 */
class CameraGLThread(private val surfaceTexture: SurfaceTexture, private val renderer: Renderer) :
    Runnable {

    private var running = true;

    private var egl: EGL10? = null
    private var eglDisplay: EGLDisplay? = null
    private var eglContext: EGLContext? = null
    private var eglSurface: EGLSurface? = null
    private val config: IntArray = intArrayOf(
        EGL10.EGL_RENDERABLE_TYPE, 4,
        EGL10.EGL_RED_SIZE, 8,
        EGL10.EGL_GREEN_SIZE, 8,
        EGL10.EGL_BLUE_SIZE, 8,
        EGL10.EGL_ALPHA_SIZE, 8,
        EGL10.EGL_DEPTH_SIZE, 16,
        EGL10.EGL_STENCIL_SIZE, 0,
        EGL10.EGL_SAMPLE_BUFFERS, 1,
        EGL10.EGL_SAMPLES, 4,
        EGL10.EGL_NONE
    )

    fun start() {
        Thread(this).start()
    }

    fun dispose() {
        running = false
    }

    override fun run() {
        initEGL()
        renderer.onCreate()

        while (running) {
            val loopStart = System.currentTimeMillis()
            if (renderer.onDraw()) {
                if (!egl!!.eglSwapBuffers(eglDisplay, eglSurface)) {
                    if (!egl!!.eglSwapBuffers(eglDisplay, eglSurface)) {
                        Log.d("aaaaa--", egl!!.eglGetError().toString())
                    }
                }
            }
            val waitDelta = 500 - (System.currentTimeMillis() - loopStart)
            if (waitDelta > 0) {
                try {
                    Thread.sleep(waitDelta)
                } catch (e: InterruptedException) {
                    Log.e("aaaaaa--", e.toString())
                }
            }
        }
        renderer.onDispose()
        destroyEGL()
    }

    private fun initEGL() {
        egl = EGLContext.getEGL() as EGL10
        eglDisplay = egl!!.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
        if (eglDisplay === EGL10.EGL_NO_DISPLAY) {
            throw RuntimeException("eglGetDisplay failed")
        }
        val version = IntArray(2)
        if (!egl!!.eglInitialize(eglDisplay, version)) {
            throw RuntimeException("eglInitialize failed")
        }
        val eglConfig = chooseEglConfig()
        eglContext = createContext(egl!!, eglDisplay, eglConfig)
        eglSurface = egl!!.eglCreateWindowSurface(eglDisplay, eglConfig, surfaceTexture, null)
        if (eglSurface == null || eglSurface === EGL10.EGL_NO_SURFACE) {
            throw RuntimeException("GL Error: " + GLUtils.getEGLErrorString(egl!!.eglGetError()))
        }
        if (!egl!!.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
            throw RuntimeException("GL make current error: " + GLUtils.getEGLErrorString(egl!!.eglGetError()))
        }
    }

    private fun createContext(
        egl: EGL10,
        eglDisplay: EGLDisplay?,
        eglConfig: EGLConfig?
    ): EGLContext? {
        val attribList = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
        return egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attribList)
    }

    private fun destroyEGL() {
        egl!!.eglMakeCurrent(
            eglDisplay,
            EGL10.EGL_NO_SURFACE,
            EGL10.EGL_NO_SURFACE,
            EGL10.EGL_NO_CONTEXT
        )
        egl!!.eglDestroySurface(eglDisplay, eglSurface)
        egl!!.eglDestroyContext(eglDisplay, eglContext)
        egl!!.eglTerminate(eglDisplay)
    }

    private fun chooseEglConfig(): EGLConfig? {
        val configsCount = IntArray(1)
        val configs = arrayOfNulls<EGLConfig>(1)
        val configSpec = config
        require(egl!!.eglChooseConfig(eglDisplay, configSpec, configs, 1, configsCount)) {
            "Failed to choose config: " + GLUtils.getEGLErrorString(egl!!.eglGetError())
        }
        return if (configsCount[0] > 0) {
            configs[0]
        } else null
    }

    interface Renderer {
        /**
         * onCreate 回调
         */
        fun onCreate()

        /**
         * onDraw
         * @return 是否上屏
         */
        fun onDraw(): Boolean

        /**
         * dispose 回调
         */
        fun onDispose()
    }
}