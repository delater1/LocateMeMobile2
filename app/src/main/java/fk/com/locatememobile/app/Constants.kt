package fk.com.locatememobile.app

object Constants {
    val SERVER_URL = "http://locateMe.eu-central-1.elasticbeanstalk.com"

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