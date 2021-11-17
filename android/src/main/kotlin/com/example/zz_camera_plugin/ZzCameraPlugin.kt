package com.example.zz_camera_plugin

import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.view.TextureRegistry

class ZzCameraPlugin: FlutterPlugin, MethodCallHandler,ActivityAware{

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "zz_camera_plugin")
            channel.setMethodCallHandler(ZzCameraPlugin())
        }
    }

    private lateinit var channel : MethodChannel
    private lateinit var textureRegistry: TextureRegistry
    private lateinit var surfaceEntry: TextureRegistry.SurfaceTextureEntry
    private lateinit var externalGLThread: CameraGLThread
    private lateinit var cameraThread: CameraThread
    private lateinit var context: Context
    private lateinit var activity: Activity

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.flutterEngine.dartExecutor, "zz_camera_plugin")
        channel.setMethodCallHandler(this)
        textureRegistry = flutterPluginBinding.textureRegistry
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        val arguments = call.arguments<Map<String,Int>>()
        when (call.method) {
            "create" -> {
                val width = arguments["width"]
                val height = arguments["height"]

                surfaceEntry = textureRegistry.createSurfaceTexture()
                val surfaceTexture = surfaceEntry.surfaceTexture().apply {
                    setDefaultBufferSize(width!!, height!!)
                }

//                externalGLThread = CameraGLThread(surfaceTexture, SimpleRenderer())
//                externalGLThread.start()
                cameraThread = CameraThread(surfaceTexture,activity,200,200)
                cameraThread.start()
                result.success(surfaceEntry.id())
            }
            "start" ->{
//                cameraThread.start()
                cameraThread.startPreview()
            }
            "dispose" -> {
//                externalGLThread.dispose()
//                surfaceEntry.release()
            }
            else -> result.notImplemented()
        }
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }
}
