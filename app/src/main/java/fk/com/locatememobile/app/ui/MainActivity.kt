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
import fk.com.locatememobile.app.device.DeviceLocationSubscriber
import fk.com.locatememobile.app.device.LocationResultService
import fk.locateme.app.R
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: Repository

    private val MY_PERMISSIONS_REQUEST_FINE_LOCATION = 111
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        setContentView(R.layout.activity)
        showMapFragment()
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION)
        } else {
            startLocationService()
        }
    }

    private fun showMapFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrame, MapFragment())
        fragmentTransaction.commit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION) {
            startLocationService()
        }
    }

    private fun startLocationService() {
        startService(Intent(applicationContext, LocationResultService::class.java))
        DeviceLocationSubscriber(applicationContext, repository).bindToLocationService()
    }
}