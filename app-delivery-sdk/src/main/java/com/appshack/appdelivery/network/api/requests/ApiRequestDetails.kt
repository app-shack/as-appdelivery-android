package com.appshack.appdelivery.network.api.requests

/**
 * Created by joelbrostrom on 2019-01-16
 * Developed by App Shack
 */

/**
 * Holds data necessary for api request
 * @param apiKey the string provided by backend for identification.
 * @param packageName the apps package name for identifying project and platform.
 */
data class ApiRequestDetails(val apiKey: String, val packageName:String)

/**
 * Holds name:value key par for adding params to header in api request.
 * @param name identifying key
 * @param value value of property
 */
data class HeaderPair(val name: String, val value: String)