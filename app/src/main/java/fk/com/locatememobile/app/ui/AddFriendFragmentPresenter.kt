package fk.com.locatememobile.app.ui

import android.util.Log
import fk.com.locatememobile.app.device.Core
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddFriendFragmentPresenter : AddFriendFragmentContract.Presenter {
    val TAG = javaClass.simpleName
    @Inject
    constructor(core: Core) {
        this.core = core
    }

    val core: Core
    var view: AddFriendFragmentContract.View? = null

    override fun register(view: AddFriendFragmentContract.View) {
        this.view = view
    }

    override fun addButtonClicked() {
        view?.let {
            core.addUserFriend(it.getFriendTag(), it.getFriendAlias())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { view?.showSucces() },
                            { error: Throwable ->
                                view?.showError("User with provided tag doesn't exists.")
                                Log.e(TAG, error.message)
                            })
            view?.showLoading()
        }
    }

}