package com.mog.bondoman

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mog.bondoman.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.appBarMain.toolbar)
//
//        val navHostFragment =
//            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?)!!
//        val navController = navHostFragment.navController
//
//        binding.navView?.let {
//            appBarConfiguration = AppBarConfiguration(
//                setOf(
//                    R.id.nav_transaction, R.id.nav_scan, R.id.nav_graf, R.id.nav_settings
//                ),
//                binding.drawerLayout
//            )
//            setupActionBarWithNavController(navController, appBarConfiguration)
//            it.setupWithNavController(navController)
//        }
//
//        binding.appBarMain.contentMain.bottomNavView?.let {
//            appBarConfiguration = AppBarConfiguration(
//                setOf(
//                    R.id.nav_transaction, R.id.nav_scan, R.id.nav_graf
//                )
//            )
//            setupActionBarWithNavController(navController, appBarConfiguration)
//            it.setupWithNavController(navController)
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val result = super.onCreateOptionsMenu(menu)
//        // Using findViewById because NavigationView exists in different layout files
//        // between w600dp and w1240dp
//        val navView: NavigationView? = findViewById(R.id.nav_view)
//        if (navView == null) {
//            // The navigation drawer already has the items including the items in the overflow menu
//            // We only inflate the overflow menu if the navigation drawer isn't visible
//            menuInflater.inflate(R.menu.overflow, menu)
//        }
//        return result
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.nav_settings -> {
//                val navController = findNavController(R.id.nav_host_fragment_content_main)
//                navController.navigate(R.id.nav_settings)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}