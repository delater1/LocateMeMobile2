package fk.com.locatememobile.app.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fk.com.locatememobile.app.ui.fragments.StartFragment
import fk.locateme.app.R

class Activity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)
        showStartingFragment()
    }

    private fun showStartingFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrame, StartFragment())
        fragmentTransaction.commit()
    }
}

