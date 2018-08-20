package com.appshack.appdelivery.entity

import java.io.Serializable

/**
 * Data model holding JSON variables returned from REST request
 */
data class VersionResult(
        val resultCode: VersionResultCode,
        val downloadUrl: String? = null,
        val currentVersion: List<Int>? = null,
        val minimumVersion: List<Int>? = null,
        val maximumVersion: List<Int>? = null,
        val errorMessage: String? = null
) : Serializable