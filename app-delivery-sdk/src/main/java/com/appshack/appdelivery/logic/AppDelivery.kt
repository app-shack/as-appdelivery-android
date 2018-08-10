package com.appshack.appdelivery.logic

import com.appshack.appdelivery.entity.VersionCheckResult
import com.appshack.appdelivery.entity.VersionResultCode
import com.appshack.appdelivery.interfaces.AppDeliveryInterface
import com.appshack.appdelivery.interfaces.ResultCallback
import com.appshack.appdelivery.network.api.models.VersionDataModel
import com.appshack.appdelivery.network.api.parsers.ResponseParser
import com.appshack.appdelivery.network.api.requests.APIRequest
import com.appshack.appdelivery.network.api.requests.UpdateRequest
import com.appshack.appdelivery.network.dispatchers.Dispatcher
import com.appshack.appdelivery.utility.extensions.cleanListPrint
import com.appshack.appdelivery.utility.extensions.toVersionList


/**
 * Created by joelbrostrom on 2018-08-03
 * Developed by App Shack
 */
class AppDelivery(val appDeliveryInterface: AppDeliveryInterface) {

    fun startVersionCheckForResult() {
        appDeliveryInterface.setTextViewText("Fetching...")
        val apiRequest: APIRequest = UpdateRequest()
        val dispatcher = Dispatcher()
        dispatcher.dispatch(apiRequest, ResponseParser(onResultCallback))
    }

    fun getMaxLength(candidates: List<List<Int>>): Int {
        return candidates.maxBy { it.size }?.size ?: 0
    }

    fun adjustVersionLength(versions: List<MutableList<Int>>, length: Int): List<MutableList<Int>> {
        val adjustedVersions = mutableListOf<MutableList<Int>>()
        for (version in versions) {
            while (version.size < length) {
                version.add(0)
            }
            adjustedVersions.add(version)
        }
        return adjustedVersions
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

    fun buildVersionCheckResult(versionDataModel: VersionDataModel): VersionCheckResult {
        with(versionDataModel) {
            val currentVersion = getCurrentVersion()
            val downloadUrl = latestVersionUrl
            val minimumVersion = requiredVersion?.toVersionList()
            val maximumVersion = latestVersion?.toVersionList()
            val versions = listOfNotNull(currentVersion, minimumVersion, maximumVersion)
            val maxLength = getMaxLength(versions)

            adjustVersionLength(versions, maxLength)

            val isUpdateRequired: Boolean? = if (minimumVersion != null )
                isVersionGraterThen(minimumVersion, currentVersion) else null
            val isUpdateAvailable: Boolean? = if (maximumVersion != null)
                isVersionGraterThen(maximumVersion, currentVersion) else null
            val versionCheckResult = getVersionResultCode(isUpdateRequired, isUpdateAvailable)

            return VersionCheckResult(
                    versionCheckResult,
                    downloadUrl,
                    currentVersion,
                    minimumVersion,
                    maximumVersion,
                    isUpdateRequired,
                    isUpdateAvailable)
        }
    }

    private fun getCurrentVersion(): MutableList<Int> {
        return appDeliveryInterface.context?.packageManager
                ?.getPackageInfo(appDeliveryInterface.context?.packageName, 0)
                ?.versionName
                ?.toVersionList()
                ?: mutableListOf(0, 0, 0)
    }

    private val onResultCallback: ResultCallback = object : ResultCallback {

        override fun onComplete(result: VersionDataModel?) {
            result?.let {
                val versionCheckResult = buildVersionCheckResult(it)
                appDeliveryInterface.onVersionCheckResult(versionCheckResult)

                /**
                 * For debugging and testing
                 */
                appDeliveryInterface.setTextViewText("name: ${it.identifier}\n" +
                        "current version: ${versionCheckResult.currentVersion?.cleanListPrint()}\n" +
                        "minimum version: ${versionCheckResult.minimumVersion?.cleanListPrint()}\n" +
                        "maximum version: ${versionCheckResult.maximumVersion?.cleanListPrint()}\n\n" +
                        "update required: ${versionCheckResult.isUpdateRequired}\n" +
                        "update available: ${versionCheckResult.isUpdateAvailable}\n")
            }
        }

        override fun onFailure(error: String?) {
            val versionCheckResult = VersionCheckResult(VersionResultCode.ERROR, errorMessage = error)
            appDeliveryInterface.onVersionCheckResult(versionCheckResult)
            appDeliveryInterface.setTextViewText(error ?: "Unknown error")
        }

    }

}


