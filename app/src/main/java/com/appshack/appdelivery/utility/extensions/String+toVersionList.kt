package com.appshack.appdelivery.utility.extensions


/**
 * Created by joelbrostrom on 2018-08-08
 * Developed by App Shack
 */

fun String.toVersionList() =
        this.split(".")
                .map { it.toInt() }
                .toMutableList()