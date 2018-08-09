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

    override fun onFailure(call: Call?, e: IOException?) {
        Log.d("@dev Fail:", e.toString())

        callback.onFailure(e.toString())
    }

    override fun onResponse(call: Call?, response: Response?) {

        if (response?.code() !in 200..299) {
            callback.onFailure(response?.message())
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