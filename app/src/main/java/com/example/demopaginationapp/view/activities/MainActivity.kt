package com.example.demopaginationapp.view.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.demopaginationapp.R
import com.example.demopaginationapp.databinding.ActivityMainBinding
import com.example.demopaginationapp.utils.FirebaseUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var firebaseUtils: FirebaseUtils// ... later in code ...


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply the insets as padding to the view
            // You can choose to apply only top, only bottom, or all
            v.setPadding(systemBars.left, systemBars.top, systemBars.right,0)

            insets
        }
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        binding.bottomNavigation.setupWithNavController(navController)

        val bottomBarRoutes = setOf(
            R.id.home_screen,
//            R.id.categories_screen,
            R.id.brand_screen,
            R.id.fav_screen,
            R.id.cart_screen,
            R.id.quotes_list_screen
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in bottomBarRoutes) {
                binding.bottomNavigation.visibility = View.VISIBLE
            } else {
                binding.bottomNavigation.visibility = View.GONE
            }
        }

        firebaseUtils.getFirebaseToken { token ->
            if (token != null) {
                // Send token to your server or save it locally
                Log.d("firebase_tokennn", "onCreate: tokennn = $token")
            }
        }
    }
}






