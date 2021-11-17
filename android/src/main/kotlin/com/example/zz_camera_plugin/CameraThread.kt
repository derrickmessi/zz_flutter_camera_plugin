package com.example.zz_camera_plugin

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Created by ZhaoZhao,
 * Date on 2021/11/7.
 *
 */
class CameraThread(private val surfaceTexture: SurfaceTexture, private val context: Activity,private val width:Int=200,private val height:Int=200) :
    Runnable {

    private lateinit var mCameraDevice: CameraDevice
    private lateinit var mCameraManager: CameraManager
    private lateinit var mMainHandler: Handler
    private lateinit var mChildHandler: Handler


    fun start() {
        Thread(this).start()
    }

    override fun run() {
        mMainHandler = Handler(context.mainLooper)
        val handlerThread =  HandlerThread("Camera2")
        handlerThread.start()
        mChildHandler = Handler(handlerThread.looper)
        initCamera()
//        startPreview()
    }



    private fun initCamera() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 111
            )
        }
        mCameraManager = (context.getSystemService(Context.CAMERA_SERVICE) as CameraManager?)!!

        val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                Log.d("aaaaa---", "onOpened: camera = $camera")
                mCameraDevice = camera
            }

            override fun onDisconnected(camera: CameraDevice) {}
            override fun onError(camera: CameraDevice, error: Int) {}
        }
       var cameraId =  mCameraManager.cameraIdList[2]
        var cameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraId)
        mCameraManager.openCamera(cameraId,stateCallback,mMainHandler)
    }

     fun startPreview() {
       val captureRequest =  mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequest.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
        var outputTarget = Surface(surfaceTexture)
        captureRequest.addTarget(outputTarget)
        val mStateCallback = object :CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                var mCameraCaptureSession = session
                mCameraCaptureSession.setRepeatingRequest(captureRequest.build(),null,mChildHandler)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {

            }
        }
        var mImageReader = ImageReader.newInstance(width,height,ImageFormat.JPEG,1)
        mCameraDevice.createCaptureSession(
            listOf(outputTarget, mImageReader.surface),
            mStateCallback,
            mChildHandler
        )
    }
}