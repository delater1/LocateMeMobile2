package fk.com.locatememobile.app.ui

/**
 * Created by korpa on 23.11.2017.
 */
interface LoginFragmentContract {
    interface View {
        fun onLoggedIn()
        fun onLogInError()
    }

    interface Presenter {
        fun register(view: LoginFragmentContract.View)
        fun logIn(firstName: String, lastName: String)
    }
}

