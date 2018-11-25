package com.gneo.fgurbanov.junctionhealth.presentation.mainActivity.fragments.about

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gneo.fgurbanov.junctionhealth.R
import dagger.android.support.DaggerFragment

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_about, container, false)


    companion object {
        fun newInstance(): AboutFragment = AboutFragment()
    }
}