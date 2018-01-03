package fk.com.locatememobile.app.ui

import fk.com.locatememobile.app.device.Core
import javax.inject.Inject

/**
 * Created by korpa on 06.11.2017.
 */
class MapFragmentPresenter : MapFragmentContract.Presenter {
    val TAG = javaClass.simpleName
    val core: Core
    var view: MapFragmentContract.View? = null

    @Inject
    constructor(core: Core) {
        this.core = core
    }

    override fun register(view: MapFragmentContract.View) {
        this.view = view
        view.setToken(core.getUserToken())
    }
}
