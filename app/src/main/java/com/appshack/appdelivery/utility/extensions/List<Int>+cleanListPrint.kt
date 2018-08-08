package com.appshack.appdelivery.utility.extensions


/**
 * Created by joelbrostrom on 2018-08-08
 * Developed by App Shack
 */


fun List<Int>.cleanListPrint() =
        this.toString()
                .replace("[", "")
                .replace("]", "")
                .replace(", ", ".")
                .trim()