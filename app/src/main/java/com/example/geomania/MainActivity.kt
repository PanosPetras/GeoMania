package com.example.geomania

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.geomania.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Create a navController to show messages to the user
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        //Make sure that we update the text of the coins counter when the amount of coins is changed
        onCoinsChanged()
        User.onCoinsChanged = {
            onCoinsChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        User.onCoinsChanged = null
    }

    private fun onCoinsChanged(){
        findViewById<TextView>(R.id.hintsTV1).text = "${User.coins}"
    }
}