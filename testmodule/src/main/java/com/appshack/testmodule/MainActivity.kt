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
import com.appshack.appdelivery.entity.VersionResult
import com.appshack.appdelivery.entity.VersionResultCode.*
import com.appshack.appdelivery.interfaces.AppDeliveryInterface
import com.appshack.appdelivery.logic.AppDelivery
import com.appshack.appdelivery.utility.dialog.VersionAlert
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AppDeliveryInterface {

    override val context: Context = this

    private lateinit var appDelivery: AppDelivery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appDelivery = AppDelivery(this, "test")
        Log.d("@dev onCreate", "layout completed")

        setupListeners()
        responseText.text = getString(R.string.FETCHING)
        appDelivery.startVersionCheck()
    }

    override fun onVersionCheckResult(versionResult: VersionResult) {

        runOnUiThread {
            when (versionResult.resultCode) {

                UP_TO_DATE -> { //All good
                    val statusText = statusTextView
                    val statusTextString = SpannableString("Up to date!")
                    statusTextString.setSpan(
                            ForegroundColorSpan(Color.GREEN), 0, statusTextString.length, 0)
                    statusTextString.setSpan(
                            RelativeSizeSpan(1.5f), 0, statusTextString.length, 0)
                    statusText.text = statusTextString
                }

                UPDATE_AVAILABLE -> { //Implement custom prompt to upgrade
                    val statusText = statusTextView
                    val statusTextString = SpannableString("Update Available\ndownload link: ${versionResult.downloadUrl}")
                    statusTextString.setSpan(ForegroundColorSpan(ContextCompat.getColor(
                            this, R.color.orangeWarning)), 0, 16, 0)
                    statusTextString.setSpan(
                            RelativeSizeSpan(1.5f), 0, 16, 0)
                    statusText.text = statusTextString
                    VersionAlert.showDialog(this, versionResult)
                }

                UPDATE_REQUIRED -> { //Implement lock down here
                    val statusText = statusTextView
                    val statusTextString = SpannableString("Update Required!\ndownload link: ${versionResult.downloadUrl}")
                    statusTextString.setSpan(
                            ForegroundColorSpan(Color.RED), 0, 16, 0)
                    statusTextString.setSpan(
                            RelativeSizeSpan(1.5f), 0, 16, 0)
                    statusText.text = statusTextString
                    VersionAlert.showDialog(this, versionResult)
                }

                ERROR -> { //Handle error here
                    responseText.text = versionResult.resultCode.message
                }

            }

            if (versionResult.resultCode != ERROR) {
                responseText.text = "current version: ${versionResult.currentVersionName}(${versionResult.currentVersionCode})\n" +
                        "minimum version (only code): (${versionResult.minVersionCode})\n" +
                        "maximum version: ${versionResult.maxVersionName}(${versionResult.maxVersionCode})"
            }
        }
    }

    private fun setupListeners() {
        fetchButton.setOnClickListener {
            responseText.text = getString(R.string.FETCHING)
            appDelivery.startVersionCheck()
        }
    }

}

