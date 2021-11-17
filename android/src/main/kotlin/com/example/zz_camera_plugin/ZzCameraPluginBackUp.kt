//package com.example.zz_camera_plugin
//
//import android.Manifest
//import android.content.Context.CAMERA_SERVICE
//import android.content.pm.PackageManager
//import android.graphics.ImageFormat
//import android.graphics.SurfaceTexture
//import android.hardware.camera2.*
//import android.media.ImageReader
//import android.media.MediaRecorder
//import android.os.Build
//import android.os.Handler
//import android.os.HandlerThread
//import android.util.Log
//import android.util.Size
//import android.view.SurfaceHolder
//import android.view.SurfaceView
////import androidx.core.app.ActivityCompat
////import androidx.core.content.ContextCompat
////import androidx.core.content.ContextCompat.getSystemService
//
//import io.flutter.embedding.engine.plugins.FlutterPlugin
//import io.flutter.plugin.common.MethodCall
//import io.flutter.plugin.common.MethodChannel
//import io.flutter.plugin.common.MethodChannel.MethodCallHandler
//import io.flutter.plugin.common.MethodChannel.Result
//import io.flutter.plugin.common.PluginRegistry
//import io.flutter.view.TextureRegistry
//import java.lang.Exception
//import java.util.*
//
///** ZzCameraPlugin */
class ZzCameraPluginBackUp  {}
//class ZzCameraPluginBackUp : FlutterPlugin, MethodCallHandler {
//    /// The MethodChannel that will the communication between Flutter and native Android
//    ///
//    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
//    /// when the Flutter Engine is detached from the Activity
//
//
//    companion object {
//        private lateinit var myRegistrar: PluginRegistry.Registrar
//
//        fun registerWith(registrar: PluginRegistry.Registrar) {
//            val channel = MethodChannel(registrar.messenger(), "zz_camera_plugin")
//            channel.setMethodCallHandler(ZzCameraPluginBackUp())
//            myRegistrar = registrar;
//        }
//    }
//
////    private lateinit var channel: MethodChannel
////    private lateinit var textureRegistry: TextureRegistry
////    private lateinit var surfaceEntry: TextureRegistry.SurfaceTextureEntry
////    private lateinit var externalGLThread: CameraGLThread
////
////    private var mCameraManager: CameraManager? = null
////    private var mCameraDevice: CameraDevice? = null
////    private var mCameraCaptureSession: CameraCaptureSession? = null
////    private var mChildHandler: Handler? = null
////    private var mMainHandler: Handler? = null
////    private var mSurfaceView: SurfaceView? = null
////    private var mSurfaceHolder: SurfaceHolder? = null
////    private var mSfWidth = 1080
////    private var mSfHeight = 1920
////    private var mImageReader: ImageReader? = null
////    private var mStateCallback: CameraCaptureSession.StateCallback? = null
////    private var mVideoSize: Size? = null
////    private var mPreviewSize: Size? = null
////    private var mSensorOrientation: Int? = null
//
//    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
//        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "zz_camera_plugin")
//        channel.setMethodCallHandler(this)
//        textureRegistry = flutterPluginBinding.textureRegistry
//    }
//
//    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
//        val arguments = call.arguments<Map<String, Int>>()
//        when (call.method) {
//            "create" -> {
////                val width = arguments["width"]
////                val height = arguments["height"]
////
////                surfaceEntry = textureRegistry.createSurfaceTexture()
////                val surfaceTexture = surfaceEntry.surfaceTexture().apply {
////                    setDefaultBufferSize(width!!, height!!)
//                }
//
////                externalGLThread = CameraGLThread(surfaceTexture, SimpleRenderer())
////                externalGLThread.start()
//
////                val handlerThread = HandlerThread("Camera2")
////                handlerThread.start()
////                mMainHandler = Handler(handlerThread.looper)
////                mChildHandler = Handler(handlerThread.looper)
////                initCamera()
////                starPreview()
////                result.success(surfaceEntry.id())
////            }
//            "dispose" -> {
////                externalGLThread.dispose()
////                surfaceEntry.release()
//            }
//            else -> result.notImplemented()
//        }
//    }
//
//    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
//        channel.setMethodCallHandler(null)
//    }
//
////    private fun initCamera() {
////        if (ContextCompat.checkSelfPermission(
////                myRegistrar.activity(),
////                Manifest.permission.CAMERA
////            ) != PackageManager.PERMISSION_GRANTED
////        ) {
////            ActivityCompat.requestPermissions(
////                myRegistrar.activity(),
////                arrayOf(
////                    Manifest.permission.CAMERA,
////                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
////                    Manifest.permission.READ_EXTERNAL_STORAGE
////                ),
////                111
////            )
////        }
////            mCameraManager =  myRegistrar.activity().getSystemService(CAMERA_SERVICE) as CameraManager
////            val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
////                override fun onOpened(camera: CameraDevice) {
////                    mCameraDevice = camera
////                }
////
////                override fun onDisconnected(camera: CameraDevice) {}
////                override fun onError(camera: CameraDevice, error: Int) {}
////            }
////                    try {
////            val cameraId = mCameraManager!!.cameraIdList[2]
////            Log.d("aaaaa---", "initCamera: idSize = " + mCameraManager!!.cameraIdList.size)
////            val cameraCharacteristics = mCameraManager!!.getCameraCharacteristics(cameraId)
////            val configurationMap =
////                cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
////            mSensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
////            val outputSizes = configurationMap!!.getOutputSizes(
////                MediaRecorder::class.java
////            )
////            val outputSizes1 = configurationMap.getOutputSizes(
////                SurfaceTexture::class.java
////            )
////            mVideoSize = outputSizes[5]
////            mPreviewSize = outputSizes1[5]
////            mCameraManager!!.openCamera(cameraId, stateCallback, mMainHandler)
////        } catch (e: CameraAccessException) {
////            Log.e("aaaaa---", "initCamera: e$e")
////            e.printStackTrace()
////        }
////
////
////
////    } //    private void chooseVideoSize()
//
////    private fun starPreview() {
////        try {
////            val captureRequest = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
////            captureRequest.set(
////                CaptureRequest.CONTROL_AF_MODE,
////                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
////            )
////            captureRequest.addTarget(mSurfaceHolder!!.surface) //TODO:
////            mStateCallback = object : CameraCaptureSession.StateCallback() {
////                override fun onConfigured(session: CameraCaptureSession) {
////                    if (mCameraDevice == null) return
////                    //摄像头准备好,开始预览流
////                    mCameraCaptureSession = session
////                    try {
////                        mCameraCaptureSession!!.setRepeatingRequest(
////                            captureRequest.build(),
////                            null,
////                            mChildHandler
////                        )
////                    } catch (e: CameraAccessException) {
////                        e.printStackTrace()
////                    }
////                }
////
////                override fun onConfigureFailed(session: CameraCaptureSession) {
////                    Log.d("aaaaa---", "onConfigureFailed: !!!!!!!!!!!")
////                }
////            }
////            Log.d(
////                "aaaaa---",
////                "mSurfaceView.getWidth() = " + mSurfaceView!!.width + "    height = " + mSurfaceView!!.height
////            )
////            mImageReader = ImageReader.newInstance(
////                mSurfaceView!!.width,
////                mSurfaceView!!.height,
////                ImageFormat.JPEG,
////                1
////            )
////            mCameraDevice!!.createCaptureSession(
////                Arrays.asList(
////                    mSurfaceHolder!!.surface, mImageReader!!.surface
////                ), mStateCallback, mChildHandler
////            )
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
////    }
//}
