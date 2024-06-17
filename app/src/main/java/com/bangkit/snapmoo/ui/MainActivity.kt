package com.bangkit.snapmoo.ui


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.snapmoo.R
import com.bangkit.snapmoo.databinding.ActivityMainBinding
import com.bangkit.snapmoo.ui.scan.CameraActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBottomMenu()
    }

    private fun setupActionBottomMenu() {
        val bottomNavView: BottomNavigationView = binding.bottomNavView
        val bottomNavController = findNavController(R.id.bottom_nav_host_fragment)
        setToolBar()
        bottomNavController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.tvToolbarTitle.text = destination.label
        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_report,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(bottomNavController, appBarConfiguration)
        bottomNavView.setupWithNavController(bottomNavController)

        if (intent != null) {
            val openFragment = intent.getStringExtra("openFragment")
            if (openFragment == "profileFragment") {
                val item = bottomNavView.menu.findItem(R.id.navigation_profile)
                NavigationUI.onNavDestinationSelected(item, bottomNavController)
            }
            else if (openFragment == "sendReportFragment") {
                val item = bottomNavView.menu.findItem(R.id.navigation_report)
                NavigationUI.onNavDestinationSelected(item, bottomNavController)
            }
            intent = null
        }

        binding.fab.setOnClickListener {
            startCameraX()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    private fun setToolBar() {
        val toolbar: Toolbar = binding.toolbar.toolbar
        setSupportActionBar(toolbar)
        binding.toolbar.btnBackToolbar.visibility = View.GONE
    }


}

