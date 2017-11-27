package fk.com.locatememobile.app.ui

import android.util.Log
import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.device.Core
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by korpa on 23.11.2017.
 */
class FriendsSelectionPresenter : FriendsSelectionContract.Presenter {
    val TAG = javaClass.simpleName
    val core: Core

    var view: FriendsSelectionContract.View? = null

    @Inject
    constructor(core: Core) {
        this.core = core
    }

    override fun register(view: FriendsSelectionContract.View) {
        this.view = view
    }

    override fun getUsers(): Single<List<User>> {
        return core.getUsers()
    }

    override fun getUserFriends(): Single<List<User>> {
        return core.getUserFriends()
    }

    override fun onForwardClicked() {
        view?.let {
            core.postNewFriendsUserList(it.getSelectedUserFriends()).subscribe(
                    { view?.showMapFragment() },
                    { e: Throwable -> Log.e(TAG, e.message) })
        }
    }
}
