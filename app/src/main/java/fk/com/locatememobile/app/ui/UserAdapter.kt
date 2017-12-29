package fk.com.locatememobile.app.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import fk.com.locatememobile.app.data.entities.User
import fk.locateme.app.R

/**
 * Created by korpa on 20.11.2017.
 */
class UserAdapter : RecyclerView.Adapter<UserViewHolder> {
    val userSelectedListener: UserSelectedListener
    var userColorList: List<Pair<User, MarkerColors>>

    constructor(userSelectedListener: UserSelectedListener) : super() {
        userColorList = listOf()
        this.userSelectedListener = userSelectedListener
    }

    fun setUsersWithColors(usersWithColors: List<Pair<User,MarkerColors>>) {
        userColorList = usersWithColors
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userFirstName.text = userColorList[position].first.instanceId
        holder.userLastName.text = userColorList[position].first.device
        holder.userAvatar.setColorFilter(userColorList[position].second.color)
        holder.itemView.setOnClickListener({ userSelectedListener.onUserSelected(userColorList[position].first) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        var userView = LayoutInflater.from(parent.context).inflate(R.layout.user_view, parent, false)
        return UserViewHolder(userView)
    }

    override fun getItemCount() = userColorList.size
}

class UserViewHolder : RecyclerView.ViewHolder {
    constructor(view: View) : super(view) {
        userAvatar = view.findViewById(R.id.user_avatar)
        userFirstName = view.findViewById(R.id.user_first_name)
        userLastName = view.findViewById(R.id.user_last_name)
    }

    var userAvatar: ImageView
    var userFirstName: TextView
    var userLastName: TextView
}


