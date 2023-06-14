package com.example.codingchallange.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.example.codingchallange.MyApplication


object ConnectionDetector {
    val isNotConnectedToInternet: Boolean
        get() {
            var connectivity: ConnectivityManager? = null
            try {
                connectivity =
                    MyApplication.instance?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?;
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (connectivity != null) {
                val info = connectivity.allNetworkInfo
                for (networkInfo in info) if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return false
                }
            }
            return true
        }


    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            MyApplication.instance?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}
