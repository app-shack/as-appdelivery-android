package com.appshack.appdelivery.entity

import java.io.Serializable

/**
 * Data model returned to the AppDeliveryInterface passed at initiation of AppDelivery.
 * This is the only data passed between the implementor and the SDK and contains all
 * results calculated.
 *
 * @property resultCode enum containing the result of the version check.
 * @property downloadUrl holds path to apk.
 * @property currentVersionName holds the current version of the app on the local device.
 * @property maxVersionName holds the recommended version of the app.
 * @property currentVersionCode holds the current version code on the local device.
 * @property minVersionCode holds the minimum required version code.
 * @property maxVersionCode holds the maximum version code of the app.
 */
data class VersionResult(
        val resultCode: VersionResultCode,
        val downloadUrl: String? = null,
        val currentVersionName: String? = null,
        val maxVersionName: String? = null,
        val currentVersionCode: Int? = null,
        val minVersionCode: Int? = null,
        val maxVersionCode: Int? = null
        ) : Serializable