package com.appshack.appdelivery.entity

import java.io.Serializable

/**
 * Data model holding JSON variables returned from REST request
 */
data class VersionResult(
        val resultCode: VersionResultCode,
        val downloadUrl: String? = null,
        val deviceVersion: List<Int>? = null,
        val minimumVersion: List<Int>? = null,
        val recommendedVersion: List<Int>? = null
) : Serializable