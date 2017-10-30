package fk.com.locatemedata.data.rest.services

import fk.com.locatemedata.data.entities.Location
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by korpa on 16.10.2017.
 */
interface LocationService {
    @GET("user/{userId}/location")
    fun getUserLocations(@Path("userId") userId:Long): Observable<List<Location>>


    @POST("user/{userId}/location")
    fun addLocationForUser(@Path("userId") userId: Long, @Body location: Location): Completable
}