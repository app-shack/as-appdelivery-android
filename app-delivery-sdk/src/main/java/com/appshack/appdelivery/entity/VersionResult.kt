package com.appshack.appdelivery.entity

import java.io.Serializable

/**
 * Data model returned to the AppDeliveryInterface passed at initiation of AppDelivery.
 * This is the only data passed between the implementor and the SDK and contains all
 * results calculated.
 *
 * @property resultCode enum containing the result of the version check.
 * @property downloadUrl holds path to apk
 * @property deviceVersion holds the current version of the app on the local device.
 * @property minimumVersion holds the minimum required (forced) version of the app.
 * @property recommendedVersion holds the recommended version of the app.
 */
data class VersionResult(
        val resultCode: VersionResultCode,
        val downloadUrl: String? = null,
        val deviceVersion: List<Int>? = null,
        val minimumVersion: List<Int>? = null,
        val recommendedVersion: List<Int>? = null
) : Serializable