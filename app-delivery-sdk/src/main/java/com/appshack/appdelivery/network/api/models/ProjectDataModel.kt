package com.appshack.appdelivery.network.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Response data model from server request.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProjectDataModel(
        @field:JsonProperty("android_versions")
        val versions: List<VersionDataModel>? = null,

        @field:JsonProperty("min_version_android")
        val minVersion: String? = null,

        @field:JsonProperty("recommended_version_android")
        val recommendedVersion: String? = null
)