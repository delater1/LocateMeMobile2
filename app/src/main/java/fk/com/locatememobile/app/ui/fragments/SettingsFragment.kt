package fk.com.locatememobile.app.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import fk.com.locatememobile.app.App
import fk.com.locatememobile.app.ui.contracts.SettingsFragmentContract
import fk.locateme.app.R
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : Fragment(), SettingsFragmentContract.View {
    @Inject
    lateinit var presenter: SettingsFragmentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity.application as App).appComponent.inject(this)
        setOnClickListeners()
        presenter.register(this)
    }

    private fun setOnClickListeners() {
        settings_save_button.setOnClickListener {
            presenter.onSave(settings_location_interval_spinner.selectedItemPosition,
                    settings_friends_location_updates_spinner.selectedItemPosition)
        }
        settings_close_button.setOnClickListener { popFragment() }
    }

    override fun setPossibleIntervals(possibleIntervals: Array<Int>) {
        val adaper = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, possibleIntervals.map { milisToMinutesString(it)})
        settings_location_interval_spinner.adapter = adaper
        settings_friends_location_updates_spinner.adapter = adaper
    }

    override fun setSelectedValues(updatesIntervalIndex: Int, friendsUpdatesIntervalIndex: Int) {
        if (friendsUpdatesIntervalIndex != -1) {
            settings_location_interval_spinner.setSelection(updatesIntervalIndex)
        }
        if (friendsUpdatesIntervalIndex != -1) {
            settings_friends_location_updates_spinner.setSelection(updatesIntervalIndex)
        }
    }

    override fun popFragment() {
        activity.supportFragmentManager.popBackStackImmediate()
    }

    private fun milisToMinutesString(milis: Int): String {
        return (milis/ (60 * 1000)).toString() + " min"
    }
}
