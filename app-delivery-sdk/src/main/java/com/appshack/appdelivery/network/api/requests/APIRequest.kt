package com.appshack.appdelivery.network.api.requests

import com.appshack.appdelivery.config.Config
import okhttp3.HttpUrl
import okhttp3.RequestBody


/**
 * Created by joelbrostrom on 2018-07-27
 * Developed by App Shack
 */

private interface HttpRequest {
    val path: HttpUrl
    val method: HTTPMethod
    var body: RequestBody?
}

abstract class APIRequest : HttpRequest {

    override val path: HttpUrl
        get() = HttpUrl.parse(Config.baseUrl)!!
    override val method: HTTPMethod
        get() = HTTPMethod.GET
    override var body: RequestBody? = null
        get() = RequestBody.create(MediaType.FORM, "")

}

enum class HTTPMethod {
    POST(),
    GET(),
    PUT(),
    PATCH(),
    DELETE()
}

enum class UrlPaths(val path: String) {
    Updates("/data")
}

class MediaType {
    companion object {
        val FORM = okhttp3.MediaType.parse("multipart/form-data")!!
        val PNG = okhttp3.MediaType.parse("image/png")!!
    }
}



