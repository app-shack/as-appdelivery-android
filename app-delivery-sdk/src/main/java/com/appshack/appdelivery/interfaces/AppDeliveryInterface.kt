package com.appshack.appdelivery.interfaces

import android.content.Context
import com.appshack.appdelivery.entity.VersionResult
import com.appshack.appdelivery.entity.VersionResultCode

/**
 * Created by joelbrostrom on 2018-08-03
 * Developed by App Shack
 */

/**
 * Purpose: Implemented by the Class holding an AppDelivery object
 *
 * @property context supplies context
 * @property onVersionCheckResult will be called when AppDelivery gets a result from checkResult().
 * [VersionResult] contains a [VersionResultCode] which contains information used to determine
 * the current local app state.
 */
interface AppDeliveryInterface {

    val context: Context?
    fun onVersionCheckResult(versionResult: VersionResult)
}