package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Align with findViewById approach
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController: NavController = findNavController(R.id.nav_host_fragment_activity_main)

        // Set up AppBar configuration for top-level destinations
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_community,
                R.id.navigation_route,
                R.id.navigation_home,
                R.id.navigation_crimedata,
                R.id.navigation_profile
            )
        )

        // Setup ActionBar with NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        // Set up Bottom Navigation with NavController
        NavigationUI.setupWithNavController(navView, navController)
    }
}
