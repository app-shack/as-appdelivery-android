package com.appshack.appdelivery.mvp.main


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