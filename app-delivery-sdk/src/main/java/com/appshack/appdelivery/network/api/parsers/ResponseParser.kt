package com.appshack.appdelivery.network.api.parsers

import android.util.Log
import com.appshack.appdelivery.interfaces.ResultCallback
import com.appshack.appdelivery.network.api.models.VersionDataModel
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.JsonParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


/**
 * Created by joelbrostrom on 2018-07-27
 * Developed by App Shack
 */
class ResponseParser(private val callback: ResultCallback) : Callback {

    private fun getErrorMessage(response: Response?): String? {
        val responseCode = response?.code()?.let { it } ?: 0
        return when (responseCode) {
            400 -> "400 : Bad Request"
            401 -> "401 : Unauthorized"
            402 -> "402 : Payment Requested"
            403 -> "403 : Forbidden"
            404 -> "404 : Not Found"
            408 -> "408 : Request Timeout"
            409 -> "409 : Conflict"
            410 -> "410 : Gone"
            in 500..599 -> "5xx : Server Error"
            else -> null
        }
    }

    override fun onFailure(call: Call?, e: IOException?) {
        callback.onFailure(e.toString())
    }

    override fun onResponse(call: Call?, response: Response?) {
        val errorMessage = getErrorMessage(response)
        if (errorMessage != null) {
            callback.onFailure(errorMessage)
        } else {
            response?.body()?.string()?.let {
                val mapper = jacksonObjectMapper()
                val obj = JsonParser().parse(it).toString()
                val versionDataModel: VersionDataModel = mapper.readValue(obj)

                callback.onComplete(versionDataModel)
            }
        }
    }
}