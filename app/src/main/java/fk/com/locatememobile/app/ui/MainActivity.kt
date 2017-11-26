package fk.com.locatememobile.app.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import fk.com.locatememobile.app.App
import fk.com.locatememobile.app.data.Repository
import fk.com.locatememobile.app.device.LocationResultService
import fk.com.locatememobile.app.device.LocationSubscriptionStateListener
import fk.locateme.app.R
import javax.inject.Inject

class MainActivity : AppCompatActivity(){
    private val PERMISSIONS_REQUEST_FINE_LOCATION = 111
    @Inject
    lateinit var repository: Repository
    @Inject
    lateinit var locationSubscribtionStateListener: LocationSubscriptionStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        setContentView(R.layout.activity)
        showStartingFragment()
        checkPermissionAndStartLocationService()
    }

    private fun showStartingFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrame, LoginFragment())
        fragmentTransaction.commit()
    }

    private fun checkPermissionAndStartLocationService() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_FINE_LOCATION)
        } else {
            startLocationService()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_FINE_LOCATION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationService()
        }
    }

    private fun startLocationService() {
        startService(Intent(applicationContext, LocationResultService::class.java))
        locationSubscribtionStateListener.onLocationServiceStarted()
    }
}

