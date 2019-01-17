package com.appshack.appdelivery.network.api.parsers

import com.appshack.appdelivery.interfaces.ResultCallback
import com.appshack.appdelivery.network.api.models.ProjectDataModel
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
 *
 * Purpose: Parse response from Json to data model or send error message to interface
 */
class ResponseParser(private val callback: ResultCallback) : Callback {

    /**
     * Returns one of several common error messages or null.
     */
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

    /**
     * Calls interface with error message
     */
    override fun onFailure(call: Call?, e: IOException?) {
        callback.onFailure(e.toString())
    }

    /**
     * Checks if error message is returned and calls on failure if thats the case.
     * If not, proceed to parse the response Json object to a VersionDataModel and call Interface
     * with it as an argument.
     */
    override fun onResponse(call: Call?, response: Response?) {
        val errorMessage = getErrorMessage(response)
        if (errorMessage != null) {
            callback.onFailure(errorMessage)
        } else {
            response?.body()?.string()?.let {
                val mapper = jacksonObjectMapper()
                val obj = JsonParser().parse(it).toString()
                val projectDataModel: ProjectDataModel = mapper.readValue(obj)

                callback.onSuccess(projectDataModel)
            }
        }
    }
}