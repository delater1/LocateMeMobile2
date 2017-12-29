package fk.com.locatememobile.app.device

import android.content.Context
import android.content.Intent
import android.util.Log
import fk.com.locatememobile.app.Constants
import fk.com.locatememobile.app.data.Repository
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Core {
    val TAG = javaClass.simpleName
    private val applicationContext: Context
    private val repository: Repository
    private val sharedPreferencesRepository: SharedPreferencesRepository

    @Inject
    constructor(applicationContext: Context, repository: Repository, sharedPreferencesRepository: SharedPreferencesRepository) {
        this.applicationContext = applicationContext
        this.repository = repository
        this.sharedPreferencesRepository = sharedPreferencesRepository
    }

    fun startLocationService() {
        applicationContext.startService(getLocationServiceIntent())
    }

    fun getLocationServiceIntent(): Intent {
        val intent = Intent(applicationContext, LocationService::class.java)
        intent.putExtra(Constants.IntentExtrasKeys.LOCATION_UPDATE_INTERVAL, 60000)
        return intent
    }

    fun logIn() {
        val token = sharedPreferencesRepository.getUserToken()
        if (token.isEmpty()) {
            repository.logInUser()
        }
    }
}