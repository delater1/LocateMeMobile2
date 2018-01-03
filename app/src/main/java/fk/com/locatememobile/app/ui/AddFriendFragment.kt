package fk.com.locatememobile.app.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.EditText
import com.transitionseverywhere.*
import fk.com.locatememobile.app.App
import fk.locateme.app.R
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_add_friend.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class AddFriendFragment : Fragment(), AddFriendFragmentContract.View {
    val waitTimeAfterSucces = 2000L
    @Inject
    lateinit var presenter: AddFriendFragmentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_add_friend, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity.application as App).appComponent.inject(this)
        presenter.register(this)
        add_friend_fragment_add_button.setOnClickListener { presenter.addButtonClicked() }
        add_friend_close_button.setOnClickListener { activity.supportFragmentManager.popBackStackImmediate() }
    }

    override fun showLoading() {
        showProgressBar()
        disableViews()
    }

    private fun showProgressBar() {
        add_friend_progress_bar.visibility = View.VISIBLE
    }

    override fun showError(message: String) {

    }

    private fun disableViews() {
        add_friend_fragment_add_button.isEnabled = false
        disableEditText(add_friend_fragment_friend_tag_editText)
        disableEditText(add_friend_fragment_friend_alias_editText)
    }

    private fun disableEditText(editText: EditText) {
        editText.isEnabled = false
        editText.isFocusable = false
        editText.isFocusableInTouchMode = false
    }

    override fun showSucces() {
        hideProgressBar()
        showSuccesIcon()
    }

    private fun showSuccesIcon() {
        TransitionManager.beginDelayedTransition(add_friend_fragment_main_view,
                TransitionSet()
                        .addTransition(Slide(Gravity.RIGHT).setInterpolator(FastOutLinearInInterpolator())
                                .setDuration(1000)))
        add_friend_succes_icon.visibility = View.VISIBLE
        waitAndPopFragment()
    }

    private fun waitAndPopFragment() {
        Completable.timer(waitTimeAfterSucces, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    activity.supportFragmentManager.popBackStackImmediate()
                }
    }

    private fun hideProgressBar() {
        add_friend_progress_bar.visibility = View.GONE
    }

    override fun getFriendTag(): String {
        return add_friend_fragment_friend_tag_editText.text.toString()
    }

    override fun getFriendAlias(): String {
        return add_friend_fragment_friend_alias_editText.text.toString()
    }
}
