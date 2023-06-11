package com.ukm.pajak.ui.reference

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.ukm.pajak.R
import com.ukm.pajak.databinding.ActivityReferenceDetailBinding

class ReferenceDetailActivity : AppCompatActivity() {

    private var _binding: ActivityReferenceDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityReferenceDetailBinding.inflate(layoutInflater)
        val view = _binding!!.root

        setContentView(view)


        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")

        loadAds()

        val textViewTitle = findViewById<TextView>(R.id.textViewTitle)
        val textViewDescription = findViewById<TextView>(R.id.textViewDescription)

        textViewTitle.text = title
        textViewDescription.text = description


        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "Body Fat Reference"
        actionBar?.setDisplayHomeAsUpEnabled(true)



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private lateinit var adView: AdView
    private lateinit var adRequest: AdRequest

    private fun loadAds(){

        adView = _binding?.adView!!

            MobileAds.initialize(this)

            val testDeviceIds = listOf(
                "A27D54F21D395038C90E85A8C00C1DBD"
            )
            val configuration =
                RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
            MobileAds.setRequestConfiguration(configuration)

            // on below line we are initializing
            // our ad view with its id


            // on below line we are
            // initializing our ad request.
            adRequest = AdRequest.Builder().build()

            // on below line we are loading our
            // ad view with the ad request
            adView.loadAd(adRequest)





    }
}