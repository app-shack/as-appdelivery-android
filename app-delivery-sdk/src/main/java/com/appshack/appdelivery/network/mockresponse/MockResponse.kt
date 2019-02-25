package com.appshack.appdelivery.network.mockresponse

import com.appshack.appdelivery.network.api.models.ProjectDataModel
import com.appshack.appdelivery.network.api.models.VersionDataModel


/**
 * Created by joelbrostrom on 2019-02-20
 * Developed by App Shack
 */

/**
 * Mock data used until backend is up and running.
 */
class MOCKProvider {
    companion object {
        val projectDataModelResponse = ProjectDataModel(
                2,
                "1.0.2",
                VersionDataModel(4,
                        "1.0.3",
                        "This release solves bugs A-Z",
                        "https://google.com"))
    }
}


