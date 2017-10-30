package fk.com.locatememobile.app

import android.app.Application
import fk.com.locatemedata.data.Repository


/**
 * Created by korpa on 30.10.2017.
 */

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this)
    }
}