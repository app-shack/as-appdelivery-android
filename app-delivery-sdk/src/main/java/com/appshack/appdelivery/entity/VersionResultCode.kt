package com.appshack.appdelivery.entity

/**
 * Created by joelbrostrom on 2018-08-07
 * Developed by App Shack
 */

/**
 * Enum containing possible result outcomes.
 * This is what will be used by the implementor to determine cause of action.
 */
enum class VersionResultCode(var message: String) {
    UPDATE_REQUIRED("Update Required"),
    UPDATE_AVAILABLE("Update Available"),
    UP_TO_DATE("Up To Date"),
    ERROR("ERROR")
}