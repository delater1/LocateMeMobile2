package fk.com.locatememobile.app

/**
 * Created by korpa on 04.11.2017.
 */
object Constants {
    val SERVER_URL = "http://192.168.1.11:8080"

    object SharedPreferencesKeys {
        val USER_TOKEN_KEY = "user_token"
        val LOCATION_INTERVAL_KEY = "location_key"
    }

    object IntentExtrasKeys {
        val LATIDUTE_KEY = "latitude"
        val LONGITUDE_KEY = "longitude"
        val ACCURACY = "accuracy"
        val LOCATION_ACTION_KEY = "fk.com.location"
        val LOCATION_UPDATE_INTERVAL = "location_update_interval"
        val TIME_KEY = "time"
    }
}