package com.appshack.appdelivery.network.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Data model returned from api request and resembles a specific version.
 *
 * @property versionName holds the android gradle versionName. ex. 1.3.1
 * @property buildNumber holds the android gradle versionCode. ex. 23
 * @property apkFile holds path to this versions apk.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class VersionDataModel(

        @field:JsonProperty("build_version")
        val versionName: String? = null,

        @field:JsonProperty("build_number")
        val buildNumber: String? = null,

        @field:JsonProperty("apk_file")
        val apkFile: String? = null
)
