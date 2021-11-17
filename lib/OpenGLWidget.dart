import 'package:flutter/material.dart';
import 'package:zz_camera_plugin/zz_camera_plugin.dart';

// ignore: must_be_immutable
class OpenGLWidget extends StatefulWidget {
  OpenGLWidget({Key key, this.width = 200, this.height = 200})
      : super(key: key);

  var width;
  var height;

  @override
  State<StatefulWidget> createState() {
    return _TextureState();
  }
}

class _TextureState extends State<OpenGLWidget> {
  final _externalPlugin = ZzCameraPlugin();

  @override
  void initState() {
    super.initState();
    initPlugin();
  }

  void initPlugin() async {
    await _externalPlugin.initialize(widget.width, widget.height);
    setState(() {});
  }

  @override
  void dispose() {
    super.dispose();
    _externalPlugin.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [TextButton(onPressed: (){
        _externalPlugin.start();
      },child: Text("开始")),
        Container(
            width: widget.width.toDouble(),
            height: widget.height.toDouble(),
            child: _externalPlugin.isInitialized
                ? Texture(textureId: _externalPlugin.textureId)
                : Container(color: Colors.blue)),
      ],
    );
  }
}
