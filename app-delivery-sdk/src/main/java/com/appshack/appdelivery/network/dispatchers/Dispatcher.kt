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

    fun dispatch(apiRequest: APIRequest, responseHandler: Callback) {

        val path = apiRequest.path

        //TODO Fetch the api key from the SDK init function and pass it along here.
        //NOTE: the api key needs to be prefixed with "api-key "
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