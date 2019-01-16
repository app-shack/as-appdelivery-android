package com.appshack.appdelivery.interfaces

import com.appshack.appdelivery.network.api.models.ProjectDataModel


/**
 * Created by joelbrostrom on 2018-08-08
 * Developed by App Shack
 *
 * Purpose: contract to have a callback response.
 */
interface ResultCallback {
    fun onComplete(result: ProjectDataModel)
    fun onFailure(error: String?)
}