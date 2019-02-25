package com.appshack.appdelivery.network.api.models

/**
 * Data model returned from api request and resembles a specific version.
 *
 * @property versionName holds the android gradle versionName. ex. 1.3.1.
 * @property versionCode holds the android gradle versionCode. ex. 23.
 * @property releaseNotes holds release notes for the this version.
 * @property apkFile holds path to this versions apk.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
//data class VersionDataModel(
//
//        @field:JsonProperty("build_version")
//        val versionName: String? = null,
//
//        @field:JsonProperty("build_number")
//        val buildNumber: String? = null,
//
//        @field:JsonProperty("apk_file")
//        val apkFile: String? = null
//)

data class VersionDataModel(
        val versionCode: Int,
        val versionName: String? = null,
        val releaseNotes: String? = null,
        val apkFile: String? = null
)