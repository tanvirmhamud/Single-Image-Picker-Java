import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  static const platform = MethodChannel('samples.flutter.dev/data');

  String getdata = "Unknown";
  String? imagepath;

  Future<void> _updateProfile() async {
    try {
      var data = await platform.invokeMethod('updateProfile');
      print(data);
      setState(() {
        getdata = data;
        imagepath = data;
      });
    } on PlatformException catch (e) {
      print('Failed : ${e.message}');
    }
  }

  parmissionhandeler() async {
    var status = await Permission.storage.status;
    if (status.isDenied) {
      await Permission.storage.request();
    } else {
      _updateProfile();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Home"),
      ),
      body: Container(
        child: Column(
          children: [
            Container(
              height: 200,
              width: double.infinity,
              child: imagepath == null
                  ? Container()
                  : Image.file(File(imagepath!)),
            ),
            ElevatedButton(
              onPressed: () {
                parmissionhandeler();
              },
              child: Text("Click Me"),
            ),
            Text(getdata),
          ],
        ),
      ),
    );
  }
}
