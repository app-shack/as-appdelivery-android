package com.appshack.appdelivery.interfaces

import com.appshack.appdelivery.network.api.models.VersionStateResponse


/**
 * Created by joelbrostrom on 2018-08-08
 * Developed by App Shack
 *
 * Purpose: contract to have a callback response.
 */
interface ResultCallback {
    fun onSuccess(result: VersionStateResponse)
    fun onFailure(error: String?)
}