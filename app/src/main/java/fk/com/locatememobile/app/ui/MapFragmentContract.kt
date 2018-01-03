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
        fun setToken(token: String)
    }

    interface Presenter {
        fun register(view: View)
    }
}