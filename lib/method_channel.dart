import 'package:flutter/services.dart';

const MethodChannel channel =
MethodChannel("wifi_poc");

class Const {
  static const publishWifi = "publish_wifi"; // 서비스 게시
  static const subscribeWifi = "subscribe_wifi";  // 서비스 구독
  static const connectWifi = "connect_wifi";   // 서비스 연결
}