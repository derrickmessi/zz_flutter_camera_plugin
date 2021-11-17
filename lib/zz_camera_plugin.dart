
import 'dart:async';

import 'package:flutter/services.dart';

class ZzCameraPlugin {
  static const MethodChannel _channel = MethodChannel('zz_camera_plugin');

  int textureId;

  bool get isInitialized => textureId != null;

  Future<int> initialize(int width,int height) async {
    textureId = await _channel.invokeMethod('create',{
      'width':width,
      'height':height
    });
    return textureId;
  }

  Future<void> dispose() => _channel.invokeMethod('dispose',{'textureId':textureId});

  Future<void> start() => _channel.invokeMethod('start');

}
