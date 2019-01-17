package com.appshack.appdelivery.network.api.requests

import com.appshack.appdelivery.config.Config
import okhttp3.HttpUrl

/**
 * Created by joelbrostrom on 2018-07-27
 * Developed by App Shack
 *
 * Purpose: Contains all information needed for an API-request
 * @param packageApiDetails takes a [PackageApiDetailsModel] containing information
 *        needed to construct a version status request
 */
class VersionStatusRequest(packageApiDetails: PackageApiDetailsModel?) : APIRequest() {
    private val apiPrefix = "api-key "
    override val method: HTTPMethod = HTTPMethod.GET
    override val path: HttpUrl = HttpUrl.parse(Config.baseUrl + UrlPaths.PROJECT.path + packageApiDetails?.packageName)!!
    override val header: HeaderPair? = packageApiDetails?.apiKey?.let { value ->
        HeaderPair(Header.AUTHORIZATION.string, apiPrefix + value)
    }
}