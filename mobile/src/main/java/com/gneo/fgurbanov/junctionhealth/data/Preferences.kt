package com.gneo.fgurbanov.junctionhealth.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Preferences @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun clear() = sharedPreferences.edit().clear().apply()

    companion object {
        private const val PREF_COOKIES = "cookies"

        private const val PREF_FEED_FRAGMENT = "feed_fragment"

        private const val PREF_FIRST_LAUNCH = "first_launch"
    }
}
