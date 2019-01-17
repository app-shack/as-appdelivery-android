package com.appshack.appdelivery.logic

import com.appshack.appdelivery.entity.VersionResult
import com.appshack.appdelivery.entity.VersionResultCode
import com.appshack.appdelivery.interfaces.AppDeliveryInterface
import com.appshack.appdelivery.interfaces.ResultCallback
import com.appshack.appdelivery.network.api.models.ProjectDataModel
import com.appshack.appdelivery.network.api.parsers.ResponseParser
import com.appshack.appdelivery.network.api.requests.APIRequest
import com.appshack.appdelivery.network.api.requests.PackageApiDetailsModel
import com.appshack.appdelivery.network.api.requests.VersionStatusRequest
import com.appshack.appdelivery.network.dispatchers.Dispatcher
import com.appshack.appdelivery.utility.extensions.compareTo
import com.appshack.appdelivery.utility.extensions.toVersionList

/**
 * Created by joelbrostrom on 2018-08-03
 * Developed by App Shack
 */

/**
 * Purpose: Make a REST request for project state, get local state and compare to se if app needs
 *          to be updated.
 *          If so, the user can be prompted to upgrade or the app can be locked down until the
 *          required minimum version is met.
 *
 * Init:
 * @param appDeliveryInterface an object implementing the AppDeliveryInterface,
 *        usually the activity holding the AppDelivery object.
 * @param apiKey the api key provided by App Delivery backend management.
 */
class AppDelivery(private val appDeliveryInterface: AppDeliveryInterface, private val apiKey: String) {

    /**
     * Sets up API-request and a dispatcher.
     * Calls the dispatchers dispatch().
     */
    fun startVersionCheck() {
        val packageDataModel = PackageApiDetailsModel(apiKey, getPackageName())
        val apiRequest: APIRequest = VersionStatusRequest(packageDataModel)
        val dispatcher = Dispatcher()
        dispatcher.dispatch(apiRequest, ResponseParser(onResultCallback))
    }

    /**
     * Sets up and returns a VersionResult object.
     *
     * Extract data from projectDataModel and covert strings to List<Int>.
     * Adjusts versions to make all Lists equal in length.
     * Check if updates are available/required.
     * Creates ResultCode with correct result enum.
     * Constructs and return a VersionCode with the above as arguments.
     *
     * @param projectDataModel returned from api request to app delivery back end.
     * @param deviceVersionName the current version running on device.
     *        This is passed as a param instead of fetched in method to enable testing.
     * @return VersionResult containing information of the current state and required actions.
     */
    internal fun buildVersionResult(projectDataModel: ProjectDataModel, deviceVersionName: String? = null)
            : VersionResult {

        val versionName = deviceVersionName ?: getDeviceVersionName()
        ?: return VersionResult(VersionResultCode.ERROR
                .apply { message = "Could not find current version on device." })

        val deviceVersion = versionName.toVersionList()
        val minVersion = projectDataModel.minVersion?.toVersionList() ?: mutableListOf()
        val recommendedVersion = projectDataModel.recommendedVersion?.toVersionList() ?: mutableListOf()

        val versions = listOf(deviceVersion, minVersion, recommendedVersion)
        val maxLength = getMaxLength(versions)
        adjustVersionLength(versions, maxLength)

        val isUpdateRequired = minVersion > deviceVersion
        val isUpdateAvailable = recommendedVersion > deviceVersion
        val resultCode = getVersionResultCode(isUpdateRequired, isUpdateAvailable)

        val downloadUrl = projectDataModel
                .versions
                ?.find { it.versionName?.toVersionList() == recommendedVersion }
                ?.apkFile

        return VersionResult(
                resultCode,
                downloadUrl,
                deviceVersion,
                minVersion,
                recommendedVersion)

    }

    /**
     * @return the project package name specified in the Manifest.
     */
    private fun getPackageName(): String? {
        return appDeliveryInterface.context?.packageName
    }

    /**
     * @return versionName specified in app build.gradle,
     * or null if sufficient context is missing.
     */
    internal fun getDeviceVersionName(): String? {
        return appDeliveryInterface.context?.packageManager
                ?.getPackageInfo(getPackageName(), 0)
                ?.versionName
    }

    /**
     * @param candidates a list containing list<Int> of various lengths.
     * @return the size of the longest List in a List of Lists.
     * @example used to determine the longest format of a list of versions split into Lists of Integers.
     *          For example. [ [1, 2, 0], [1, 2] ] would return 2.
     */
    internal fun getMaxLength(candidates: List<List<Int>>): Int {
        return candidates.maxBy { it.size }?.size ?: 0
    }

    /**
     * Pad versions with zeros to match the minLength argument.
     * The zeros are added to the end of the lists, so 1.2 becomes 1.2.0 if length is 3.
     * @param versions list of versions to be adjusted.
     * @param minLength value of minimum length.
     * @Return List with padded elements.
     */
    internal fun adjustVersionLength(versions: List<MutableList<Int>>, minLength: Int): List<MutableList<Int>> {
        for (version in versions) {
            while (version.size < minLength) {
                version.add(0)
            }
        }
        return versions
    }

    /**
     * Return VersionCode enum depending on app state.
     * if isUpdateRequired and isUpdateAvailable is both true only UPDATE_REQUIRED will be returned.
     * @param isUpdateRequired
     * @param isUpdateAvailable
     * @return VersionCode enum.
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
     * @onSuccess call buildVersionResult with the callback result as param.
     * The VersionResult is then sent to appDeliveryInterface callback method,
     * from where the developer implementing this SDK have to handle the response.
     *
     * @onFailure appDeliveryInterface is called with an Error versionResult.
     */
    private val onResultCallback: ResultCallback = object : ResultCallback {

        override fun onSuccess(result: ProjectDataModel) {
            val versionResult = buildVersionResult(result)
            appDeliveryInterface.onVersionCheckResult(versionResult)
        }

        override fun onFailure(error: String?) {
            val errorResultCode = VersionResultCode.ERROR
            error?.let { errorResultCode.message = it }
            val versionResult = VersionResult(errorResultCode)
            appDeliveryInterface.onVersionCheckResult(versionResult)
        }

    }

}



