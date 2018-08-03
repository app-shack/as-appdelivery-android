package com.appshack.versiongate.network.api_requests

import com.appshack.versiongate.config.Config
import okhttp3.HttpUrl


/**
 * Created by joelbrostrom on 2018-07-27
 * Developed by App Shack
 */
class UpdateRequest : APIRequest() {
    override val method: HTTPMethod = HTTPMethod.GET
    override val path: HttpUrl = HttpUrl.parse(Config.baseUrl + UrlPaths.Updates.path )!!.newBuilder().build()
}