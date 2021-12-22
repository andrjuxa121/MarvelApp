package com.great.app


import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.great.app.fragment.DetailsFragment
import com.great.app.fragment.ListFragment


class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity)
        setMainPage()
    }

    override fun onBackPressed() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            supportFragmentManager.popBackStack()
            return
        }
        super.onBackPressed()
    }

    fun openFragment(fragment: Fragment) {
        clearOldFragments(
            supportFragmentManager.beginTransaction())
            .add(R.id.lay_main, fragment)
            .addToBackStack(fragment.javaClass.name)
            .commit()
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
            clearOldFragments(
                supportFragmentManager.beginTransaction())
                .add(R.id.lay_main, listFragment)
                .commit()
        } else {
            val listFragment = ListFragment()
            val detailsFragment = DetailsFragment()
            clearOldFragments(
                supportFragmentManager.beginTransaction())
                .add(R.id.lay_main, listFragment)
                .add(R.id.lay_main, detailsFragment)
                .commit()
        }
    }
}