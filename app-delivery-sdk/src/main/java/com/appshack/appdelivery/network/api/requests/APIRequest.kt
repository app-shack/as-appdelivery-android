package com.appshack.appdelivery.network.api.requests

import com.appshack.appdelivery.config.Config
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.RequestBody


/**
 * Created by joelbrostrom on 2018-07-27
 * Developed by App Shack
 */

/**
 * Interface for a HttpRequest.
 * Each REST request needs a path and a method,
 * but not necessarily a body.
 */
private interface HttpRequest {

    val path: HttpUrl
    val method: HTTPMethod
    val header: HeaderPair?
    var body: RequestBody?

}

/**
 * Predefine default values common for a http request.
 */
abstract class APIRequest : HttpRequest {

    override val path: HttpUrl
        get() = HttpUrl.parse(Config.baseUrl)!!
    override val method: HTTPMethod
        get() = HTTPMethod.GET
    override var body: RequestBody? = null
        get() = RequestBody.create(MediaType.FORM.type, "")
    override val header: HeaderPair? = null
}

/**
 * Enum containing all different REST methods.
 */
enum class HTTPMethod {
    POST(),
    GET(),
    PUT(),
    PATCH(),
    DELETE()
}

/**
 * Enum containing url paths
 */
enum class UrlPaths(val path: String) {
    PROJECT("/api/project/")
}

/**
 * Header properties
 */
enum class Header(val string: String){
    AUTHORIZATION("Authorization")
}

/**
 * Enum containing predefined MediaTypes
 */
enum class MediaType(val type: okhttp3.MediaType) {
    FORM(okhttp3.MediaType.parse("multipart/form-data")!!),
    PNG(okhttp3.MediaType.parse("image/png")!!)
}



