package com.gneo.fgurbanov.junctionhealth.presentation.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gneo.fgurbanov.junctionhealth.R
import com.gneo.fgurbanov.junctionhealth.navigation.MainRoute
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        startBtn.setOnClickListener {
            MainRoute(context = this)
            finish()
        }
    }
}
