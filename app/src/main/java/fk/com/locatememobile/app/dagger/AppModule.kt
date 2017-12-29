package fk.com.locatememobile.app.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import fk.com.locatememobile.app.data.Repository
import fk.com.locatememobile.app.device.Core
import fk.com.locatememobile.app.device.LocationSubscriptionStateListener
import fk.com.locatememobile.app.device.SharedPreferencesRepository
import fk.com.locatememobile.app.ui.*
import javax.inject.Singleton

/**
 * Created by korpa on 04.11.2017.
 */
@Module
class AppModule(val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesRepository(applicationContext: Context): SharedPreferencesRepository {
        return SharedPreferencesRepository(applicationContext)
    }

    @Provides
    @Singleton
    fun provideCore(applicationContext: Context, repository: Repository): Core {
        return Core(applicationContext, repository)
    }

    @Provides
    fun provideLocationSubscriptionStateListener(core: Core): LocationSubscriptionStateListener {
        return core
    }

    @Provides
    @Singleton
    fun provideMapFragmentPresenter(core: Core): MapFragmentContract.Presenter {
        return MapFragmentPresenter(core)
    }
}