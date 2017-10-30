package fk.com.locatemedata.data

import android.arch.persistence.room.Room
import android.content.Context
import fk.com.locatemedata.data.rest.endpoints.LocationEndpoint
import fk.com.locatemedata.data.rest.endpoints.UserEndpoint
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by korpa on 21.10.2017.
 */
object Repository {
    lateinit var db: RoomDatabase
    private lateinit var retrofit: Retrofit
    lateinit var userEndpoint: UserEndpoint
    lateinit var locationEndpoint: LocationEndpoint

    fun initialize(applicationContext: Context) {
        db =  Room.databaseBuilder(applicationContext, RoomDatabase::class.java, "LocateMeDb").build()
        retrofit = Retrofit.Builder().baseUrl("http://192.168.1.26:8181")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        userEndpoint = UserEndpoint(retrofit)
        locationEndpoint = LocationEndpoint(retrofit)
    }
}