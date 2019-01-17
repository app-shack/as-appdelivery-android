package com.appshack.appdelivery.utility.extensions


/**
 * Created by joelbrostrom on 2018-08-08
 * Developed by App Shack
 *
 * Purpose: Returns itself as an List of Integers.
 * String must follow the syntax "Int.Int.Int...Int"
 *
 * @example "1.22.3".toVersionList() == [1, 22, 3] //true
 */

fun String.toVersionList():MutableList<Int> =
        this.split(".")
                .map { it.toInt() }
                .toMutableList()