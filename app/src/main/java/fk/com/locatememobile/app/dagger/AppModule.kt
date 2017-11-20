package fk.com.locatememobile.app.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
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
}