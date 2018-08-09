package com.appshack.appdelivery.interfaces

import android.content.Context
import com.appshack.appdelivery.entity.VersionCheckResult


/**
 * Created by joelbrostrom on 2018-08-03
 * Developed by App Shack
 */


interface AppDeliveryInterface {
    val context: Context?
    fun setTextViewText(text: String)
    fun onVersionCheckResult(versionCheckResult: VersionCheckResult)
}