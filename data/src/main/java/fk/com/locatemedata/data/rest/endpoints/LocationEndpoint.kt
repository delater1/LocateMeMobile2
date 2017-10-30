package fk.com.locatemedata.data.rest.endpoints

import android.util.Log
import fk.com.locatemedata.data.rest.services.LocationService
import fk.com.locatemedata.data.entities.Location
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by korpa on 22.10.2017.
 */
class LocationEndpoint(retrofit: Retrofit, val locationService: LocationService = retrofit.create(LocationService::class.java)) {
    val TAG = this.javaClass.simpleName

    fun getUserLocationsSubscrition(userId: Long): Observable<List<Location>> {
        return Observable.interval(5, TimeUnit.SECONDS)
                .startWith(0)
                .flatMap { locationService.getUserLocations(userId) }
                .repeat()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun postUserLocation(location: Location) {
        locationService.addLocationForUser(1, location).subscribeOn(Schedulers.newThread()).subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "on subscribe")
            }

            override fun onComplete() {
                Log.d(TAG, "post user location complete")
            }


            override fun onError(e: Throwable) {
                Log.e(TAG, "on Error: ${e.message}")
            }
        })
    }
}