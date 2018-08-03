package com.appshack.appdelivery.mvp.main

import android.content.pm.PackageInfo


/**
 * Created by joelbrostrom on 2018-08-03
 * Developed by App Shack
 */


interface MainActivityPresenterRelations {

    interface Activity {
        var packageInformation: PackageInfo
        fun setTextViewText(text: String)
    }

    interface Presenter {
        fun checkForUpdates()
    }
}