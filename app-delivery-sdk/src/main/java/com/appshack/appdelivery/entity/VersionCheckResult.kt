package com.appshack.appdelivery.entity

import java.io.Serializable

data class VersionCheckResult(
        val resultCode: VersionResultCode,
        val downloadUrl: String? = null,
        val currentVersion: List<Int>? = null,
        val minimumVersion: List<Int>? = null,
        val maximumVersion: List<Int>? = null,
        val errorMessage: String? = null
) : Serializable