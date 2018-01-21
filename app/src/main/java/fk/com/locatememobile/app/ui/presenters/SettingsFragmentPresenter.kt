package fk.com.locatememobile.app.ui.presenters

import fk.com.locatememobile.app.device.Core
import fk.com.locatememobile.app.device.SharedPreferencesRepository
import fk.com.locatememobile.app.ui.contracts.SettingsFragmentContract
import javax.inject.Inject

class SettingsFragmentPresenter : SettingsFragmentContract.Presenter {
    @Inject
    constructor(core: Core, sharedPreferencesRepository: SharedPreferencesRepository) {
        this.core = core
        this.sharedPreferencesRepository = sharedPreferencesRepository
    }

    val core: Core
    val sharedPreferencesRepository: SharedPreferencesRepository
    var view: SettingsFragmentContract.View? = null
    val possibleIntervals = arrayOf(minutesToMilis(1), minutesToMilis(2), minutesToMilis(3), minutesToMilis(5))

    override fun register(view: SettingsFragmentContract.View) {
        this.view = view
        view.setPossibleIntervals(possibleIntervals)
        view.setSelectedValues(getSelectedValueIndex(sharedPreferencesRepository.getLocationInterval()),
                getSelectedValueIndex(sharedPreferencesRepository.getFriendsLocationInterval()))
    }

    private fun getSelectedValueIndex(locationInterval: Int): Int {
        return possibleIntervals.indexOfFirst { it == locationInterval }
    }

    override fun onSave(locationInterval: Int, friendsLocationInterval: Int) {
        sharedPreferencesRepository.saveFriendsLocationInterval(possibleIntervals[friendsLocationInterval])
        sharedPreferencesRepository.saveLocationInterval(possibleIntervals[locationInterval])
        core.restartService()
        view?.popFragment()
    }

    private fun minutesToMilis(minutes: Int): Int {
        return minutes * 60 * 1000
    }
}