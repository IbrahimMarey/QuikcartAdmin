package com.example.quikcartadmin.ui

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.ActivityMainBinding
import com.example.quikcartadmin.helpers.NetworkChangeReceiver
import com.example.quikcartadmin.helpers.NetworkConnection
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import com.qamar.curvedbottomnaviagtion.gone
import com.qamar.curvedbottomnaviagtion.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    private val HOME_ITEM = R.id.home
    private val PRODUCTS_ITEM = R.id.all_products
    private val COUPONS_ITEM = R.id.allRulesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavHost()
        binding.setUpBottomNavigation()

        networkChangeReceiver = NetworkChangeReceiver(this)
        registerReceiver(networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }
    private fun ActivityMainBinding.setUpBottomNavigation() {

        val bottomNavigationItems = mutableListOf(
            CurvedBottomNavigation.Model(HOME_ITEM, getString(R.string.home), R.drawable.ic_home),
            CurvedBottomNavigation.Model(PRODUCTS_ITEM, getString(R.string.products), R.drawable.ic_products),
            CurvedBottomNavigation.Model(COUPONS_ITEM, getString(R.string.coupons), R.drawable.ic_coupons),
        )

        bottomNavigation.apply {
            bottomNavigationItems.forEach { add(it) }
            setOnClickMenuListener {
                navController.navigate(it.id)
            }
            show(HOME_ITEM)
            setupNavController(navController)
        }
    }
    private fun initNavHost() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Set the visibility of the bottom navigation to GONE
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.home && destination.id != R.id.all_products &&
                destination.id != R.id.allRulesFragment) {
                binding.bottomNavigation.gone()
            } else {
                binding.bottomNavigation.visible()
            }
        }
    }
}