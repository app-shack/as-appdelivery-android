package com.appshack.appdelivery.network.api.requests

import com.appshack.appdelivery.config.Config
import okhttp3.HttpUrl

/**
 * Created by joelbrostrom on 2018-07-27
 * Developed by App Shack
 *
 * Purpose: Contains all information needed for an API-request
 * @param apiRequestDetails takes a [ApiRequestDetails] containing information
 *        needed to construct a version status request
 */
class VersionStateRequest(apiRequestDetails: ApiRequestDetails) : APIRequest() {
    private val apiPrefix = "api-key "
    override val method: HTTPMethod = HTTPMethod.GET
    override val path: HttpUrl? = HttpUrl.parse(Config.baseUrl + UrlPaths.PROJECT.path + '/' + apiRequestDetails.packageName)
    override val header: HeaderPair? =
            HeaderPair(Header.AUTHORIZATION.string, apiPrefix + apiRequestDetails.apiKey)
}