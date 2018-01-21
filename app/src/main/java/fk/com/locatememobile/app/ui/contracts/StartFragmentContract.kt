package fk.com.locatememobile.app.ui.contracts

interface StartFragmentContract {
    interface View {
        fun showError(errorMessage: String)
        fun checkPermissions()
        fun showMapFragment()
        fun askForPermission()
    }

    interface Presenter {
        fun register(view: View)
        fun permissionNotGranted()
        fun permissionGranted()
        fun onSetupReady()
        fun onRetry()
    }
}