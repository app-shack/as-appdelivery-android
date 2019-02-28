package com.appshack.appdelivery.logic

import com.appshack.appdelivery.entity.VersionResult
import com.appshack.appdelivery.entity.VersionResultCode
import com.appshack.appdelivery.interfaces.AppDeliveryInterface
import com.appshack.appdelivery.interfaces.ResultCallback
import com.appshack.appdelivery.network.api.models.VersionStateResponse
import com.appshack.appdelivery.network.api.parsers.ResponseParser
import com.appshack.appdelivery.network.api.requests.APIRequest
import com.appshack.appdelivery.network.api.requests.ApiRequestDetails
import com.appshack.appdelivery.network.api.requests.VersionStateRequest
import com.appshack.appdelivery.network.dispatchers.Dispatcher
import com.appshack.appdelivery.network.mockresponse.MOCKProvider

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
 * Constructor:
 * @param appInterface an object implementing the AppDeliveryInterface,
 *        usually the activity holding the AppDelivery object.
 * @param apiKey key provided by App Delivery backend admin.
 */
class AppDelivery(private val appInterface: AppDeliveryInterface, private val apiKey: String) {

    /**
     * Handles callback result when dispatcher receives a result from API-request.
     *
     * @onSuccess call buildVersionResult with the callback result as param.
     * The VersionResult is then sent to appInterface callback method,
     * from where the developer implementing this SDK have to handle the response.
     *
     * @onFailure appInterface is called with an Error versionResult.
     */
    private val onResultCallback: ResultCallback = object : ResultCallback {

        override fun onSuccess(result: VersionStateResponse) {
            val versionResult = buildVersionResult(result)
            appInterface.onVersionCheckResult(versionResult)
        }

        override fun onFailure(error: String?) {
            val errorResultCode = VersionResultCode.ERROR
            error?.let { errorResultCode.message = it }
            val versionResult = VersionResult(errorResultCode)
            appInterface.onVersionCheckResult(versionResult)
        }
    }

    /**
     * Sets up API-request and a dispatcher.
     * Calls the dispatchers dispatch().
     */
    fun startVersionCheck() {
        val packageDataModel = ApiRequestDetails(apiKey, appInterface.bundleId)
        val apiRequest: APIRequest = VersionStateRequest(packageDataModel)
        val dispatcher = Dispatcher()
        //onResultCallback.onSuccess(MOCKProvider.projectDataModelResponse) //TODO: Replace with live data
        dispatcher.dispatch(apiRequest, ResponseParser(onResultCallback)) //HttpOk call live data.
    }

    /**
     * Sets up and returns a VersionResult object.
     *
     * Compares min, max and current version codes.
     * Creates ResultCode with correct result enum.
     * Constructs and return a VersionCode with the above as arguments.
     *
     * @param versionStateResponse returned from api request to app delivery back end.
     * @param appInfo contains information about the appInterface Implementor.
     * @return VersionResult containing information of the current state and required actions.
     */
    internal fun buildVersionResult(versionStateResponse: VersionStateResponse): VersionResult {

        val currentVersionName = appInterface.versionName
        val maxVersionName = versionStateResponse.latestVersionName

        val currentVersionCode = appInterface.versionCode
        val minVersionCode = versionStateResponse.minVersionCode
        val maxVersionCode = versionStateResponse.latestVersionCode

        val isUpdateRequired = minVersionCode > currentVersionCode
        val isUpdateAvailable = maxVersionCode > currentVersionCode
        val resultCode = getVersionResultCode(isUpdateRequired, isUpdateAvailable)

        val downloadUrl = versionStateResponse.apkFile

        return VersionResult(
                resultCode,
                downloadUrl,
                currentVersionName,
                maxVersionName,
                currentVersionCode,
                minVersionCode,
                maxVersionCode
        )
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

}



