package com.appshack.appdelivery.entity

/**
 * Created by joelbrostrom on 2018-08-07
 * Developed by App Shack
 */
enum class VersionResultCode(var string: String) {
    UPDATE_REQUIRED("Update Required"),
    UPDATE_AVAILABLE("Update Available"),
    UP_TO_DATE("Up To Date"),
    ERROR("ERROR")
}