package fk.com.locatememobile.app.dagger

import dagger.Component
import fk.com.locatememobile.app.device.Core
import fk.com.locatememobile.app.ui.LoginFragment
import fk.com.locatememobile.app.ui.MainActivity
import fk.com.locatememobile.app.ui.MapFragment
import fk.com.locatememobile.app.ui.MapFragmentPresenter
import javax.inject.Singleton

/**
 * Created by korpa on 04.11.2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, DataModule::class))
interface AppComponent {
    fun inject(target: MapFragment)
    fun inject(target: MainActivity)
    fun inject(target: Core)
    fun inject(target: MapFragmentPresenter)
    fun inject(target: LoginFragment)
}