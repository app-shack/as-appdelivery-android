package com.appshack.testmodule

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import com.appshack.appdelivery.entity.VersionCheckResult
import com.appshack.appdelivery.entity.VersionResultCode
import com.appshack.appdelivery.interfaces.AppDeliveryInterface
import com.appshack.appdelivery.logic.AppDelivery
import com.appshack.appdelivery.utility.dialog.VersionAlert
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AppDeliveryInterface {

    override val context: Context
        get() {
            return this
        }

    private lateinit var appDelivery: AppDelivery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appDelivery = AppDelivery(this)
        Log.d("@dev onCreate", "layout completed")

        setupListeners()
        fetchButton.callOnClick()
    }

    override fun onVersionCheckResult(versionCheckResult: VersionCheckResult) {

        runOnUiThread {
            when (versionCheckResult.resultCode) {

                VersionResultCode.UP_TO_DATE -> { //All good
                    val statusText = statusTextView
                    val statusTextString = SpannableString("Up to date!")
                    statusTextString.setSpan(
                            ForegroundColorSpan(Color.GREEN), 0, statusTextString.length, 0)
                    statusTextString.setSpan(
                            RelativeSizeSpan(1.5f), 0, statusTextString.length, 0)
                    statusText.text = statusTextString
                }

                VersionResultCode.UPDATE_AVAILABLE -> { //Implement custom prompt to upgrade
                    val statusText = statusTextView
                    val statusTextString = SpannableString("Update Available\ndownload link: ${versionCheckResult.downloadUrl}")
                    statusTextString.setSpan(ForegroundColorSpan(ContextCompat.getColor(
                            this, R.color.orangeWarning)), 0, 16, 0)
                    statusTextString.setSpan(
                            RelativeSizeSpan(1.5f), 0, 16, 0)
                    statusText.text = statusTextString
                    VersionAlert.showDialog(this, versionCheckResult)
                }

                VersionResultCode.UPDATE_REQUIRED -> { //Implement lock down here
                    val statusText = statusTextView
                    val statusTextString = SpannableString("Update Required!\ndownload link: ${versionCheckResult.downloadUrl}")
                    statusTextString.setSpan(
                            ForegroundColorSpan(Color.RED), 0, 16, 0)
                    statusTextString.setSpan(
                            RelativeSizeSpan(1.5f), 0, 16, 0)
                    statusText.text = statusTextString
                    VersionAlert.showDialog(this, versionCheckResult)
                }

                VersionResultCode.ERROR -> { //Handle error here
                    responseText.text = versionCheckResult.errorMessage

                }
            }

            if (versionCheckResult.resultCode != VersionResultCode.ERROR) {
                responseText.text = "current version: ${versionCheckResult.currentVersion?.cleanListPrint()}\n" +
                        "minimum version: ${versionCheckResult.minimumVersion?.cleanListPrint()}\n" +
                        "maximum version: ${versionCheckResult.maximumVersion?.cleanListPrint()}\n\n"
            }
        }
    }

    private fun setupListeners() {
        fetchButton.setOnClickListener {
            responseText.text = "Fetching..."
            appDelivery.startVersionCheckForResult()
        }
    }

}

