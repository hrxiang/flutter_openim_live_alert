import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class FlutterOpenimLiveAlert {
  static const MethodChannel _channel =
      MethodChannel('flutter_openim_live_alert');

  static Future? moveTaskToFront({
    String activityName = 'io.openim.app.enterprisechat.MainActivity',
    String? packageName,
  }) async {
    if (Platform.isAndroid) {
      return _channel.invokeMethod('moveTaskToFront', {
        'activityName': activityName,
        'packageName': packageName,
      });
    }
    return null;
  }

  static Future? startActivity({required String activityName}) async {
    if (Platform.isAndroid) {
      return _channel.invokeMethod('startActivity', {
        'activityName': activityName,
      });
    }
    return null;
  }

  static Future? showLiveAlert({String? title}) async {
    if (Platform.isAndroid) {
      return _channel.invokeMethod('showAlert', {'title': title});
    }
    return null;
  }

  static Future? closeLiveAlert() async {
    if (Platform.isAndroid) {
      return _channel.invokeMethod('closeAlert');
    }
    return null;
  }

  static Future? closeAlertAndMoveTaskToFront({
    String activityName = 'io.openim.app.enterprisechat.MainActivity',
    String? packageName,
  }) async {
    if (Platform.isAndroid) {
      return _channel.invokeMethod('closeAlertAndMoveTaskToFront', {
        'activityName': activityName,
        'packageName': packageName,
      });
    }
    return null;
  }

  static buttonEvent({Function()? onReject, Function()? onAccept}) {
    if (Platform.isAndroid) {
      _channel.setMethodCallHandler((call) async {
        switch (call.method) {
          case 'reject':
            onReject?.call();
            closeLiveAlert();
            break;
          case 'accept':
            onAccept?.call();
            closeAlertAndMoveTaskToFront();
            break;
        }
        return null;
      });
    }
  }
}
