package com.appshack.appdelivery.mvp.main

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.appshack.appdelivery.R
import com.appshack.appdelivery.entity.VersionCheckResult
import com.appshack.appdelivery.entity.VersionResultCode
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AppDeliveryInterface {

    override val context: Context
        get() {
            return this
        }

    override fun onVersionCheckResult(versionCheckResult: VersionCheckResult) {
        when (versionCheckResult.resultCode) {
            VersionResultCode.UP_TO_DATE -> Unit
            VersionResultCode.UPDATE_AVAILABLE -> Unit //Implement custom prompt to upgrade
            VersionResultCode.UPDATE_REQUIRED -> Unit //Implement lock down here
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

