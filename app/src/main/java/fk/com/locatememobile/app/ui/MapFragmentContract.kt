package fk.com.locatememobile.app.ui

import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import io.reactivex.Flowable
import io.reactivex.Observable


/**
 * Created by korpa on 26.11.2017.
 */

interface MapFragmentContract {
    interface View {
    }

    interface Presenter {
        fun getLocationObservable(): Observable<Location>
        fun getUserFriends(): Flowable<List<User>>
    }
}