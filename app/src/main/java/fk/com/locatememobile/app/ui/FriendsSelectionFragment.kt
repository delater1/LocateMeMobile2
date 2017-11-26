package fk.com.locatememobile.app.ui


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fk.com.locatememobile.app.App
import fk.com.locatememobile.app.data.entities.User
import fk.locateme.app.R
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_friends_selection.*
import javax.inject.Inject

class FriendsSelectionFragment : Fragment(), FriendsSelectionContract.View {
    private val TAG = javaClass.simpleName

    private var userFriendsAdapter: UserFriendsAdapter? = null
    @Inject
    lateinit var presenter: FriendsSelectionContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as App).appComponent.inject(this)
        presenter.register(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_friends_selection, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        friends_selection_forward_button.setOnClickListener(getOnForwardClickListener())
        getAllUsersAsync().subscribe(
                { res: Pair<List<User>, List<User>> ->
                    userFriendsAdapter = UserFriendsAdapter(context, res.first, res.second)
                    friends_selection_fragment_list_view.adapter = userFriendsAdapter
                },
                { error: Throwable ->
                    Snackbar.make(friends_selection_fragment_main_view,
                            "Something went wrong while getting list of users",
                            Snackbar.LENGTH_LONG).show()
                }
        )
    }

    private fun getOnForwardClickListener(): View.OnClickListener {
        return View.OnClickListener { presenter.onForwardClicked() }
    }

    private fun getAllUsersAsync(): Single<Pair<List<User>, List<User>>> {
        return Single.zip(presenter.getUsers(), presenter.getUserFriends(), BiFunction { x, y -> Pair(x, y) })
    }

    override fun getSelectedUserFriends(): List<User>? {
        userFriendsAdapter?.let { return it.mutableUserList.toList() }
        return null
    }

    override fun showMapFragment() {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrame, MapFragment())
        fragmentTransaction.commit()
    }
}
