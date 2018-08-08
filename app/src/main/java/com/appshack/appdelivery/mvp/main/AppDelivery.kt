package com.appshack.appdelivery.mvp.main

import com.appshack.appdelivery.entity.VersionCheckResult
import com.appshack.appdelivery.entity.VersionResultCode
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
class MainPresenter(val appDeliveryInterface: AppDeliveryInterface) {

    fun startVersionCheckForResult() {
        appDeliveryInterface.setTextViewText("Fetching...")
        val apiRequest: APIRequest = UpdateRequest()
        val dispatcher = Dispatcher()
        dispatcher.dispatch(apiRequest, ResponseParser(onResultCallback))
    }

    fun getMaxLength(candidates: List<List<Int>>): Int {
        return candidates.maxBy { it.size }?.size
                ?: 0
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

    fun isVersionGraterThen(leftVersion: List<Int>?, rightVersion: List<Int>?): Boolean {
        if (leftVersion == null || rightVersion == null) return false
        var isLeftGreater: Boolean? = null
        for ((index, left) in leftVersion.withIndex()) when {
            left > rightVersion[index] && isLeftGreater == null -> isLeftGreater = true
            left < rightVersion[index] && isLeftGreater == null -> isLeftGreater = false
        }
        return isLeftGreater ?: false
    }

    fun getVersionResultCode(isUpdateRequired: Boolean?, isUpdateAvailable: Boolean?)
            : VersionResultCode =
            when {
                isUpdateRequired == true -> VersionResultCode.UPDATE_REQUIRED
                isUpdateAvailable == true -> VersionResultCode.UPDATE_AVAILABLE
                else -> VersionResultCode.UP_TO_DATE
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

            val isUpdateRequired = isVersionGraterThen(minimumVersion, currentVersion)
            val isUpdateAvailable = isVersionGraterThen(maximumVersion, currentVersion)
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

    fun getCurrentVersion(): MutableList<Int> =
            appDeliveryInterface.context?.packageManager
                    ?.getPackageInfo(appDeliveryInterface.context?.packageName, 0)
                    ?.versionName
                    ?.toVersionList()
                    ?: mutableListOf(0, 0, 0)

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
                        "update available: ${versionCheckResult.isUpdateAvailable}\n" +
                        "download link: ${versionCheckResult.downloadUrl}")
            }

        }

        override fun onFailure(error: String?) {
            val versionCheckResult = VersionCheckResult(VersionResultCode.ERROR, errorMessage = error)
            appDeliveryInterface.onVersionCheckResult(versionCheckResult)
            appDeliveryInterface.setTextViewText(error ?: "Unknown error")
        }

    }
}

interface ResultCallback {
    fun onComplete(result: VersionDataModel?)
    fun onFailure(error: String?)
}

