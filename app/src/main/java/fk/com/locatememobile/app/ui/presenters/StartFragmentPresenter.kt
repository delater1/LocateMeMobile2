package fk.com.locatememobile.app.ui.presenters

import android.util.Log
import fk.com.locatememobile.app.device.Core
import fk.com.locatememobile.app.ui.contracts.StartFragmentContract
import fk.com.locatememobile.app.ui.presenters.StartFragmentPresenterErrorType.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StartFragmentPresenter : StartFragmentContract.Presenter {

    constructor(core: Core) {
        this.core = core
    }

    val TAG = javaClass.simpleName
    val core: Core
    var view: StartFragmentContract.View? = null
    var errorType: StartFragmentPresenterErrorType = StartFragmentPresenterErrorType.NONE

    override fun register(view: StartFragmentContract.View) {
        this.view = view
        this.view?.checkPermissions()
    }

    override fun permissionNotGranted() {
        view?.showError("Permission for location has not been granted.\n We cannot do anything :(")
        errorType = PERMISSION_NOT_GRANTED
    }

    override fun permissionGranted() {
        core.startLocationService()
        login()
    }

    private fun login() {
        core.logIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSetupReady() },
                        { error: Throwable ->
                            view?.showError("Something went wrong during connecting to server")
                            errorType = COULD_NOT_CONNECT_TO_SERVER
                            Log.e(TAG, error.message)
                        }
                )
    }

    override fun onSetupReady() {
        view?.showMapFragment()
    }

    override fun onRetry() {
        when (errorType) {
            PERMISSION_NOT_GRANTED -> view?.askForPermission()
            COULD_NOT_CONNECT_TO_SERVER -> login()
            NONE -> Log.i(TAG, "On retry with NONE as error type")
        }
    }
}

enum class StartFragmentPresenterErrorType {
    PERMISSION_NOT_GRANTED,
    COULD_NOT_CONNECT_TO_SERVER,
    NONE
}

