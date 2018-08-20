package com.appshack.appdelivery.utility.extensions


/**
 * Created by joelbrostrom on 2018-08-08
 * Developed by App Shack
 *
 * Purpose: Returns itself as an List of Integers.
 * Requires the string to follow the syntax "Int.Int.Int...Int"
 */

fun String.toVersionList():MutableList<Int> =
        this.split(".")
                .map { it.toInt() }
                .toMutableList()