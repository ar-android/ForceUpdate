package com.ahmadrosid.forceupdate

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val firebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build()
        var cacheExpiration = 3600

        if (firebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }


        firebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings)

        firebaseRemoteConfig.fetch(cacheExpiration.toLong())
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        firebaseRemoteConfig.activateFetched()
                    }

                    val version_code = firebaseRemoteConfig.getString("version_code")

                    if (version_code.toInt() > BuildConfig.VERSION_CODE) {
                        AlertDialog.Builder(this)
                                .setTitle("Pemberitahuan")
                                .setMessage("Ada pembaharuan aplikasi tersedia, silahkan update untuk mendapatkan pembaharuan.")
                                .setCancelable(false)
                                .setNegativeButton("Abaikan") { dialog, _ ->
                                    dialog.dismiss()
                                }.setPositiveButton("Update"){dialog, _ ->
                                    dialog.dismiss()
                                    try {
                                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                                    }catch (e: Exception){
                                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                                    }
                                }.show()
                    }
                }

    }

}
