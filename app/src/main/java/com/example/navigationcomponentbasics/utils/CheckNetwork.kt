package com.example.navigationcomponentbasics.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest


class CheckNetwork {

    companion object {
        fun registerNetworkCallback(context: Context): Boolean {
            var isNetworkConnected = false
            try {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val builder = NetworkRequest.Builder()
                connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        isNetworkConnected = true // Global Static Variable
                    }

                    override fun onLost(network: Network) {
                        isNetworkConnected = false // Global Static Variable
                    }
                }
                )
            } catch (e: Exception) {
                isNetworkConnected = false
            }
            return isNetworkConnected
        }
    }
}