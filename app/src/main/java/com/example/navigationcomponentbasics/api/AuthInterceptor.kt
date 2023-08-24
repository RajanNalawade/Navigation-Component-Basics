package com.example.navigationcomponentbasics.api

import android.content.Context
import com.example.navigationcomponentbasics.utils.CheckNetwork
import com.example.navigationcomponentbasics.utils.TokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

//intercepts/adds parameters in to incoming request and passes to next point

class AuthInterceptor @Inject constructor(@ApplicationContext private val context: Context) : Interceptor {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val token = tokenManager.getToken()
        request = if (CheckNetwork.registerNetworkCallback(context)) {
            /*
                        *  If there is Internet, get the cache that was stored 5 seconds ago.
                        *  If the cache is older than 5 seconds, then discard it,
                        *  and indicate an error in fetching the response.
                        *  The 'max-age' attribute is responsible for this behavior.
                        */
            request.newBuilder().header("Cache-Control", "public, max-age=" + 5)
                .addHeader("Authorization", "Bearer $token").build()
        } else {
            /*
                        *  If there is no Internet, get the cache that was stored 7 days ago.
                        *  If the cache is older than 7 days, then discard it,
                        *  and indicate an error in fetching the response.
                        *  The 'max-stale' attribute is responsible for this behavior.
                        *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
                        */
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                .addHeader("Authorization", "Bearer $token").build()
        }

        return chain.proceed(request)
    }

}