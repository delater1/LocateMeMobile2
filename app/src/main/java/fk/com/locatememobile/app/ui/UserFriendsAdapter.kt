package fk.com.locatememobile.app.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import fk.com.locatememobile.app.data.entities.User
import fk.locateme.app.R

/**
 * Created by korpa on 25.11.2017.
 */
class UserFriendsAdapter : ArrayAdapter<User> {
    val userList: List<User>
    val mutableUserList: MutableList<User>

    constructor(context: Context, userList: List<User>, userFriendsList: List<User>) : super(context, -1, userList) {
        this.userList = userList
        this.mutableUserList = userFriendsList.toMutableList()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View? = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.user_friends_row, parent, false)
            view?.let {
                val firstNameTextView = it.findViewById<TextView>(R.id.user_friends_row_first_name)
                val lastNameTextView = it.findViewById<TextView>(R.id.user_friends_row_last_name)
                val userFriendCheckBox = it.findViewById<CheckBox>(R.id.user_friends_row_is_user_friend_check_box)

                firstNameTextView.text = userList[position].firstName
                lastNameTextView.text = userList[position].lastName

                userFriendCheckBox.setOnClickListener(null)
                userFriendCheckBox.isChecked = isUserInFriendList(userList[position])
                userFriendCheckBox.setOnCheckedChangeListener(onCheckedChangeListener(position))
            }
        }
        return view
    }

    private fun onCheckedChangeListener(position: Int): CompoundButton.OnCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { _, isChecked: Boolean ->
                if (isChecked)
                    mutableUserList.add(userList[position])
                else
                    mutableUserList.remove(userList[position])
            }

    private fun isUserInFriendList(user: User): Boolean {
        return mutableUserList.find { u -> u == user } != null
    }
}


