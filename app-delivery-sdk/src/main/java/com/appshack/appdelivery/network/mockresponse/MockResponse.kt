package com.appshack.appdelivery.network.mockresponse

import com.appshack.appdelivery.network.api.models.VersionStateResponse


/**
 * Created by joelbrostrom on 2019-02-20
 * Developed by App Shack
 */

/**
 * Mock data used until backend is up and running.
 */
class MOCKProvider {
    companion object {
        val projectDataModelResponse = VersionStateResponse(
                "This release solves bugs A-Z",
                2,
                "https://google.com",
                "1.0.3",
                4
        )
    }
}


