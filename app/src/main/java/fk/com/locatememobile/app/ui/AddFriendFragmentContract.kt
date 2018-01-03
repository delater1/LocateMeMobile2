package fk.com.locatememobile.app.ui

interface AddFriendFragmentContract {
    interface View {
        fun getFriendTag(): String
        fun getFriendAlias(): String
        fun showLoading()
        fun showSucces()
        fun showError(message: String)
    }

    interface Presenter {
        fun register(view: AddFriendFragmentContract.View)
        fun addButtonClicked()
    }
}