package fk.com.locatememobile.app.dagger

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import fk.com.locatememobile.app.data.rest.services.LocationService
import fk.com.locatememobile.app.data.rest.services.UserFriendsService
import fk.com.locatememobile.app.data.rest.services.UserService
import fk.com.locatememobile.app.Constants
import fk.com.locatememobile.app.data.Repository
import fk.com.locatememobile.app.data.rest.ServerRepository
import fk.com.locatememobile.app.data.db.RoomDatabase
import fk.com.locatememobile.app.data.rest.endpoints.LocationEndpoint
import fk.com.locatememobile.app.data.rest.endpoints.UserEndpoint
import fk.com.locatememobile.app.data.rest.endpoints.UserFriendsEndpoint
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    fun provideBaseUrl(): String {
        return Constants.SERVER_URL
    }

    @Provides
    @Singleton
    fun provideGsonConverter(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideAdapterFactory(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(10, TimeUnit.SECONDS).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, converter: Converter.Factory, callAdapter: CallAdapter.Factory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(converter)
                .addCallAdapterFactory(callAdapter)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideLocationService(retrofit: Retrofit): LocationService {
        return retrofit.create(LocationService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserFriendsService(retrofit: Retrofit): UserFriendsService {
        return retrofit.create(UserFriendsService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserEndpoint(userService: UserService): UserEndpoint {
        return UserEndpoint(userService)
    }

    @Provides
    @Singleton
    fun provideLocationEndpoint(locationService: LocationService): LocationEndpoint {
        return LocationEndpoint(locationService)
    }

    @Provides
    @Singleton
    fun provideUserFriendsEndpoint(userFriendsService: UserFriendsService): UserFriendsEndpoint {
        return UserFriendsEndpoint(userFriendsService)
    }

    @Provides
    @Singleton
    fun provideServerRepository(userEndpoint: UserEndpoint, locationEndpoint: LocationEndpoint, userFriendsEndpoint: UserFriendsEndpoint): ServerRepository {
        return ServerRepository(userEndpoint, locationEndpoint, userFriendsEndpoint)
    }

    @Provides
    @Singleton
    fun provideRoomDb(context: Context): RoomDatabase {
        return Room.databaseBuilder(context, RoomDatabase::class.java, "LocateMeDb").build()
    }

    @Provides
    @Singleton
    fun provideRepository(serverRepository: ServerRepository, roomDatabase: RoomDatabase): Repository {
        return Repository(serverRepository, roomDatabase)
    }
}