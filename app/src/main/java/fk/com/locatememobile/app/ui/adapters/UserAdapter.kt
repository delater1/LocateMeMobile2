package fk.com.locatememobile.app.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO
import fk.com.locatememobile.app.ui.MarkerColors
import fk.com.locatememobile.app.ui.UserSelectedListener
import fk.locateme.app.R

class UserAdapter : RecyclerView.Adapter<UserViewHolder> {
    val userSelectedListener: UserSelectedListener
    var userColorList: List<Pair<UserFriendDTO, MarkerColors>>

    constructor(userSelectedListener: UserSelectedListener) : super() {
        userColorList = listOf()
        this.userSelectedListener = userSelectedListener
    }

    fun setUsersWithColors(usersWithColors: List<Pair<UserFriendDTO, MarkerColors>>) {
        userColorList = usersWithColors
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userFirstName.text = userColorList[position].first.manufacturer
        holder.userLastName.text = userColorList[position].first.device
        holder.userAvatar.setColorFilter(userColorList[position].second.color)
        holder.itemView.setOnClickListener({ userSelectedListener.onUserSelected(userColorList[position].first) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_view, parent, false))
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


