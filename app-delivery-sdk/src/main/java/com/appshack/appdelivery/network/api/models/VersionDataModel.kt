package com.appshack.appdelivery.network.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Response data model from server request.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class VersionDataModel(
        @field:JsonProperty("current_version")
        var currentVersion: String? = null,

        @field:JsonProperty("required_version")
        val requiredVersion: String? = null,

        @field:JsonProperty("build_version")
        val latestVersion: String? = null,

        @field:JsonProperty("build_number")
        val build_number: String? = null,

        @field:JsonProperty("apk_file")
        val latestVersionUrl: String? = null
)
