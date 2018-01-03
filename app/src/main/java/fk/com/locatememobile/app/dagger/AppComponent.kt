package fk.com.locatememobile.app.dagger

import dagger.Component
import fk.com.locatememobile.app.device.Core
import fk.com.locatememobile.app.device.SharedPreferencesRepository
import fk.com.locatememobile.app.ui.*
import javax.inject.Singleton

/**
 * Created by korpa on 04.11.2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, DataModule::class))
interface AppComponent {
    fun inject(target: MapFragment)
    fun inject(target: Core)
    fun inject(target: MapFragmentPresenter)
    fun inject(target: StartFragment)
    fun inject(target: SharedPreferencesRepository)
    fun inject(target: AddFriendFragment)
}