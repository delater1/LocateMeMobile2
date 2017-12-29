package fk.com.locatememobile.app.ui

/**
 * Created by FK on 28-Dec-17.
 */
class StartFragmentPresenter: StartFragmentContract.Presenter {
    var view: StartFragmentContract.View? = null

    override fun register(view: StartFragmentContract.View) {
        this.view = view
    }

    override fun permissionNotGranted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun permissionGranted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSetupReady() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}