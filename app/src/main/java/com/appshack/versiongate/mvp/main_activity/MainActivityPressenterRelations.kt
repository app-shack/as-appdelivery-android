package com.appshack.versiongate.mvp.main_activity


/**
 * Created by joelbrostrom on 2018-08-03
 * Developed by App Shack
 */


interface MainActivityPresenterRelations {

    interface Activity {
        fun setTextViewText(text: String)
    }

    interface Presenter {
        fun checkForUpdates()
    }
}