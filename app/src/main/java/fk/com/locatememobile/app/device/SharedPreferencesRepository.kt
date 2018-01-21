package fk.com.locatememobile.app.device

import android.content.Context
import android.preference.PreferenceManager
import fk.com.locatememobile.app.Constants.SharedPreferencesKeys.LOCATION_INTERVAL_KEY
import fk.com.locatememobile.app.Constants.SharedPreferencesKeys.USER_TOKEN_KEY
import javax.inject.Inject

class SharedPreferencesRepository {
    @Inject
    constructor(applicationContext: Context) {
        this.applicationContext = applicationContext
    }

    private val applicationContext: Context

    fun getUserToken(): String {
        val sharedPreferences =
                PreferenceManager
                        .getDefaultSharedPreferences(applicationContext)
        return sharedPreferences.getString(USER_TOKEN_KEY, "")
    }

    fun saveUserToken(token: String) {
        val sharedPreferencesEditor =
                PreferenceManager
                        .getDefaultSharedPreferences(applicationContext)
                        .edit()
        sharedPreferencesEditor.putString(USER_TOKEN_KEY, token)
        sharedPreferencesEditor.apply()
    }

    fun getLocationInterval(): Int {
        val sharedPreferences =
                PreferenceManager
                        .getDefaultSharedPreferences(applicationContext)
        return sharedPreferences.getInt(LOCATION_INTERVAL_KEY, 60)
    }

    fun saveLocationInterval(locationInterval: Int) {
        val sharedPreferencesEditor =
                PreferenceManager
                        .getDefaultSharedPreferences(applicationContext)
                        .edit()
        sharedPreferencesEditor.putInt(LOCATION_INTERVAL_KEY, 60)
        sharedPreferencesEditor.apply()
    }
}