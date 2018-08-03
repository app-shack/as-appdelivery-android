package com.appshack.appdelivery.network.api.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by joelbrostrom on 2018-07-27
 * Developed by App Shack
 */

data class UpdateResponseModel(

        @field:JsonProperty("data")
        val data: VersionDataModel? = null
)