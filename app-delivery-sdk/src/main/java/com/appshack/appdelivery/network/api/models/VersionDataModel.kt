package com.appshack.appdelivery.network.api.models

import com.fasterxml.jackson.annotation.JsonProperty

data class VersionDataModel(

        @field:JsonProperty("identifier")
        val identifier: String? = null,

        @field:JsonProperty("required_version")
        val requiredVersion: String? = null,

        @field:JsonProperty("latest_version")
        val latestVersion: String? = null,

        @field:JsonProperty("latest_version_url")
        val latestVersionUrl: String? = null
)
