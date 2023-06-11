package com.ukm.pajak.ui.apps

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*


import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ukm.pajak.MainActivity
import com.ukm.pajak.R
import com.ukm.pajak.databinding.FragmentAppsBinding
import com.ukm.pajak.models.ReferenceModel
import com.ukm.pajak.models.PajakAppModel
import com.ukm.pajak.services.TvmApiService
import com.ukm.pajak.tools.GeneralTools
import com.ukm.pajak.tools.SharedPreferencesTools
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppsFragment : Fragment(),AppsAdapter.ItemClickListener  {

    private lateinit var appList: ArrayList<PajakAppModel>
    private var _binding: FragmentAppsBinding? = null
    private lateinit var llLoader : ProgressBar
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onStop() {
        // call the superclass method first
        super.onStop()

    }


    private var flag: Boolean =false
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onResume() {
        super.onResume()
    }

    private lateinit var layoutManager : LinearLayoutManager

    private lateinit var selectedReference : ReferenceModel

    private fun setupRecyclerView() {

        recyclerView = binding.list



        adapter = AppsAdapter(this,appList)

        layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter


    }


    private fun loadInterstitialAd() {

        val testDeviceIds = listOf(
            "A27D54F21D395038C90E85A8C00C1DBD"
        )
        val configuration =
            RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)


        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),
            getString(R.string.interstitial_ad_unit_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    // The interstitialAd reference will be null until
                    // an ad is loaded.
                    interstitialAd = ad

                    ad.setFullScreenContentCallback(
                        object : FullScreenContentCallback() {
                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                interstitialAd = null


                                loadInterstitialAd()
                                hitung()

                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                interstitialAd = null
                                hitung()
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.

                            }


                        })
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error

                    interstitialAd = null

                    val error: String = String.format(
                        Locale.ENGLISH,
                        "domain: %s, code: %d, message: %s",
                        loadAdError.domain,
                        loadAdError.code,
                        loadAdError.message
                    )

                }
            })
    }


    private fun hitung(){


        //startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(appList[selectedPosition].link)))





        SharedPreferencesTools.saveData(
            requireContext(), "spinnerInterestCalculator",
            appList[selectedPosition].solution.pajak.toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextGaji",
            GeneralTools.formatMoney(appList[selectedPosition].solution.gaji,"en","ID").toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextPendapatan",
            GeneralTools.formatMoney(appList[selectedPosition].solution.bonus,"en","ID").toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextJumlahTanggungan",
            appList[selectedPosition].solution.tanggungan.toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextBiayaJabatan",
            GeneralTools.formatMoney(appList[selectedPosition].solution.biayajabatan,"en","ID").toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextIuranPensiun",
            GeneralTools.formatMoney(appList[selectedPosition].solution.iuranpensiun,"en","ID").toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextBiayaLainnya",
            GeneralTools.formatMoney(appList[selectedPosition].solution.biayausahalainnya,"en","ID").toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextJumlahTanggungan",
            appList[selectedPosition].solution.tanggungan.toString()
        )



        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextPendapatanBruto",
            GeneralTools.formatMoney( appList[selectedPosition].solution.bruto,"en","ID").toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextPenghasilanLainnya",
            GeneralTools.formatMoney( appList[selectedPosition].solution.penghasilanlainnya,"en","ID").toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextBiayaUsaha",
            GeneralTools.formatMoney(appList[selectedPosition].solution.biayausaha,"en","ID").toString()
        )



        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextPenghasilanDosen",
            GeneralTools.formatMoney(appList[selectedPosition].solution.penghasilandosen,"en","ID").toString()
        )



        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextKreditPajak",
            GeneralTools.formatMoney(appList[selectedPosition].solution.kreditpajak,"en","ID").toString()
        )


        val intent = Intent(requireContext(), MainActivity::class.java)
        requireContext().startActivity(intent)
    }



    private lateinit var adapter: AppsAdapter
    private lateinit var recyclerView : RecyclerView

    private var interstitialAd: InterstitialAd? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAppsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Studi Kasus"

        llLoader = binding.simpleProgressBar

        llLoader.visibility = View.VISIBLE

        loadInterstitialAd()

        loadAds()

        getTvms()



        return root
    }


    private fun getTvms() {

        val tvmsApiService = TvmApiService()

        tvmsApiService.getTvms() { it ->


            appList = it!!

            setupRecyclerView()


            llLoader.visibility = View.INVISIBLE


        }
    }
    private lateinit var adView: AdView
    private lateinit var adRequest: AdRequest

    private fun showInterstitial() {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.


        if (interstitialAd != null) {
            interstitialAd!!.show(requireActivity())
        } else {
            hitung()
        }

    }

    private fun loadAds(){

        adView = binding.adView!!

            MobileAds.initialize(requireContext())

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

    private var selectedPosition=0;

    override fun onItemClick(position: Int) {


        selectedPosition =position
        showInterstitial()



    }


}