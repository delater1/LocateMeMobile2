package fk.com.locatememobile.app.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import fk.com.locatememobile.app.data.Repository
import fk.com.locatememobile.app.device.Core
import fk.com.locatememobile.app.device.LocationSubscriptionStateListener
import fk.com.locatememobile.app.ui.LoginFragmentContract
import fk.com.locatememobile.app.ui.LoginFragmentPresenter
import fk.com.locatememobile.app.ui.MapFragmentPresenter
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
    fun provideCore(applicationContext: Context, repository: Repository): Core {
        return Core(applicationContext, repository)
    }

    @Provides
    fun provideLocationSubscriptionStateListener(core: Core): LocationSubscriptionStateListener {
        return core
    }

    @Provides
    @Singleton
    fun provideMapFragmentPresenter(applicationContext: Context, core: Core): MapFragmentPresenter {
        return MapFragmentPresenter(applicationContext, core)
    }

    @Provides
    @Singleton
    fun provideLoginFragmentPresenter(core: Core): LoginFragmentContract.Presenter {
        return LoginFragmentPresenter(core)
    }

}