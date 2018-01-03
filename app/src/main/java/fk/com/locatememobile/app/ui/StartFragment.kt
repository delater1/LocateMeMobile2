package fk.com.locatememobile.app.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fk.com.locatememobile.app.App
import fk.locateme.app.R
import kotlinx.android.synthetic.main.fragment_start.*
import javax.inject.Inject

/**
 * Created by FK on 28-Dec-17.
 */
class StartFragment : Fragment(), StartFragmentContract.View {
    private val PERMISSIONS_REQUEST_FINE_LOCATION = 111

    @Inject
    lateinit var presenter: StartFragmentContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as App).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.register(this)
    }

    override fun showError(errorMessage: String) {
        hideProgressBar()
        showErrorText(errorMessage)
    }

    private fun showErrorText(errorMessage: String) {
        start_fragment_error_description.text = errorMessage
        start_fragment_error_description.setVisibility(View.VISIBLE)
    }

    private fun hideProgressBar() {
        start_fragment_progress_bar.visibility = View.GONE
    }

    private fun showProgressBar() {
        start_fragment_progress_bar.visibility = View.VISIBLE
    }

    override fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_FINE_LOCATION)
        } else {
            presenter.permissionGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_FINE_LOCATION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            presenter.permissionGranted()
        } else {
            presenter.permissionNotGranted()
        }
    }

    override fun showMapFragment() {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrame, MapFragment())
        fragmentTransaction.commit()
    }

}
