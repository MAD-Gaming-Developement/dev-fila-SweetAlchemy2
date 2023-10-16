package br.brlgames.sweetalchemy2.dataconfig

import android.app.Application
import android.content.SharedPreferences

class sac : Application() {

    lateinit var sharedPref: SharedPreferences

    companion object {
        var facebookAppId: String = ""
        var facebookClientToken: String = ""
        const val appCode = "BRL003GS"
        var apiURL = ""
        var gameURL = ""
        var policyURL = ""
    }

    override fun onCreate() {
        super.onCreate()

        sharedPref = getSharedPreferences(appCode, MODE_PRIVATE)
    }

}