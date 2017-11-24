package fk.com.locatememobile.app.ui

import android.content.Context
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.device.Core
import fk.com.locatememobile.app.device.UserLocationUpdatesReceiver
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by korpa on 06.11.2017.
 */
class MapFragmentPresenter : UserLocationUpdatesReceiver {
    var applicationContext: Context
    var observableEmitter: ObservableEmitter<Location>? = null
    var core: Core

    @Inject
    constructor(applicationContext: Context, core: Core) {
        this.applicationContext = applicationContext
        this.core = core
        core.subscribeToLocationService(this)
    }

    override fun onUserLocationUpdate(location: Location) {
        observableEmitter?.onNext(location)
    }


    fun getLoactionObservable(): Observable<Location> {
        return Observable.create<Location> { e: ObservableEmitter<Location> -> observableEmitter = e }
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }
}