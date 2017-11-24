package fk.com.locatememobile.app.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import fk.locateme.app.R


/**
 * A simple [Fragment] subclass.
 */
class FriendsSelectionFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_friends_selection, container, false)
    }

}// Required empty public constructor
