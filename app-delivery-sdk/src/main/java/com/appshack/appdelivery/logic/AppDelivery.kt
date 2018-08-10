package com.appshack.appdelivery.logic

import com.appshack.appdelivery.entity.VersionCheckResult
import com.appshack.appdelivery.entity.VersionResultCode
import com.appshack.appdelivery.interfaces.AppDeliveryInterface
import com.appshack.appdelivery.interfaces.ResultCallback
import com.appshack.appdelivery.network.api.models.VersionDataModel
import com.appshack.appdelivery.network.api.parsers.ResponseParser
import com.appshack.appdelivery.network.api.requests.APIRequest
import com.appshack.appdelivery.network.api.requests.VersionStatusRequest
import com.appshack.appdelivery.network.dispatchers.Dispatcher
import com.appshack.appdelivery.utility.extensions.toVersionList


/**
 * Created by joelbrostrom on 2018-08-03
 * Developed by App Shack
 */
class AppDelivery(val appDeliveryInterface: AppDeliveryInterface) {

    fun startVersionCheckForResult() {
        val apiRequest: APIRequest = VersionStatusRequest()
        val dispatcher = Dispatcher()
        dispatcher.dispatch(apiRequest, ResponseParser(onResultCallback))
    }

    internal fun buildVersionCheckResult(versionDataModel: VersionDataModel): VersionCheckResult {
        val downloadUrl = versionDataModel.latestVersionUrl
        val currentVersion = getCurrentVersion()
        val minimumVersion = versionDataModel.requiredVersion?.toVersionList() ?: mutableListOf()
        val maximumVersion = versionDataModel.latestVersion?.toVersionList() ?: mutableListOf()

        val versions = listOf(currentVersion, minimumVersion, maximumVersion)
        val maxLength = getMaxLength(versions)
        adjustVersionLength(versions, maxLength)

        val isUpdateRequired = isVersionGraterThen(minimumVersion, currentVersion)
        val isUpdateAvailable = isVersionGraterThen(maximumVersion, currentVersion)
        val resultCode = getVersionResultCode(isUpdateRequired, isUpdateAvailable)

        return VersionCheckResult(
                resultCode,
                downloadUrl,
                currentVersion,
                minimumVersion,
                maximumVersion)
    }

    private fun getCurrentVersion(): MutableList<Int> {
        return appDeliveryInterface.context?.packageManager
                ?.getPackageInfo(appDeliveryInterface.context?.packageName, 0)
                ?.versionName
                ?.toVersionList()
                ?: mutableListOf(0, 0, 0)
    }

        return candidates.maxBy { it.size }?.size ?: 0
    }

    internal fun adjustVersionLength(versions: List<MutableList<Int>>, length: Int): List<MutableList<Int>> {
        for (version in versions) {
            while (version.size < length) {
                version.add(0)
            }
        }
        return versions
    }

    fun isVersionGraterThen(leftVersion: List<Int>, rightVersion: List<Int>): Boolean {
        for ((index, left) in leftVersion.withIndex()) when {
            left > rightVersion[index] -> return true
            left < rightVersion[index] -> return false
        }
        return false
    }

    fun getVersionResultCode(isUpdateRequired: Boolean?, isUpdateAvailable: Boolean?): VersionResultCode {
        return when {
            isUpdateRequired == true -> VersionResultCode.UPDATE_REQUIRED
            isUpdateAvailable == true -> VersionResultCode.UPDATE_AVAILABLE
            else -> VersionResultCode.UP_TO_DATE
        }
    }

    private val onResultCallback: ResultCallback = object : ResultCallback {

        override fun onComplete(result: VersionDataModel?) {
            result?.let {
                val versionCheckResult = buildVersionCheckResult(it)
                appDeliveryInterface.onVersionCheckResult(versionCheckResult)

            }
        }

        override fun onFailure(error: String?) {
            val versionCheckResult = VersionCheckResult(VersionResultCode.ERROR, errorMessage = error)
            appDeliveryInterface.onVersionCheckResult(versionCheckResult)
        }

    }
}



