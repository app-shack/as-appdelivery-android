package com.appshack.appdelivery.entity

data class VersionCheckResult(
        val resultCode: VersionResultCode,
        val downloadUrl: String? = null,
        val currentVersion: List<Int>? = null,
        val minimumVersion: List<Int>? = null,
        val maximumVersion: List<Int>? = null,
        val isUpdateRequired: Boolean? = null,
        val isUpdateAvailable: Boolean? = null,
        val errorMessage: String? = null

)