package com.appshack.appdelivery.utility.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.appshack.appdelivery.entity.VersionCheckResult
import com.appshack.appdelivery.entity.VersionResultCode


/**
 * Created by joelbrostrom on 2018-08-13
 * Developed by App Shack
 */
class VersionAlert(context: Context, versionCheckResult: VersionCheckResult) {

    private var appName: String? = null
    private val defaultDialog = AlertDialog.Builder(context)


    init {
        val appInfo = context.applicationInfo
        appName = context.packageManager.getApplicationLabel(appInfo).toString()
        defaultDialog.setTitle(versionCheckResult.resultCode.string)
                .setMessage("There is a new version of ${appName ?: "this App."}")
                .setPositiveButton("Download") { _, _ ->
                    val downloadPath = Uri.parse(versionCheckResult.downloadUrl)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = downloadPath
                    context.startActivity(intent)
                }
        if (versionCheckResult.resultCode != VersionResultCode.UPDATE_REQUIRED) {
            defaultDialog.setNegativeButton("Later") { dialog, _ -> dialog.dismiss() }
        } else {
            defaultDialog.setCancelable(false)
        }
    }

    fun show() {
        defaultDialog.create().show()
    }


}