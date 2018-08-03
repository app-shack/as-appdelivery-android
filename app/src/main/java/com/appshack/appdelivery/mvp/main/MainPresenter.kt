package com.appshack.appdelivery.mvp.main

import com.appshack.appdelivery.network.api.models.VersionDataModel
import com.appshack.appdelivery.network.api.parsers.ResponseParser
import com.appshack.appdelivery.network.api.requests.APIRequest
import com.appshack.appdelivery.network.api.requests.UpdateRequest
import com.appshack.appdelivery.network.dispatchers.Dispatcher


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

    fun evaluateVersionStatus(versionDataModel: VersionDataModel) {
        with(versionDataModel) {
            val currentVersion = currentVersion?.split(".")?.map { it.toInt() } ?: listOf(0, 0, 0)
            val minimumVersion = requiredVersion?.split(".")?.map { it.toInt() } ?: listOf(0, 0, 0)
            val lastVersion = latestVersion?.split(".")?.map { it.toInt() } ?: listOf(0, 0, 0)

            isUpdateRequired = isVersionGraterThen(currentVersion, minimumVersion)
            isUpdateAvailable = isVersionGraterThen(currentVersion, lastVersion)

        }

    }

    private fun isVersionGraterThen(leftVersion: List<Int>, rightVersion: List<Int>): Boolean {
        var isLeftGreater: Boolean? = null
        for ((index, left) in leftVersion.withIndex()) {
            when {
                left > rightVersion[index] && isLeftGreater == null -> isLeftGreater = false
                left < rightVersion[index] && isLeftGreater == null -> isLeftGreater = true
            }
        }
        return isLeftGreater ?: false
    }

    private val onResultCallback: ResultCallback = object : ResultCallback {

        override fun onComplete(versionDataModel: VersionDataModel?) {
            versionDataModel?.let {
                android.util.Log.i("@dev Response", it.identifier)
                android.util.Log.i("@dev Response", it.latestVersion)
                android.util.Log.i("@dev Response", it.requiredVersion)

                it.currentVersion = activity.packageInformation.versionName
                evaluateVersionStatus(it)
                activity.setTextViewText("name: ${it.identifier}\n" +
                        "current version: ${it.currentVersion}\n" +
                        "min version: ${it.requiredVersion} \n" +
                        "latest version: ${it.latestVersion}\n\n" +
                        "update required: ${it.isUpdateRequired}\n" +
                        "update available: ${it.isUpdateAvailable}")
            }
        }

        override fun onFailure(error: String?) {
            activity.setTextViewText(error ?: "Unknown error")
        }

    }
}

interface ResultCallback {
    fun onComplete(result: VersionDataModel?)
    fun onFailure(error: String?)
}
