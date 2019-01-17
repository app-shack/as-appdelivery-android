package com.appshack.appdelivery.network.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Response data model from server request.
 *
 * @property versions contains list of all available versions represented as [VersionDataModel].
 * @property minVersion the required version. If local device have lower
 *           version the app should lock until requirements are met.
 * @property recommendedVersion the latest stable version.
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