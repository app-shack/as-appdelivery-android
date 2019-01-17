package com.appshack.appdelivery.utility.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.appshack.appdelivery.R
import android.widget.Toast
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
            intent.putExtra(context.getString(R.string.VERSION_RESULT), versionResult)
            context.startActivity(intent)
        }
    }

    /**
     * Creates an alert dialog using the data stored in the intent.
     * If ResultCode is .UPDATE_AVAILABLE the dialog is dismissible,
     * else it's .UPDATE_REQUIRED and the dialog cannot be dismissed.
     *
     * The positive button routs the user to the url stored in the [VersionResult].
     * Typically this is the Download path for the recommended app version.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val versionResult = intent.getSerializableExtra(getString(R.string.VERSION_RESULT)) as VersionResult

        val appInfo = this.applicationInfo
        val appName = this.packageManager.getApplicationLabel(appInfo).toString()

        val defaultDialog = AlertDialog.Builder(this)
                .setTitle(versionResult.resultCode.message)
                .setMessage(getString(R.string.NEW_VERSION_OF_APP_NAME, appName))
                .setPositiveButton("Download") { _, _ ->
                    if (versionResult.downloadUrl != null) {
                        val downloadPath = Uri.parse(versionResult.downloadUrl)
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = downloadPath
                        this.startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this,
                                getString(com.appshack.appdelivery.R.string.MISSING_APK_WARNING),
                                Toast.LENGTH_LONG)
                                .show()
                        showDialog(this, versionResult)
                        finish()
                    }
                }

        if (versionResult.resultCode != VersionResultCode.UPDATE_REQUIRED) {
            defaultDialog.setNegativeButton("Not Now") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
        } else {
            defaultDialog.setCancelable(false)
        }

        runOnUiThread {
            defaultDialog.create().show()
        }
    }
}