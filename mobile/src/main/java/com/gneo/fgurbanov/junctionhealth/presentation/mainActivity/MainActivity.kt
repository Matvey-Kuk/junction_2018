package com.gneo.fgurbanov.junctionhealth.presentation.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gneo.fgurbanov.junctionhealth.R
import com.ncapdevi.fragnav.FragNavController

class MainActivity : AppCompatActivity(),
    FragNavController.RootFragmentListener {

    private val fragNavController = FragNavController(
        supportFragmentManager,
        R.id.fragment_container
    ).apply {
        rootFragmentListener = this@MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragNavController.initialize(FragNavController.TAB3, savedInstanceState)
    }

    override val numberOfRootFragments: Int = 3

    override fun getRootFragment(index: Int): Fragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        fragNavController.onSaveInstanceState(outState)
    }
}
