package fk.com.locatememobile.app.dagger

import dagger.Component
import fk.com.locatememobile.app.ui.MainActivity
import fk.com.locatememobile.app.ui.MapFragment
import javax.inject.Singleton

/**
 * Created by korpa on 04.11.2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, DataModule::class))
interface AppComponent {
    fun inject(mapFragment: MapFragment)
    fun inject(target: MainActivity)
}