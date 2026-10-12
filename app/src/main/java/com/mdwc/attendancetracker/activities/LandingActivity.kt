package com.mdwc.attendancetracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.mdwc.attendancetracker.R
import com.mdwc.attendancetracker.databinding.ActivityLandingBinding
import com.mdwc.attendancetracker.fragments.AnalyticsFragment
import com.mdwc.attendancetracker.fragments.HomeFragment
import com.mdwc.attendancetracker.fragments.ProfileFragment
import com.mdwc.attendancetracker.fragments.UpdatesFragment

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setBackground(null)

        binding.addSubjectFAB.setOnClickListener {
            val intent = Intent(this, AddSubjectActivity::class.java)
            startActivity(intent)

        }


        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setCurrentFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.home -> setCurrentFragment(HomeFragment())

                R.id.analytics -> setCurrentFragment(AnalyticsFragment())

                R.id.updates -> setCurrentFragment(UpdatesFragment())

                R.id.profile -> setCurrentFragment(ProfileFragment())
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }

}

