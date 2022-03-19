package com.emissa.apps.rockclassicpop

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.emissa.apps.rockclassicpop.databinding.ActivityMainBinding
import com.emissa.apps.rockclassicpop.di.DaggerMusicsComponent
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

//    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var bottomNavBar: BottomNavigationView
//    private var mBinding: ActivityMainBinding? = null
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DaggerMusicsComponent.create().inject(this)

        bottomNavBar = binding.bottomNav

        navController = findNavController(R.id.main_frag_container)
        setupActionBarWithNavController(navController)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavBar.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}