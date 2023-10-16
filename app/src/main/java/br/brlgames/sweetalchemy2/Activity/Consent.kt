package br.brlgames.sweetalchemy2.Activity

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import br.brlgames.sweetalchemy2.R
import br.brlgames.sweetalchemy2.databinding.ActivityConsentBinding
import br.brlgames.sweetalchemy2.dataconfig.sac

class Consent : AppCompatActivity() {


    private val PERMISSION_REQUEST_CODE = 201233
    private lateinit var consent : ActivityConsentBinding
    private lateinit var policyWV: WebView
    private lateinit var agree: Button
    private lateinit var disagree: Button

    private lateinit var sharedPref: SharedPreferences

    private var alertDialog : AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        consent = ActivityConsentBinding.inflate(layoutInflater)
        setContentView(consent.root)

        sharedPref = getSharedPreferences(sac.appCode, MODE_PRIVATE)

        policyWV = findViewById(R.id.consentView)
        agree = findViewById(R.id.AgreeBtn)
        disagree = findViewById(R.id.DisagreeBtn)

        policyWV.webViewClient = WebViewClient()

        policyWV.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                agree.visibility = View.VISIBLE
                disagree.visibility = View.VISIBLE
            }
        }
        policyWV.loadUrl(sac.policyURL)


        showConsent()
    }

    private fun showConsent() {
        disagree.setOnClickListener { finishAffinity() }
        val isPermissionGranted = checkPermissions(this)
        agree.setOnClickListener {
            alertDialog = AlertDialog.Builder(this@Consent)
            alertDialog!!.setTitle("User Data Consent")
            alertDialog!!.setMessage("We may collect your information based on your activities during the usage of the app, to provide better user experience.")
            alertDialog!!.setPositiveButton(
                "Agree"
            ) { dialogInterface: DialogInterface, _: Int ->
                sharedPref.edit().putBoolean("permitSendData", true).apply()
                dialogInterface.dismiss()
            }
            alertDialog!!.setNegativeButton(
                "Disagree"
            ) { dialogInterface: DialogInterface, _: Int ->
                sharedPref.edit().putBoolean("permitSendData", false).apply()
                dialogInterface.dismiss()
            }

            alertDialog!!.setOnDismissListener {
                if (sharedPref.getBoolean("permitSendData",true)) {
                    if (!isPermissionGranted){
                        requestPermission()
                    }else openGame()
                } else openGame()
            }
            alertDialog!!.show()

        }

    }

    private fun checkPermissions(context : Context): Boolean {
        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val mediaPermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        return locationPermission == PackageManager.PERMISSION_GRANTED
                && cameraPermission == PackageManager.PERMISSION_GRANTED
                && mediaPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        val permissions =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        //openGame()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val locationGranted = grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED
            val cameraGranted = grantResults.getOrNull(1) == PackageManager.PERMISSION_GRANTED
            val mediaGranted = grantResults.getOrNull(2) == PackageManager.PERMISSION_GRANTED

            sharedPref.edit {
                putBoolean("locationGranted", locationGranted)
                putBoolean("cameraGranted", cameraGranted)
                putBoolean("mediaGranted", mediaGranted)
                putBoolean("runOnce", locationGranted && cameraGranted && mediaGranted)
                apply()
            }
        }

        openGame()
    }

    private fun openGame() {
        val gameIntent = Intent(this, MainActivity::class.java)
        gameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(gameIntent)
        finish()
    }
}