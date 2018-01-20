package fk.com.locatememobile.app.ui.contracts

/**
 * Created by FK on 28-Dec-17.
 */
interface StartFragmentContract {
    interface View {
        fun showError(errorMessage: String)
        fun checkPermissions()
        fun showMapFragment()
    }

    interface Presenter {
        fun register(view: View)
        fun permissionNotGranted()
        fun permissionGranted()
        fun onSetupReady()
    }
}