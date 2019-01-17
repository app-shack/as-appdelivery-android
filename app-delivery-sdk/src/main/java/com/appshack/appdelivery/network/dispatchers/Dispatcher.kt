package com.appshack.appdelivery.network.dispatchers

import android.util.Log
import com.appshack.appdelivery.network.api.requests.APIRequest
import com.appshack.appdelivery.network.api.requests.HTTPMethod
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by joelbrostrom on 2018-07-27
 * Developed by App Shack
 *
 * Purpose: Send API-requests and pass response to responseHandler
 */

class Dispatcher {

    private val client: OkHttpClient = OkHttpClient()

    /**
     * Dispatch an [APIRequest] and enqueue the responseHandler to run once
     * a result have been returned from the api.
     *
     * @param apiRequest generic request containing required request params.
     * @param responseHandler is called on api result and handles the deserialization
     *        and mapping of json properties.
     */
    fun dispatch(apiRequest: APIRequest, responseHandler: Callback) {

        val path = apiRequest.path

        var requestBuilder = Request.Builder()
                .url(path)

        apiRequest.header?.let { header ->
            requestBuilder.addHeader(header.name, header.value)
        }

        apiRequest.body?.let {
            requestBuilder = when (apiRequest.method) {

                HTTPMethod.GET -> requestBuilder.get()
                HTTPMethod.POST -> requestBuilder.post(it)
                HTTPMethod.DELETE -> requestBuilder.delete(it)
                HTTPMethod.PATCH -> requestBuilder.patch(it)
                HTTPMethod.PUT -> requestBuilder.put(it)
            }
        }

        Log.d("/dev dispatching Api", """method: ${apiRequest.method}
            path: ${apiRequest.path}
            header: ${apiRequest.header?.name} : ${apiRequest.header?.value}
            body: ${apiRequest.body}""".trimMargin())

        client.newCall(requestBuilder.build()).enqueue(responseHandler)
    }
}