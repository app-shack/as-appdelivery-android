package com.appshack.appdelivery.logic

import com.appshack.appdelivery.entity.VersionResult
import com.appshack.appdelivery.entity.VersionResultCode
import com.appshack.appdelivery.interfaces.AppDeliveryInterface
import com.appshack.appdelivery.interfaces.ResultCallback
import com.appshack.appdelivery.network.api.models.ProjectDataModel
import com.appshack.appdelivery.network.api.parsers.ResponseParser
import com.appshack.appdelivery.network.api.requests.APIRequest
import com.appshack.appdelivery.network.api.requests.VersionStatusRequest
import com.appshack.appdelivery.network.dispatchers.Dispatcher
import com.appshack.appdelivery.utility.extensions.compareTo
import com.appshack.appdelivery.utility.extensions.toVersionList

/**
 * Created by joelbrostrom on 2018-08-03
 * Developed by App Shack
 *
 * Purpose: Make a REST request for version state, get current state and compare to se if app needs
 *          to be updated.
 *          If so, the user can be prompted to upgrade or the app can be locked down until the
 *          required minimum version is met.
 */
class AppDelivery(private val appDeliveryInterface: AppDeliveryInterface) {

    /**
     * Sets up API-request and a dispatcher.
     * Calls the dispatchers dispatch().
     */
    fun startVersionCheck() {
        val apiRequest: APIRequest = VersionStatusRequest(getPackageName())
        val dispatcher = Dispatcher()
        dispatcher.dispatch(apiRequest, ResponseParser(onResultCallback))
    }

    /**
     * Returns a VersionResult and set up their members.
     *
     * Extract data from versionModel and covert strings to List<Int>.
     * Adjusts versions to make all Lists equal in length.
     * Check if updates are available/required.
     * Creates ResultCode with correct result enum.
     * Constructs and return a VersionCode with the above as arguments.
     */
    internal fun buildVersionResult(projectDataModel: ProjectDataModel, currentVersionString: String): VersionResult {
        val versionDataModel = projectDataModel.versions?.firstOrNull()
        val currentVersion = currentVersionString.toVersionList()
        val minVersion = projectDataModel.min_version_android?.toVersionList() ?: mutableListOf()
        val maxVersion = versionDataModel?.latestVersion?.toVersionList() ?: mutableListOf()
        val downloadUrl = versionDataModel?.latestVersionUrl

        val versions = listOf(currentVersion, minVersion, maxVersion)
        val maxLength = getMaxLength(versions)
        adjustVersionLength(versions, maxLength)

        val isUpdateRequired = minVersion > currentVersion
        val isUpdateAvailable = maxVersion > currentVersion
        val resultCode = getVersionResultCode(isUpdateRequired, isUpdateAvailable)

        return VersionResult(
                resultCode,
                downloadUrl,
                currentVersion,
                minVersion,
                maxVersion)
    }

    internal fun getPackageName(): String? {
        return appDeliveryInterface.context?.packageName
    }

    /**
     * Returns versionName specified in app build.gradle,
     * or null if sufficient context is missing.
     */
    internal fun getCurrentVersion(): String? {
        return appDeliveryInterface.context?.packageManager
                ?.getPackageInfo(getPackageName(), 0)
                ?.versionName
    }

    /**
     * Returns the size of the longest List in a List of Lists
     */
    internal fun getMaxLength(candidates: List<List<Int>>): Int {
        return candidates.maxBy { it.size }?.size ?: 0
    }

    /**
     * Returns argument List with all elements padded with zeros to match the length argument.
     * The zeros are added to the end of the lists, so 1.2 becomes 1.2.0 if length is 3.
     */
    internal fun adjustVersionLength(versions: List<MutableList<Int>>, length: Int): List<MutableList<Int>> {
        for (version in versions) {
            while (version.size < length) {
                version.add(0)
            }
        }
        return versions
    }

    /**
     * Check if updates are needed and returns the appropriate VersionCode enum.
     */
    internal fun getVersionResultCode(isUpdateRequired: Boolean?, isUpdateAvailable: Boolean?): VersionResultCode {
        return when {
            isUpdateRequired == true -> VersionResultCode.UPDATE_REQUIRED
            isUpdateAvailable == true -> VersionResultCode.UPDATE_AVAILABLE
            else -> VersionResultCode.UP_TO_DATE
        }
    }

    /**
     * Handles callback result when dispatcher receives a result from API-request.
     *
     * If the result is completed the current version is fetched.
     * If it's null a Error version result is sent to the appDeliveryInterface,
     * else buildVersionResult() is called and its result sent to appDeliveryInterface.
     *
     * On failure appDeliveryInterface is called with an Error versionResult.
     */
    private val onResultCallback: ResultCallback = object : ResultCallback {

        override fun onComplete(result: ProjectDataModel) {
            val currentVersion = getCurrentVersion()

            if (result.versions != null && currentVersion != null) {
                val versionResult = buildVersionResult(result, currentVersion)
                appDeliveryInterface.onVersionCheckResult(versionResult)
            } else {
                val resultCode = VersionResultCode.ERROR
                resultCode.string = "Current version could not be found"
                val versionResult = VersionResult(resultCode)
                appDeliveryInterface.onVersionCheckResult(versionResult)
            }
        }

        override fun onFailure(error: String?) {
            val errorResultCode = VersionResultCode.ERROR
            error?.let { errorResultCode.string = it }
            val versionResult = VersionResult(errorResultCode, errorMessage = error)
            appDeliveryInterface.onVersionCheckResult(versionResult)
        }

    }

}



