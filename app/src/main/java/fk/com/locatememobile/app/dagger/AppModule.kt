package fk.com.locatememobile.app.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import fk.com.locatememobile.app.data.Repository
import fk.com.locatememobile.app.device.Core
import fk.com.locatememobile.app.device.SharedPreferencesRepository
import fk.com.locatememobile.app.ui.contracts.AddFriendFragmentContract
import fk.com.locatememobile.app.ui.contracts.MapFragmentContract
import fk.com.locatememobile.app.ui.contracts.SettingsFragmentContract
import fk.com.locatememobile.app.ui.contracts.StartFragmentContract
import fk.com.locatememobile.app.ui.presenters.AddFriendFragmentPresenter
import fk.com.locatememobile.app.ui.presenters.MapFragmentPresenter
import fk.com.locatememobile.app.ui.presenters.SettingsFragmentPresenter
import fk.com.locatememobile.app.ui.presenters.StartFragmentPresenter
import javax.inject.Singleton


@Module
class AppModule(val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesRepository(applicationContext: Context):
            SharedPreferencesRepository {
        return SharedPreferencesRepository(applicationContext)
    }

    @Provides
    @Singleton
    fun provideCore(applicationContext: Context, repository: Repository,
                    sharedPreferencesRepository: SharedPreferencesRepository): Core {
        return Core(applicationContext, repository, sharedPreferencesRepository)
    }

    @Provides
    @Singleton
    fun provideMapFragmentPresenter(core: Core): MapFragmentContract.Presenter {
        return MapFragmentPresenter(core)
    }

    @Provides
    @Singleton
    fun provideStartFragmentPresenter(core: Core): StartFragmentContract.Presenter {
        return StartFragmentPresenter(core)
    }

    @Provides
    @Singleton
    fun provideAddFriendPresenter(core: Core): AddFriendFragmentContract.Presenter {
        return AddFriendFragmentPresenter(core)
    }

    @Provides
    @Singleton
    fun provideSettingsPresenter(core: Core, sharedPreferencesRepository: SharedPreferencesRepository): SettingsFragmentContract.Presenter {
        return SettingsFragmentPresenter(core, sharedPreferencesRepository)
    }
}