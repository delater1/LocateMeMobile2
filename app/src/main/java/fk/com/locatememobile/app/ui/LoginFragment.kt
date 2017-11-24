package fk.com.locatememobile.app.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import fk.com.locatememobile.app.App
import fk.locateme.app.R

import kotlinx.android.synthetic.main.fragment_login.*
import java.time.Duration
import javax.inject.Inject

class LoginFragment : Fragment(), LoginFragmentContract.View {
    @Inject
    lateinit var presenter: LoginFragmentContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as App).appComponent.inject(this)
        presenter.register(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.register(this)
        login_fragment_log_in_button.setOnClickListener {
            presenter.logIn(login_fragment_first_name_edit_text.text.toString(), login_fragment_last_name_edit_text.text.toString())
            showLoading()
        }
    }

    fun showLoading() {
        login_fragment_progress_bar.visibility = View.VISIBLE
        login_fragment_first_name_edit_text.isFocusable = false
        login_fragment_last_name_edit_text.isFocusable = false
        login_fragment_log_in_button.isEnabled = false
    }

    override fun onLoggedIn() {
        hideLoading()
        showMapFragment()
    }

    private fun showMapFragment() {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrame, MapFragment())
        fragmentTransaction.commit()
    }

    private fun hideLoading() {
        login_fragment_progress_bar.visibility = View.GONE
        login_fragment_first_name_edit_text.isFocusable = true
        login_fragment_last_name_edit_text.isFocusable = true
        login_fragment_log_in_button.isEnabled = true
    }

    override fun onLogInError() {
        hideLoading()
        Toast.makeText(this.activity, "Something went wrong could login", Toast.LENGTH_LONG).show()
    }

}