package com.example.wifi_poc

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.aware.AttachCallback
import android.net.wifi.aware.DiscoverySession
import android.net.wifi.aware.DiscoverySessionCallback
import android.net.wifi.aware.PeerHandle
import android.net.wifi.aware.PublishConfig
import android.net.wifi.aware.PublishDiscoverySession
import android.net.wifi.aware.SubscribeConfig
import android.net.wifi.aware.SubscribeDiscoverySession
import android.net.wifi.aware.WifiAwareManager
import android.net.wifi.aware.WifiAwareNetworkInfo
import android.net.wifi.aware.WifiAwareNetworkSpecifier
import android.net.wifi.aware.WifiAwareSession
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.net.ServerSocket

@SuppressLint("NewApi")
class WifiAwareHelper(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.O)
    private var wifiAwareManager =
        context.getSystemService(Context.WIFI_AWARE_SERVICE) as WifiAwareManager?
    private val wifiAwareManagerReceiver: WifiAwareManagerReceiver = WifiAwareManagerReceiver()
    private val filter = IntentFilter(WifiAwareManager.ACTION_WIFI_AWARE_STATE_CHANGED)
    var wifiAwareSession: WifiAwareSession? = null
    private var mDiscoverySession: DiscoverySession? = null
    private var mPeerHandle: PeerHandle? = null
    private var handler: Handler = Handler()
    private var discoverySessionCallbackManager:DiscoverySessionCallbackManager = DiscoverySessionCallbackManager()
    private var attachCallbackManager : AttachCallbackManager = AttachCallbackManager()
    // 초기 실행
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun initWifiAwareManager(context: Context) {
        registerBroadcastReceiver()
        if (wifiAwareManager != null) {
            Log.d("wifiAwareManager ", "is not null")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (wifiAwareManager!!.isAvailable) {

                    val isGrantedLocationPermission = ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED

                    val isGrantedNearbyWifiDevice = ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.NEARBY_WIFI_DEVICES
                    ) != PackageManager.PERMISSION_GRANTED

                    if (isGrantedLocationPermission || isGrantedNearbyWifiDevice) {
                        Log.d("init", "error")
                    } else {
                        Log.d("init", "성공")
                    }
                    val resource = wifiAwareManager!!.availableAwareResources

                    Log.d("resource", resource.toString())

                    wifiAwareManager!!.attach(attachCallbackManager, handler)


                }
            }
        }
    }

    // 서비스 게시
    @SuppressLint("MissingPermission")
    fun publish() {
        val config: PublishConfig = PublishConfig.Builder()
            .setServiceName("aware_service")
            .build()
        wifiAwareSession?.publish(config,discoverySessionCallbackManager, handler)
    }

    // 서비스 구독
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    fun subscribe() {
        val config: SubscribeConfig = SubscribeConfig.Builder()
            .setServiceName("aware_service")
            .build()

        try {
            wifiAwareSession?.subscribe(config,discoverySessionCallbackManager, handler)

            Log.d("sconfig",config.toString())
            Log.d("wifiAwareSession",wifiAwareSession.toString())
        }catch (e:IOException){
            Log.d("subscribe","e"+e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun connect() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ss = ServerSocket(0)
        val port = ss.localPort

        try {
            Log.d("port",port.toString())
            val networkSpecifier = WifiAwareNetworkSpecifier.Builder(mDiscoverySession!!, mPeerHandle!!)
                .setPskPassphrase("somepassword")
                .setPort(port)
                .build()
            var mNetworkCapabilities:NetworkCapabilities? = null
            var mNetwork: Network? =null

            val myNetworkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI_AWARE)
                .setNetworkSpecifier(networkSpecifier)
                .build()
        }catch (e:IOException){
            Log.d("connect ===> ",e.toString())
        }


//        val callback = object : ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//
//            }
//
//            override fun onCapabilitiesChanged(
//                network: Network,
//                networkCapabilities: NetworkCapabilities
//            ) {
//                mNetworkCapabilities = networkCapabilities
//                mNetwork = network
//            }
//
//            override fun onLost(network: Network) {
//
//            }
//        }
//
//        connectivityManager.requestNetwork(myNetworkRequest,callback)
//
//        val peerAwareInfo = mNetworkCapabilities?.transportInfo as WifiAwareNetworkInfo
//        val peerIpv6 = peerAwareInfo.peerIpv6Addr
//        val peerPort = peerAwareInfo.port
//
//        val socket = mNetwork?.getSocketFactory()?.createSocket(peerIpv6, peerPort)

     //   Log.d("socket ===> ",socket.toString())
    }

    inner class AttachCallbackManager : AttachCallback() {

        init {
            Log.d("AttachCallbackManager", "start")
        }

        override fun onAwareSessionTerminated() {
            super.onAwareSessionTerminated()
        }

        override fun onAttached(session: WifiAwareSession?) {
            super.onAttached(session)
            wifiAwareSession = session

            Log.d("AttachCallbackManager", session.toString())
        }

        override fun onAttachFailed() {
            super.onAttachFailed()
        }

    }

    inner class DiscoverySessionCallbackManager : DiscoverySessionCallback() {

        override fun onPublishStarted(session: PublishDiscoverySession) {
            super.onPublishStarted(session)

            mDiscoverySession = session
          //  mDiscoverySession.sendMessage()
            Log.d("onPublishStarted", session.toString())
        }

        override fun onMessageReceived(peerHandle: PeerHandle?, message: ByteArray?) {
            super.onMessageReceived(peerHandle, message)
            mPeerHandle = peerHandle
            Log.d("onMessageReceived", peerHandle.toString())
        }

        override fun onSubscribeStarted(session: SubscribeDiscoverySession) {
            mDiscoverySession = session
          //  session.sendMessage()
            Log.d("onSubscribeStarted", "session $session")
        }

        override fun onMessageSendFailed(messageId: Int) {
            super.onMessageSendFailed(messageId)
        }

        override fun onMessageSendSucceeded(messageId: Int) {
            super.onMessageSendSucceeded(messageId)
        }

        override fun onServiceLost(peerHandle: PeerHandle, reason: Int) {
            super.onServiceLost(peerHandle, reason)
            Log.d("onServiceLost", "reason $reason")
        }
        override fun onServiceDiscovered(
            peerHandle: PeerHandle,
            serviceSpecificInfo: ByteArray,
            matchFilter: List<ByteArray>
        ) {
            mPeerHandle = peerHandle
            var test = "test"
            test.toByteArray()
            mDiscoverySession?.sendMessage(mPeerHandle!!,0,test.toByteArray())
            Log.d("onServiceDiscovered", "peerHandle $peerHandle")
        }
    }

    private fun permissionCheck(): Boolean {
        val isGrantedLocationPermission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

        val isGrantedNearbyWifiDevice = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.NEARBY_WIFI_DEVICES
        ) != PackageManager.PERMISSION_GRANTED
        return isGrantedLocationPermission || isGrantedNearbyWifiDevice
    }

    private fun registerBroadcastReceiver() {
        context.registerReceiver(wifiAwareManagerReceiver, filter)
    }

    fun getMacInfo(): String {
        val manager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = manager.connectionInfo
        val ipAddress = info.ipAddress
        val bssid = info.bssid
        val ssid = info.ssid
        val macAddress = info.macAddress.toUpperCase()


        Log.d("manager", manager.toString())
        Log.d("info", info.toString())
        Log.d("ipAddress", ipAddress.toString())
        Log.d("bssid", bssid)
        Log.d("ssid", ssid)
        Log.d("macAddress", macAddress)
//        Log.d("apMldMacAddress", info.apMldMacAddress.toString())
        Log.d("isWifiEnabled", manager.isWifiEnabled.toString())

        return ""
    }

}