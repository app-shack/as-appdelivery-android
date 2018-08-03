package com.appshack.versiongate

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.appshack.versiongate.network.APIRequest
import com.appshack.versiongate.network.Dispatcher
import com.appshack.versiongate.network.ResponseHandler
import com.appshack.versiongate.network.api_requests.UpdateRequest
import com.appshack.versiongate.network.response_models.UpdateResponseModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("@dev onCreate", "layout completed")

        setupListeners()
        checkForUpdates()
    }

    private fun setupListeners() {
        fetchButton.setOnClickListener{checkForUpdates()}
    }

    private fun checkForUpdates() {
        responseText.text = "Fetching..."
        val apiRequest: APIRequest = UpdateRequest()
        val dispatcher = Dispatcher()
        dispatcher.dispatch(apiRequest, ResponseHandler(onResultCallback))

    }

    private val onResultCallback: ResultCallback = object : ResultCallback {
        override fun onComplete(result: Any?) {
            (result as UpdateResponseModel).let {
                runOnUiThread {

                    with(it.data!!) {
                        android.util.Log.i("@dev Response", identifier)
                        android.util.Log.i("@dev Response", latestVersion)
                        android.util.Log.i("@dev Response", requiredVersion)
                        responseText.text = identifier + latestVersion + requiredVersion
                    }
                }
            }
        }

        override fun onFailure(error: IOException?) {
            runOnUiThread {
                responseText.text = error?.toString()
            }
        }

    }
}


interface ResultCallback {
    fun onComplete(result: Any?)
    fun onFailure(error: IOException?)
}
