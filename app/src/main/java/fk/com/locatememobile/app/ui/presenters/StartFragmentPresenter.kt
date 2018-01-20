package fk.com.locatememobile.app.ui.presenters

import android.util.Log
import fk.com.locatememobile.app.device.Core
import fk.com.locatememobile.app.ui.contracts.StartFragmentContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by FK on 28-Dec-17.
 */
class StartFragmentPresenter : StartFragmentContract.Presenter {
    constructor(core: Core) {
        this.core = core
    }

    val TAG = javaClass.simpleName
    val core: Core
    var view: StartFragmentContract.View? = null

    override fun register(view: StartFragmentContract.View) {
        this.view = view
        this.view?.checkPermissions()
    }

    override fun permissionNotGranted() {
        view?.showError("Permission for location has not been granted.\n We cannot do anything :(")
    }

    override fun permissionGranted() {
        core.startLocationService()
        core.logIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSetupReady() },
                        { error: Throwable ->
                            view?.showError("Something went wrong during connecting to server")
                            Log.e(TAG, error.message)
                        }
                )
    }

    override fun onSetupReady() {
        view?.showMapFragment()
    }
}