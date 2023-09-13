package com.great.app

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.great.app.databinding.ActivityAppBinding
import com.great.app.fragment.BaseFragment
import com.great.app.fragment.DetailsFragment
import com.great.app.fragment.ListFragment
import com.great.app.repository.Repository
import com.great.app.repository.getMarvelApiService
import com.great.app.view_model.MainViewModel


class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = Repository(getMarvelApiService())
        ViewModelProvider(
            this, MainViewModel.FACTORY(repository)
        ).get(MainViewModel::class.java)

        setMainPage()
    }

    override fun onBackPressed() {
        val orientation = resources.configuration.orientation
        val isBackStackEmpty = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (supportFragmentManager.fragments.isNotEmpty()) {
                val fragment = supportFragmentManager.fragments.last() as BaseFragment<*>
                fragment.onBackPressed()
            }
            !supportFragmentManager.popBackStackImmediate()
        } else true

        if (isBackStackEmpty) {
            super.onBackPressed()
        }
    }

    fun openFragment(fragment: Fragment) {
        clearOldFragments(supportFragmentManager.beginTransaction())
            .add(R.id.lay_main, fragment)
            .addToBackStack(fragment.javaClass.name)
            .commit()
    }

    fun clearBackground() {
        binding.layMain.setBackgroundResource(R.color.c_white_light)
    }

    private fun clearOldFragments(fragmentTransaction: FragmentTransaction): FragmentTransaction {
        supportFragmentManager.fragments.forEach { oldFragment ->
            fragmentTransaction.remove(oldFragment)
        }
        return fragmentTransaction;
    }

    private fun setMainPage() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val listFragment = ListFragment()
            clearOldFragments(supportFragmentManager.beginTransaction())
                .add(R.id.lay_main, listFragment)
                .commit()
        } else {
            val listFragment = ListFragment()
            val detailsFragment = DetailsFragment()
            clearOldFragments(supportFragmentManager.beginTransaction())
                .add(R.id.lay_main, listFragment)
                .add(R.id.lay_main, detailsFragment)
                .commit()
        }
    }
}