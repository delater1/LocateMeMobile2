package fk.com.locatememobile.app.dagger

import dagger.Component
import fk.com.locatememobile.app.device.Core
import fk.com.locatememobile.app.device.SharedPreferencesRepository
import fk.com.locatememobile.app.ui.fragments.AddFriendFragment
import fk.com.locatememobile.app.ui.fragments.MapFragment
import fk.com.locatememobile.app.ui.fragments.SettingsFragment
import fk.com.locatememobile.app.ui.fragments.StartFragment
import fk.com.locatememobile.app.ui.presenters.MapFragmentPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (DataModule::class)])
interface AppComponent {
    fun inject(target: MapFragment)
    fun inject(target: Core)
    fun inject(target: MapFragmentPresenter)
    fun inject(target: StartFragment)
    fun inject(target: AddFriendFragment)
    fun inject(target: SharedPreferencesRepository)
    fun inject(target: SettingsFragment)
}