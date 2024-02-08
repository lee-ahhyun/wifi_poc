import 'dart:io';

import 'package:flutter/material.dart';
import 'package:wifi_poc/main_view.dart';
import 'package:wifi_poc/method_channel.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: MainView(child: const MyHomePage()),
    );
  }
}

class MyHomePage extends StatelessWidget {
  const MyHomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text(''),
        ),
        body: SingleChildScrollView(
          child: Column(
            children: [
              TextButton(
                  onPressed: () {
                  channel.invokeMethod(Const.publishWifi);

                  },
                  child: const Text("서비스 게시")),
              TextButton(
                  onPressed: () {
                  channel.invokeMethod(Const.subscribeWifi);
                  },
                  child: const Text("서비스 구독")),
              TextButton(
                  onPressed: () {channel.invokeMethod(Const.connectWifi);
                  },
                  child: const Text("서비스 연결")),
              const SizedBox(
                height: 5,
              ),
              const SizedBox(
                height: 5,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
