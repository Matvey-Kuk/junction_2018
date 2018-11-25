package com.gneo.fgurbanov.junctionhealth.presentation.auth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gneo.fgurbanov.junctionhealth.R
import com.gneo.fgurbanov.junctionhealth.navigation.ConnectionRoute
import com.gneo.fgurbanov.junctionhealth.navigation.MainRoute
import com.gneo.fgurbanov.junctionhealth.navigation.OldRoute
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        startBtn.setOnClickListener {
            MainRoute(activity = this).start()
            finish()
        }
    }
}
