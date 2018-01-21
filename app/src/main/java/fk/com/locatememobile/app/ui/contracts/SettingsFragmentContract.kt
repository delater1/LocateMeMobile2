package fk.com.locatememobile.app.ui.contracts

interface SettingsFragmentContract {
    interface Presenter {
        fun register(view: SettingsFragmentContract.View)
        fun onSave(locationInterval: Int, friendsLocationInterval: Int)
    }

    interface View {
        fun setPossibleIntervals(possibleIntervals: Array<Int>)
        fun popFragment()
        fun setSelectedValues(updatesIntervalIndex: Int, friendsUpdatesIntervalIndex: Int)
    }
}
