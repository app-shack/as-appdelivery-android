package com.appshack.appdelivery.network.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProjectDataModel(
        @field:JsonProperty("android_versions")
        val versions: List<VersionDataModel>? = null,

        @field:JsonProperty("min_version_android")
        val min_version_android: String? = null,

        @field:JsonProperty("recommended_version_android")
        val recommended_version_android: String? = null
)