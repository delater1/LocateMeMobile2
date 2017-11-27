package fk.com.locatememobile.app.data.rest.endpoints

import android.util.Log
import fk.com.locatememobile.app.Constants
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.rest.services.LocationService
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by korpa on 22.10.2017.
 */
class LocationEndpoint(private val locationService: LocationService) {

    val TAG = this.javaClass.simpleName

    fun getLocationsSubscrition(userId: Long): Observable<List<Location>> {
        return Observable.interval(Constants.LOCATIONS_FROM_SERVER_INTERVAL, TimeUnit.SECONDS)
                .startWith(0)
                .flatMap { locationService.getUserFriendsLastLocations(userId) }
                .repeat()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun postUserLocation(location: Location) {
        locationService.addLocationForUser(location.userId, location).subscribeOn(Schedulers.newThread()).subscribe(object : CompletableObserver {
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