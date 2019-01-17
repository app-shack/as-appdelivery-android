package com.appshack.appdelivery.network.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class VersionDataModel(

        @field:JsonProperty("build_version")
        val versionName: String? = null,

        @field:JsonProperty("build_number")
        val buildNumber: String? = null,

        @field:JsonProperty("apk_file")
        val apkFile: String? = null
)
