package com.amir.roomdemo

import android.app.Application

//in order to get access to DAO we need to setup the application

class EmployeeApp : Application() {
    /**we set up the db in there.
     *we create it lazily
     * lazily means: it loads the needed values to our variable whenever it is needed
     * db is an instance of EmployeeDatabase instance
     * we can have just one instance
     */
    /**
     * the getInstance needs a context as argument, so we passing the applicationContext as the context(this refers to extended Application)
     * we need to setup the application, accordingly we set it up inside of manifest in application
     * not inside of an activity or anything else because we ae not using db owning this activity,
     * we are using db in entire(whole) application
     */
    //then we go to activity where we need the DAO(here we go to mainActivity to call this method)
    val db by lazy {
        EmployeeDatabase.getInstance(this)
    }

}