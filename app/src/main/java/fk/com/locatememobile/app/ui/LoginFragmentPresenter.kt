package fk.com.locatememobile.app.ui

import fk.com.locatememobile.app.device.Core
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by korpa on 23.11.2017.
 */
class LoginFragmentPresenter : LoginFragmentContract.Presenter {
    @Inject
    constructor(core: Core) {
        this.core = core
    }

    val core: Core
    var view: LoginFragmentContract.View? = null


    override fun register(view: LoginFragmentContract.View) {
        this.view = view
    }

    override fun logIn(firstName: String, lastName: String) {
        core.logIn(firstName, lastName).subscribe(object : CompletableObserver {
            override fun onComplete() {
                view?.onLoggedIn()
            }

            override fun onSubscribe(d: Disposable?) {

            }

            override fun onError(e: Throwable?) {
                view?.onLogInError()
            }

        }
        )
    }

}