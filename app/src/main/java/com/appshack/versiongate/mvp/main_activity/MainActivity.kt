package com.appshack.versiongate.mvp.main_activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.appshack.versiongate.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainActivityPresenterRelations.Activity {

    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

