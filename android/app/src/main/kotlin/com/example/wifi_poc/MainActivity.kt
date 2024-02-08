package com.example.wifi_poc

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private var connectivityHelper: ConnectivityHelper? = null
    private var wifiAwareHelper: WifiAwareHelper? = null
    var wifiAwareManagerReceiver:WifiAwareManagerReceiver = WifiAwareManagerReceiver()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun configureFlutterEngine(
        flutterEngine: FlutterEngine,
    ) {
        super.configureFlutterEngine(flutterEngine)
        connectivityHelper = ConnectivityHelper(this)
        wifiAwareHelper = WifiAwareHelper(this)


       // connectivityHelper!!.getActiveNetworkInfo()
      //  connectivityHelper!!.registerNetworkCallback()

//        wifiAwareHelper!!.getMacInfo()
        wifiAwareHelper!!.initWifiAwareManager(this)
        wifiAwareManagerReceiver.registerReceiver(this.activity)


        val channel = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "wifi_poc",
        )

        channel.setMethodCallHandler { call, result ->
            if (call.method == "publish_wifi") {
                Log.d("method call","publish_wifi")
                wifiAwareHelper!!.publish()
                result.success(true)
            }else if(call.method == "subscribe_wifi"){
                Log.d("method call","subscribe_wifi")
                wifiAwareHelper!!.subscribe()
                result.success(true)
            }else if(call.method == "connect_wifi"){
                wifiAwareHelper!!.connect()
                result.success(true)
            }
        }
    }

}
