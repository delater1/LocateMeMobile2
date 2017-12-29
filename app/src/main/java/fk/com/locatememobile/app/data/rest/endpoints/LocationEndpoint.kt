package fk.com.locatememobile.app.data.rest.endpoints

import android.util.Log
import fk.com.locatememobile.app.Constants
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.rest.services.LocationDTO
import fk.com.locatememobile.app.data.rest.services.LocationService
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class LocationEndpoint(private val locationService: LocationService) {

    val TAG = this.javaClass.simpleName

    fun getLocationDTOSubscription(userId: Long, interval: Long): Observable<List<LocationDTO>> {
        return Observable.interval(interval, TimeUnit.SECONDS)
                .startWith(0)
                .flatMap { locationService.getUserFriendsLastLocations(userId) }
                .repeat()
    }


    fun postUserLocation(location: Location) {
        locationService.addLocationForUser(location.userId, location)
                .subscribe(object : CompletableObserver {
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