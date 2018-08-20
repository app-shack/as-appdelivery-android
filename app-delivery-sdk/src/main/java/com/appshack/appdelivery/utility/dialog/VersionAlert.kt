package com.appshack.appdelivery.utility.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.appshack.appdelivery.entity.VersionResult
import com.appshack.appdelivery.entity.VersionResultCode


/**
 * Created by joelbrostrom on 2018-08-13
 * Developed by App Shack
 *
 * Purpose: Display a default alert dialog prompting or forcing the user to update the app.
 */
class VersionAlert : Activity() {

    /**
     * Static function showDialog() starts an activity necessary to display the alert dialog.
     */
    companion object {
        fun showDialog(context: Context, versionResult: VersionResult) {
            val intent = Intent(context, VersionAlert::class.java)
            intent.putExtra("versionResult", versionResult)
            context.startActivity(intent)
        }
    }

    /**
     * Creates an alert dialog using the data stored in the intent.
     * If ResultCode is .UPDATE_AVAILABLE the dialog is dismissible,
     * else it's .UPDATE_REQUIRED and the dialog cannot be dismissed.
     *
     * The positive button routs the user to the url stored in the versionResult.
     * Typically this is the Download path for the latest app version.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val versionResult = intent.getSerializableExtra("versionCheckResult") as VersionResult

        val appInfo = this.applicationInfo
        val appName = this.packageManager.getApplicationLabel(appInfo).toString()

        val defaultDialog = AlertDialog.Builder(this)
                .setTitle(versionResult.resultCode.string)
                .setMessage("There is a new version of $appName")
                .setPositiveButton("Download") { _, _ ->
                    val downloadPath = Uri.parse(versionResult.downloadUrl)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = downloadPath
                    this.startActivity(intent)
                    finish()
                }

        if (versionResult.resultCode != VersionResultCode.UPDATE_REQUIRED) {
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