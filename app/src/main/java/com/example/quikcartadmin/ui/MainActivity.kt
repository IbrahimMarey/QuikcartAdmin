package com.example.quikcartadmin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.ActivityMainBinding
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController

    private val HOME_ITEM = R.id.home
    private val PRODUCTS_ITEM = R.id.all_products
    private val COUPONS_ITEM = R.id.all_coupons
    private val PROFILE_ITEM = R.id.profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavHost()
        binding.setUpBottomNavigation()
    }
    private fun ActivityMainBinding.setUpBottomNavigation() {

        val bottomNavigationItems = mutableListOf(
            CurvedBottomNavigation.Model(HOME_ITEM, getString(R.string.home), R.drawable.ic_home),
            CurvedBottomNavigation.Model(PRODUCTS_ITEM, getString(R.string.products), R.drawable.ic_products),
            CurvedBottomNavigation.Model(COUPONS_ITEM, getString(R.string.coupons), R.drawable.ic_coupons),
            CurvedBottomNavigation.Model(PROFILE_ITEM, getString(R.string.profile), R.drawable.ic_person),
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
    }
}