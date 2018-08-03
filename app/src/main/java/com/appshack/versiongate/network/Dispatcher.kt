package com.appshack.versiongate.network

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request


/**
 * Created by joelbrostrom on 2018-07-27
 * Developed by App Shack
 */

class Dispatcher {
    private val client: OkHttpClient = OkHttpClient()

    fun dispatch(apiRequest: APIRequest, responseHandler: Callback) {

        val path = apiRequest.path

        var requestBuilder = Request.Builder()
                .url(path)
                //.addHeader("Authorization", "")

        apiRequest.body?.let {
            requestBuilder = when (apiRequest.method) {

                HTTPMethod.GET -> requestBuilder.get()
                HTTPMethod.POST -> requestBuilder.post(it)
                HTTPMethod.DELETE -> requestBuilder.delete(it)
                HTTPMethod.PATCH -> requestBuilder.patch(it)
                HTTPMethod.PUT -> requestBuilder.put(it)
            }
        }
        client.newCall(requestBuilder.build()).enqueue(responseHandler)
    }
}