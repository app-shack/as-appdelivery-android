package com.appshack.appdelivery.mvp.main

import android.content.pm.PackageInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.appshack.appdelivery.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainActivityPresenterRelations.Activity {

    override lateinit var packageInformation: PackageInfo
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        packageInformation = this.packageManager.getPackageInfo(packageName, 0)
        presenter = MainPresenter(this)
        Log.d("@dev onCreate", "layout completed")

        setupListeners()
        presenter.checkForUpdates()
    }

    private fun setupListeners() {
        fetchButton.setOnClickListener { presenter.checkForUpdates() }
    }

    override fun setTextViewText(text: String) {
        runOnUiThread {
            responseText.text = text
        }
    }
}

