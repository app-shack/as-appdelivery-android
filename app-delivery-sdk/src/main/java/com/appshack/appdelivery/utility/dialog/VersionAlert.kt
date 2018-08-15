package com.appshack.appdelivery.utility.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.appshack.appdelivery.entity.VersionCheckResult
import com.appshack.appdelivery.entity.VersionResultCode


/**
 * Created by joelbrostrom on 2018-08-13
 * Developed by App Shack
 */
class VersionAlert : Activity() {

    companion object {
        fun showDialog(context: Context, versionCheckResult: VersionCheckResult) {
            val intent = Intent(context, VersionAlert::class.java)
            intent.putExtra("versionCheckResult", versionCheckResult)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val versionCheckResult = intent.getSerializableExtra("versionCheckResult") as VersionCheckResult

        val appInfo = this.applicationInfo
        val appName = this.packageManager.getApplicationLabel(appInfo).toString()

        val defaultDialog = AlertDialog.Builder(this)
                .setTitle(versionCheckResult.resultCode.string)
                .setMessage("There is a new version of $appName")
                .setPositiveButton("Download") { _, _ ->
                    val downloadPath = Uri.parse(versionCheckResult.downloadUrl)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = downloadPath
                    this.startActivity(intent)
                    finish()
                }

        if (versionCheckResult.resultCode != VersionResultCode.UPDATE_REQUIRED) {
            defaultDialog.setNegativeButton("Later") { dialog, _ -> dialog.dismiss()
                finish()}
        } else {
            defaultDialog.setCancelable(false)
        }

        runOnUiThread {
            defaultDialog.create().show()
        }
    }
}