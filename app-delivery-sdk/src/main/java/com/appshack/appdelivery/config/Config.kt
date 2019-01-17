package com.appshack.appdelivery.config

/**
 * Created by joelbrostrom on 2018-07-27
 * Developed by App Shack
 */

/**
 * Purpose: Store base properties in a constant static field.
 */
class Config {

    /**
     * Static singleton containing app wide constants.
     * @property baseUrl holds the api base url.
     */
    companion object {
        const val baseUrl: String = "http://192.168.72.157:8000"
        //"192.168.72.38:8000" // simon
        //"http://10.0.2.2:8000" // local
    }
}