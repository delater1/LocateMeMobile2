package fk.com.locatememobile.app.ui.contracts

interface AddFriendFragmentContract {
    interface View {
        fun getFriendTag(): String
        fun getFriendAlias(): String
        fun showLoading()
        fun showSucces()
        fun showError(message: String)
    }

    interface Presenter {
        fun register(view: View)
        fun addButtonClicked()
    }
}