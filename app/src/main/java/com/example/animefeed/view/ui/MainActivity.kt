package com.example.animefeed.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.animefeed.R
import com.example.animefeed.databinding.ActivityMainBinding
import com.example.animefeed.db.AnimeDatabase
import com.example.animefeed.repository.AnimeRepository
import com.example.animefeed.view.ui.viewmodel.AnimeViewModel
import com.example.animefeed.view.ui.viewmodel.AnimeViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var animeViewModel: AnimeViewModel


    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        toolbar = findViewById<View>(R.id.tool_bar) as Toolbar
        toolbar.setTitleTextColor(resources.getColor(R.color.primary_dark))
        setSupportActionBar(toolbar)

        val animeRepository = AnimeRepository(AnimeDatabase(this))
        val animeViewModelFactory = AnimeViewModelFactory(application,animeRepository)
        animeViewModel = ViewModelProvider(this, animeViewModelFactory)[AnimeViewModel::class.java]

        bottomNavigation()
    }


    private fun bottomNavigation() {
        appBarConfiguration = AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.searchFragment,
        ).build()

        val navHostFragment= supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController= navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }



}