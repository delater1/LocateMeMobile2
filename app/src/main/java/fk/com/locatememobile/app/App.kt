package fk.com.locatememobile.app

import android.app.Application
import fk.com.locatememobile.app.dagger.AppComponent
import fk.com.locatememobile.app.dagger.AppModule
import fk.com.locatememobile.app.dagger.DaggerAppComponent
import fk.com.locatememobile.app.dagger.DataModule

class App: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
    }

    fun initDagger(application: Application):AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(application))
                .dataModule(DataModule())
                .build()
    }
}