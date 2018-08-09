package com.appshack.appdelivery.sampleactivity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import com.appshack.appdelivery.R
import com.appshack.appdelivery.entity.VersionCheckResult
import com.appshack.appdelivery.entity.VersionResultCode
import com.appshack.appdelivery.interfaces.AppDeliveryInterface
import com.appshack.appdelivery.logic.AppDelivery
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AppDeliveryInterface {

    override val context: Context
        get() {
            return this
        }

    override fun onVersionCheckResult(versionCheckResult: VersionCheckResult) {
        when (versionCheckResult.resultCode) {

            VersionResultCode.UP_TO_DATE -> { //All good
                val statusText = statusTextView
                val statusTextString = SpannableString("Up to date!")
                statusTextString.setSpan(
                        ForegroundColorSpan(Color.GREEN), 0, statusTextString.length,0)
                statusTextString.setSpan(RelativeSizeSpan(1.5f), 0,statusTextString.length, 0)

                runOnUiThread {
                    statusText.text = statusTextString
                }
            }

            VersionResultCode.UPDATE_AVAILABLE -> { //Implement custom prompt to upgrade
                val statusText = statusTextView
                val statusTextString = SpannableString("Update Available\ndownload link: ${versionCheckResult.downloadUrl}")
                statusTextString.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(this, R.color.orangeWarning)), 0, 16,0)
                statusTextString.setSpan(RelativeSizeSpan(1.5f), 0, 16, 0)
                runOnUiThread {
                    statusText.text = statusTextString
                }
            }

            VersionResultCode.UPDATE_REQUIRED -> { //Implement lock down here
                val statusText = statusTextView
                val statusTextString = SpannableString("Update Required!\ndownload link: ${versionCheckResult.downloadUrl}")
                statusTextString.setSpan(
                        ForegroundColorSpan(Color.RED), 0, 15,0)
                statusTextString.setSpan(RelativeSizeSpan(1.5f), 0,15, 0)
                runOnUiThread {
                    statusText.text = statusTextString
                }
            }

            VersionResultCode.ERROR -> Unit //Handle error here
        }
    }

    lateinit var presenter: AppDelivery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = AppDelivery(this)
        Log.d("@dev onCreate", "layout completed")

        setupListeners()
        presenter.startVersionCheckForResult()
    }

    private fun setupListeners() {
        fetchButton.setOnClickListener { presenter.startVersionCheckForResult() }
    }

    override fun setTextViewText(text: String) {
        runOnUiThread {
            responseText.text = text
        }
    }
}

