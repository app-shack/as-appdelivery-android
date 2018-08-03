package com.appshack.appdelivery.mvp.main

import com.appshack.appdelivery.network.api.requests.APIRequest
import com.appshack.appdelivery.network.api.requests.UpdateRequest
import com.appshack.appdelivery.network.dispatchers.Dispatcher
import com.appshack.appdelivery.network.api.parsers.ResponseParser
import com.appshack.appdelivery.network.api.models.UpdateResponseModel
import java.io.IOException


/**
 * Created by joelbrostrom on 2018-08-03
 * Developed by App Shack
 */
class MainPresenter(val activity: MainActivityPresenterRelations.Activity) : MainActivityPresenterRelations.Presenter {
    override fun checkForUpdates() {
        activity.setTextViewText("Fetching...")
        val apiRequest: APIRequest = UpdateRequest()
        val dispatcher = Dispatcher()
        dispatcher.dispatch(apiRequest, ResponseParser(onResultCallback))

    }

    private val onResultCallback: ResultCallback = object : ResultCallback {

        override fun onComplete(result: UpdateResponseModel?) {
            with((result as UpdateResponseModel).data!!) {
                android.util.Log.i("@dev Response", identifier)
                android.util.Log.i("@dev Response", latestVersion)
                android.util.Log.i("@dev Response", requiredVersion)
                activity.setTextViewText(identifier + latestVersion + requiredVersion)
            }
        }

        override fun onFailure(error: IOException?) {
            activity.setTextViewText(error?.toString() ?: "Unknown error")
        }

    }
}

interface ResultCallback {
    fun onComplete(result: UpdateResponseModel?)
    fun onFailure(error: IOException?)
}
