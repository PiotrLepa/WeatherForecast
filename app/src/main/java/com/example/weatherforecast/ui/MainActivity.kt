package com.example.weatherforecast.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.example.weatherforecast.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            return item.onNavDestinationSelected(findNavController(R.id.navHostFragment))
                    || super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}
