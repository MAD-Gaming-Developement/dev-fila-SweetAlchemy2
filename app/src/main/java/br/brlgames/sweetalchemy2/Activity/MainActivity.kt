package br.brlgames.sweetalchemy2.Activity

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.brlgames.sweetalchemy2.R
import br.brlgames.sweetalchemy2.databinding.ActivityMainBinding
import br.brlgames.sweetalchemy2.dataconfig.sac

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var bckExit = false

    private lateinit var sharedPref : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        sharedPref = getSharedPreferences(sac.appCode, MODE_PRIVATE)

        if(sharedPref.getString("apiResponse","success") != "success"){
            navView.visibility = View.GONE
        }


    }

    override fun onBackPressed() {
        if (bckExit) {
            super.finishAffinity()
            return
        }
        bckExit = true
        Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            bckExit = false
        }, 2000)
    }
}