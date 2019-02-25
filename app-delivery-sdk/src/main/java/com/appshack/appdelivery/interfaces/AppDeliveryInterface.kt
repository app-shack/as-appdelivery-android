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

/**
 * Purpose: Holds all package information from the app implementing appDeliveryInterface.
 *
 * @param appDeliveryInterface Interface providing context needed to access the implementors context.
 *        context is used  to obtain the relevant Package Info from the implementor.
 *        Since this library is not part of the app implementing it BuildContext will only return the
 *        library PackageInfo.
 */
interface AppDeliveryInterface {
    val context: Context
    fun onVersionCheckResult(versionResult: VersionResult)

    val bundleId: String
        get() = context.packageName
    val versionName: String
        get() = context.packageManager.getPackageInfo(bundleId, 0).versionName
    val versionCode: Int
        get() = context.packageManager.getPackageInfo(bundleId, 0).versionCode
}