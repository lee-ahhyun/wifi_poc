import 'dart:io';

import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';

class MainView extends StatefulWidget {
  MainView({required this.child});

  final Widget child;

  @override
  State<MainView> createState() => _MainViewState();
}

class _MainViewState extends State<MainView> {


  @override
  void initState() {
    super.initState();

    WidgetsBinding.instance.addPostFrameCallback((_) async {
      checkPermission();
    });
  }


  @override
  void dispose() {
    super.dispose();
  }

  Future<void> checkPermission() async {
    List<Permission> permissionList = [
      Permission.location,
      Permission.nearbyWifiDevices,
      Permission.locationAlways,
      Permission.locationWhenInUse,
    ];

    for (Permission item in permissionList) {
      print("permission => $item");
      await item.request();
    }
  }


  Future<bool> runPermission(Permission permission) async {
    PermissionStatus status = await permission.request();
    if (status == PermissionStatus.granted || status == PermissionStatus.denied || status == PermissionStatus.permanentlyDenied) {
      return true;
    }
    return false;
  }

  @override
  Widget build(BuildContext context) {
    return widget.child;
  }
}