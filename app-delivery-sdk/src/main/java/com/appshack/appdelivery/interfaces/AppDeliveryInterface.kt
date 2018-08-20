package com.appshack.appdelivery.interfaces

import android.content.Context
import com.appshack.appdelivery.entity.VersionResult

/**
 * Created by joelbrostrom on 2018-08-03
 * Developed by App Shack
 *
 * Purpose: Implemented by the Class holding an AppDelivery object
 *
 * Context: supplies context
 * onVersionCheckResult: will be called when AppDelivery gets a result from checkResult().
 * versionCheckResult contains a VersionResult code which can be used to determine the result state.
 */
interface AppDeliveryInterface {
    val context: Context?
    fun onVersionCheckResult(versionResult: VersionResult)
}