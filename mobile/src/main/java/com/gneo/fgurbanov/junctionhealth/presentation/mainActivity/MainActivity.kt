package com.gneo.fgurbanov.junctionhealth.presentation.mainActivity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.gneo.fgurbanov.junctionhealth.R
import com.gneo.fgurbanov.junctionhealth.navigation.CameraRoute
import com.gneo.fgurbanov.junctionhealth.presentation.mainActivity.fragments.about.AboutFragment
import com.gneo.fgurbanov.junctionhealth.presentation.mainActivity.fragments.details.DetailsFragment
import com.ncapdevi.fragnav.FragNavController
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_fragment_provider.*


class MainActivity : DaggerAppCompatActivity(),
    FragNavController.RootFragmentListener {

    private val fragNavController = FragNavController(
        supportFragmentManager,
        R.id.fragment_container
    ).apply {
        rootFragmentListener = this@MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_provider)

        fragNavController.initialize(FragNavController.TAB1, savedInstanceState)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_favorites -> fragNavController.switchTab(INDEX_HISTORY)
                R.id.action_camera -> CameraRoute(this).start()
                R.id.action_about -> fragNavController.switchTab(INDEX_HISTORY)
                else -> throw IllegalStateException("Need to send an index that we know")
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override val numberOfRootFragments: Int = 2

    override fun getRootFragment(index: Int): Fragment =
        when (index) {
            INDEX_HISTORY -> DetailsFragment.newInstance()
            INDEX_ABOUT -> AboutFragment.newInstance()
            else -> throw IllegalStateException("Need to send an index that we know")
        }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        fragNavController.onSaveInstanceState(outState)
    }

    companion object {
        private const val INDEX_HISTORY = 0
        private const val INDEX_ABOUT = 1
    }
}
