package com.appshack.appdelivery.network.api.parsers

import android.util.Log
import com.appshack.appdelivery.mvp.main.ResultCallback
import com.appshack.appdelivery.network.api.models.UpdateResponseModel
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

        callback.onFailure(e)
    }

    override fun onResponse(call: Call?, response: Response?) {

        response?.body()?.string()?.let {
            val mapper = jacksonObjectMapper()
            val obj = JsonParser().parse(it).asJsonObject.get("results").toString()
            val updateResponseModel: UpdateResponseModel = mapper.readValue(obj)

            callback.onComplete(updateResponseModel)
        }
    }
}