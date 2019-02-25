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
     * Static object containing app constants.
     * @property baseUrl holds the base url for api requests.
     */
    companion object {
        const val baseUrl: String = "https://appdelivery.betashack.se"
    }
}