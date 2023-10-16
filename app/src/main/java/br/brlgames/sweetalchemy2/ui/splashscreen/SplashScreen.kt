package br.brlgames.sweetalchemy2.ui.splashscreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import br.brlgames.sweetalchemy2.API.Volley
import br.brlgames.sweetalchemy2.API.Volley.requestQueue
import br.brlgames.sweetalchemy2.Activity.Consent
import br.brlgames.sweetalchemy2.Activity.MainActivity
import br.brlgames.sweetalchemy2.R
import br.brlgames.sweetalchemy2.databinding.ActivitySplashScreenBinding
import br.brlgames.sweetalchemy2.dataconfig.sac
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.json.JSONException
import org.json.JSONObject

class SplashScreen : AppCompatActivity() {

    private lateinit var splashScreen : ActivitySplashScreenBinding
    private var splashScreenFRC = "FirebaseRemoteConfig"
    private var apiResponse = "ApiResponse"
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreen.root)
        window.attributes.flags = (WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        sharedPref = getSharedPreferences(sac.appCode, MODE_PRIVATE)

        fbRemoteConfig()
    }

    private fun fbRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.default_config)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(splashScreenFRC, "Remote Config Connected: $updated")
                } else {
                    val exception = task.exception
                    Log.e(splashScreenFRC, "Error updating Config", exception)
                }
                setConfig()
            }

    }

    private fun setConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        sac.facebookAppId = remoteConfig.getString("facebookAppId")
        sac.apiURL = remoteConfig.getString("apiURL")
        sac.facebookClientToken = remoteConfig.getString("facebookClientToken")
        sac.policyURL = remoteConfig.getString("policyURL")

        Log.d("Config","apiURL: "+sac.apiURL)
        Log.d("Config","policyURL: "+sac.policyURL)
        Log.d("Config","appCode: "+sac.appCode)


        Volley.init(this)

        val endPoint = "${sac.apiURL}?appid=${sac.appCode}"
        Log.d("Config", "endpoint: $endPoint")

        val requestQueue = requestQueue


        val requestBody = JSONObject().apply {
            put("appid", sac.appCode)
        }
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            endPoint,
            requestBody,
            { response: JSONObject ->
                Log.d(apiResponse, response.toString())
                try {
                    sac.gameURL = response.getString("gameURL")
                    sharedPref.edit().putString("apiResponse", response.getString("status")).apply()
                    Log.d("Config", "gameURL: "+sac.gameURL)
                    val targetActivity = if (sharedPref.getBoolean("permitSendData", false)) {
                        MainActivity::class.java
                    } else {
                        Consent::class.java
                    }
                    val intent = Intent(this, targetActivity)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } catch (e: JSONException) {
                    throw RuntimeException(e)
                }
            },
            { error: VolleyError ->
                Log.d(apiResponse, error.message ?: "Unknown error")
            }
        )
        requestQueue?.add(jsonObjectRequest)

    }

}