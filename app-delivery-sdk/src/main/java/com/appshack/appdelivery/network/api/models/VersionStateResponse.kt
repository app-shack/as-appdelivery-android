package com.appshack.appdelivery.network.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Response data model from server request.
 *
 * @property minVersionCode the required version. If local device have lower
 *           version the app should lock until requirements are met.
 * @property latestVersionName holds the android gradle versionName. ex. 1.3.1.
 * @property latestVersionCode holds the android gradle versionCode. ex. 23.
 * @property releaseNotes holds release notes for the this version.
 * @property apkFile holds path to the latest apk version.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class VersionStateResponse(

	@field:JsonProperty("releaseNotes")
	val releaseNotes: String,

	@field:JsonProperty("minVersionCode")
	val minVersionCode: Int,

	@field:JsonProperty("apkFile")
	val apkFile: String,

	@field:JsonProperty("latestVersionName")
	val latestVersionName: String,

	@field:JsonProperty("latestVersionCode")
	val latestVersionCode: Int
)