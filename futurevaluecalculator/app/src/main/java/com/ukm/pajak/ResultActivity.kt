package com.ukm.pajak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ukm.pajak.databinding.ActivityBodyFatResultBinding


import android.annotation.SuppressLint
import android.content.Context
import android.os.*
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar

import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.ukm.pajak.tools.GeneralTools
import com.ukm.pajak.tools.MoneyTextWatcher
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBodyFatResultBinding
    lateinit var webView: WebView
    lateinit var editTextFileName: EditText


    lateinit var floatingActionButtonPdf: FloatingActionButton


    lateinit var printJob: PrintJob
    var printBtnPressed = false
    var pdfColor: String = "#008aff"

    private lateinit var llLoader : ProgressBar

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBodyFatResultBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)
        llLoader = binding.simpleProgressBar

        llLoader.visibility = View.VISIBLE
        webView = findViewById(R.id.idWebView)
        editTextFileName = findViewById(R.id.edittext_filename)

        floatingActionButtonPdf = binding.floatingActionButtonShowPdf


        val localDateTime = LocalDateTime.parse(LocalDateTime.now().toString())
        val formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm")
        val output = formatter.format(localDateTime)
        editTextFileName.setText("PDF_PAJAK_PENGHASILAN_" + output)


        val newUA =
            "Mozilla/5.0 (Linux; Android 4.1.1; HTC One X Build/JRO03C) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.58 Mobile Safari/537.31";
        webView.settings.userAgentString = newUA


        webView.settings.userAgentString = newUA
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true

        webView.settings.javaScriptEnabled = true

        webView.settings.builtInZoomControls = true;
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

                webView = view
            }
        }
        // on below line we are adding click
        // listener for our save pdf button.
        floatingActionButtonPdf.setOnClickListener {
            // on below line we are checking
            // if web view is null or not.
            showInterstitial()
        }


        GlobalScope.launch(Dispatchers.Main) {

            loadInterstitialAd()


            loadAds()
        }

        val actionBar: ActionBar? = supportActionBar

        // showing the back button in action bar

        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private lateinit var adView: AdView
    private lateinit var adRequest: AdRequest

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadAds() {

        adView = findViewById(R.id.adView)

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


            createSimulasiKredit()
            // Your main thread related task



    }


    private companion object {
        //PERMISSION request constant, assign any value
        private const val STORAGE_PERMISSION_CODE = 100
        private const val TAG = "PERMISSION_TAG"
    }


    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun readFileFromAssets(filename: String): String {

        //We will build the string line by line from *.txt file
        val builder = StringBuilder()

        //BufferedReader is needed to read the *.txt file
        //Create and Initialize BufferedReader
        val reader = BufferedReader(InputStreamReader(assets.open(filename)))

        //This variable will contain the text
        var line: String?

        //check if there is a more line available
        while (reader.readLine().also { line = it } != null) {
            builder.append(line).append("\n")
        }

        //Need to close the BufferedReader
        reader.close()

        //just return the String of the *.txt file
        return builder.toString()
    }




    private lateinit var html: String

    @RequiresApi(Build.VERSION_CODES.O)
    fun createSimulasiKredit() {





        val gaji = intent.getStringExtra("gaji")!!.toDouble()

        val bonus = intent.getStringExtra("bonus")!!.toDouble()

        val tanggungan = intent.getStringExtra("tanggungan")!!.toInt()

        val biayaJabatan = intent.getStringExtra("biayaJabatan")!!.toDouble()

        val iuranPensiunan = intent.getStringExtra("iuranPensiun")!!.toDouble()

        var profesi = intent.getStringExtra("profesi")

        val pendapatanBruto = intent.getStringExtra("pendapatanBruto")!!.toDouble()

        val penghasilanLainnya = intent.getStringExtra("pendapatanLainnya")!!.toDouble()
        val biayaUsaha = intent.getStringExtra("biayaUsaha")!!.toDouble()
        val biayaUsahaLainnya = intent.getStringExtra("biayaUsahaLainnya")!!.toDouble()


        val penghasilanDosen = intent.getStringExtra("penghasilanDosen")!!.toDouble()


        val kreditPajak = intent.getStringExtra("kreditPajak")!!.toDouble()



        if(profesi=="Pedagang Eceran"||profesi=="Pengusaha"||profesi=="Arsitek Dengan Pembukuan"||profesi=="Konsultan Dengan Pembukuan"||profesi=="Notaris Dengan Pembukuan") {

            html = readFileFromAssets("tablepajakpenghasilanpengusaha.html")

            html = html.replace("PEREDARANBRUTO", (rupiah(pendapatanBruto)))
            html = html.replace("PENGHASILANLAINNYA", (rupiah(penghasilanLainnya)))
            html = html.replace("BIAYAUSAHALAINNYA", (rupiah(biayaUsahaLainnya)))
            html = html.replace("BIAYAUSAHA", (rupiah(biayaUsaha)))


        }

        else  if(profesi=="Artis Norma Penghitungan Penghasilan Netto atau PPh Final senilai 0,5%" ||
            profesi=="Desainer Royalti" ||
            profesi=="Musisi Royalti" ||
            profesi=="Musisi Memiliki Kursus Musik" ||
            profesi=="Desainer Memiliki Usaha Desain dan Memilih Dikenakan PP 23" ||
            profesi=="Arsitek Tanpa Pembukuan" ||
            profesi=="Desainer yang menggunakan norma" ||
            profesi=="Konsultan" ||
            profesi=="Musisi Orang Pribadi Yang Menggunakan Norma" ||
            profesi=="Notaris Orang Pribadi Yang Menggunakan Norma" ||
            profesi=="Olahragawan" ||
            profesi=="Pengajar sebagai Orang Pribadi yang memiliki kegiatan usaha jasa pendidikan" ||
            profesi=="Pedagang Alat Tulis dan Buku" ||
            profesi=="Penulis" ||
            profesi=="Artis Norma Penghitungan Penghasilan Netto dengan Klasifikasi Usaha dengan Nomor KLU 90002 (Kegiatan Pekerja Seni)" ) {
            html = readFileFromAssets("tablepajakpenghasilanpengusahatanpapembukuan.html")

            html = html.replace("PEREDARANBRUTO", (rupiah(pendapatanBruto)))
            html = html.replace("PENGHASILANLAINNYA", (rupiah(penghasilanLainnya)))
            html = html.replace("BIAYAUSAHALAINNYA", (rupiah(biayaUsahaLainnya)))
            html = html.replace("BIAYAUSAHA", (rupiah(biayaUsaha)))

        } else  if(profesi=="Dokter") {
            html = readFileFromAssets("tablepajakpenghasilandokter.html")



            html = html.replace("PEREDARANBRUTO", (rupiah(pendapatanBruto)))
            html = html.replace("PENGHASILANLAINNYA", (rupiah(penghasilanLainnya)))
        }
        else  if(profesi=="Peneliti") {
            html = readFileFromAssets("tablepajakpenghasilanpeneliti.html")



            html = html.replace("PEREDARANBRUTO", (rupiah(pendapatanBruto)))
            html = html.replace("PENGHASILANLAINNYA", (rupiah(penghasilanLainnya)))
        }
        else  if(profesi=="Pengacara") {
            html = readFileFromAssets("tablepajakpenghasilanpengacara.html")



            html = html.replace("PEREDARANBRUTO", (rupiah(pendapatanBruto)))
            html = html.replace("PENGHASILANLAINNYA", (rupiah(penghasilanLainnya)))
        }
        else{




                html = readFileFromAssets("tablepajakpenghasilan.html")

        }

        html = html.replace("GAJI", (rupiah(gaji)))

        html = html.replace("BONUS", (rupiah(bonus)))


        html = html.replace("TANGGUNGAN", tanggungan.toString())

        html = html.replace("BIAYAJABATAN", (rupiah(biayaJabatan)))

        html = html.replace("IURANPENSIUN", (rupiah(iuranPensiunan)))

        var totalPendapatan = gaji + bonus
        html = html.replace("PTKP", rupiah((54000000 + (tanggungan * 4500000)).toDouble()))

        var totalPengurangan = 54000000 + (tanggungan * 4500000) + biayaJabatan + iuranPensiunan


        var ptkp =0.0
        if(
            profesi=="Arsitek Dengan Pembukuan" ||
            profesi=="Konsultan Dengan Pembukuan" ||
            profesi=="Notaris Dengan Pembukuan"){



            totalPendapatan = pendapatanBruto + penghasilanLainnya
            totalPengurangan = biayaUsaha + biayaUsahaLainnya+ 54000000.00 + (tanggungan * 4500000)



            html = html.replace("NORMAPERSEN","0")

            html = html.replace("NORMA",rupiah(0.0))

            if(totalPendapatan<4800000000){

                totalPendapatan = 0.5* pendapatanBruto

                totalPengurangan = 54000000.00 + (tanggungan * 4500000)+ kreditPajak


                html = html.replace("NORMAPERSEN","50")

                html = html.replace("NORMA",rupiah(totalPendapatan*0.5))
            }

            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))
            ptkp = totalPendapatan - totalPengurangan
            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))

        }

        else if(profesi=="Pengusaha"||
            profesi=="Pedagang Eceran" ){


            totalPendapatan = pendapatanBruto + penghasilanLainnya
            totalPengurangan = biayaUsaha + biayaUsahaLainnya+ 54000000.00 + (tanggungan * 4500000)
            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))

            ptkp = (totalPendapatan) - totalPengurangan

            html = html.replace("NORMAPERSEN","0")

            html = html.replace("NORMA",rupiah(0.0))
            if(totalPendapatan<4800000000){

                totalPendapatan = pendapatanBruto + penghasilanLainnya

                html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))

                totalPengurangan = 54000000.00 + (tanggungan * 4500000)+ kreditPajak
                ptkp = (totalPendapatan*0.005) - totalPengurangan

                html = html.replace("NORMAPERSEN","0")

                html = html.replace("NORMA",rupiah(0.0))

            }


            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))

            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))

        }

        else if(profesi=="Arsitek Tanpa Pembukuan" ||

           profesi=="Notaris Orang Pribadi Yang Menggunakan Norma"||

            profesi=="Penulis" ||
            profesi=="Artis Norma Penghitungan Penghasilan Netto dengan Klasifikasi Usaha dengan Nomor KLU 90002 (Kegiatan Pekerja Seni)"
        ){


            totalPendapatan = pendapatanBruto
            totalPengurangan = 54000000.00 + (tanggungan * 4500000)


            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))


            html = html.replace("NORMAPERSEN","50")

            html = html.replace("NORMA",rupiah(totalPendapatan*0.5))
             ptkp = (totalPendapatan*0.5) - totalPengurangan
            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))

        }
        else if(
            profesi=="Desainer yang menggunakan norma"
        ){


            totalPendapatan = pendapatanBruto
            totalPengurangan = biayaUsahaLainnya+ 54000000.00 + (tanggungan * 4500000)


            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))
            ptkp = (totalPendapatan*0.32) - totalPengurangan


            html = html.replace("NORMAPERSEN","32")

            html = html.replace("NORMA",rupiah(totalPendapatan*0.32))

            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))

        }
        else if(profesi=="Pengajar sebagai Orang Pribadi yang memiliki kegiatan usaha jasa pendidikan"||
            profesi=="Musisi Orang Pribadi Yang Menggunakan Norma"
        ){


            totalPendapatan = pendapatanBruto
            totalPengurangan =  54000000.00 + (tanggungan * 4500000)


            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))
            ptkp = (totalPendapatan*0.3) - totalPengurangan


            html = html.replace("NORMAPERSEN","30")

            html = html.replace("NORMA",rupiah(totalPendapatan*0.3))

            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))

        }
        else if(profesi=="Pengajar Sebagai Subjek Luar Negeri"
        ){


            totalPendapatan = pendapatanBruto
            totalPengurangan =  54000000.00 + (tanggungan * 4500000)


            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))
            ptkp = (totalPendapatan*0.2) - totalPengurangan


            html = html.replace("NORMAPERSEN","20")

            html = html.replace("NORMA",rupiah(totalPendapatan*0.2))

            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))

        }
        else if(
            profesi=="Olahragawan"
        ){


            totalPendapatan = pendapatanBruto + penghasilanLainnya
            totalPengurangan = 54000000.00 + (tanggungan * 4500000)


            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))
            ptkp = (totalPendapatan*0.35) - totalPengurangan


            html = html.replace("NORMAPERSEN","35")

            html = html.replace("NORMA",rupiah(totalPendapatan*0.35))

            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))

        }
        else if(
            profesi=="Desainer yang menggunakan norma"
        ){


            totalPendapatan = pendapatanBruto + penghasilanLainnya
            totalPengurangan = biayaUsaha + biayaUsahaLainnya+ 54000000.00 + (tanggungan * 4500000)


            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))
            ptkp = (totalPendapatan*0.32) - totalPengurangan


            html = html.replace("NORMAPERSEN","32")

            html = html.replace("NORMA",rupiah(totalPendapatan*0.32))

            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))

        }

        else if(


            profesi=="Musisi Memiliki Kursus Musik"||
            profesi=="Desainer Memiliki Usaha Desain dan Memilih Dikenakan PP 23" ||
            profesi=="Artis Norma Penghitungan Penghasilan Netto atau PPh Final senilai 0,5%"
            ) {


            totalPendapatan = pendapatanBruto

            totalPengurangan = 54000000.00 + (tanggungan * 4500000)



            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))

            html = html.replace("TOTALKENAPAJAK", rupiah((totalPendapatan*0.005) - totalPengurangan))

            html = html.replace("NORMAPERSEN","0")

            html = html.replace("NORMA",rupiah(0.0))
            ptkp = (totalPendapatan*0.005) - totalPengurangan
        }




            else if(
                profesi=="Desainer Royalti" ||
                profesi=="Musisi Royalti"
            ) {


            totalPendapatan = pendapatanBruto

            totalPengurangan = 54000000.00 + (tanggungan * 4500000)



            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))

            html = html.replace("TOTALKENAPAJAK", rupiah(pendapatanBruto * 0.15 - totalPengurangan))

            html = html.replace("NORMAPERSEN","15")

            html = html.replace("NORMA",rupiah(pendapatanBruto * 0.15))
            ptkp = pendapatanBruto * 0.15 - totalPengurangan


        }

            else if(
                profesi=="Dokter"
            ) {


            totalPendapatan = pendapatanBruto + penghasilanLainnya + gaji + bonus + penghasilanDosen

            totalPengurangan = 54000000.00 + (tanggungan * 4500000) + biayaJabatan + iuranPensiunan


            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))
            ptkp =
                (pendapatanBruto * 0.5) + (penghasilanLainnya * 0.5) + (penghasilanDosen*0.35)+ gaji + bonus - totalPengurangan

            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))

            html = html.replace("NORMAPERSEN","15/50")

            html = html.replace("NORMA",rupiah((pendapatanBruto * 0.5) + (penghasilanLainnya * 0.5) + (penghasilanDosen*0.35)))
            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))


        }
        else if(
            profesi=="Peneliti"
        ) {


            totalPendapatan = pendapatanBruto  + gaji + bonus

            totalPengurangan = 54000000.00 + (tanggungan * 4500000) + biayaJabatan + iuranPensiunan


            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))
            ptkp =
                (pendapatanBruto * 0.05)  + gaji + bonus - totalPengurangan

            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))

            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))


        }
        else if(
            profesi=="Konsultan"
        ) {


            totalPendapatan = pendapatanBruto + penghasilanLainnya + gaji + bonus

            totalPengurangan = 54000000.00 + (tanggungan * 4500000) + biayaJabatan + iuranPensiunan


            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))
            ptkp =
                (pendapatanBruto * 0.5) + (penghasilanLainnya * 0.5) + gaji + bonus - totalPengurangan
            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))

            html = html.replace("NORMAPERSEN","50")

            html = html.replace("NORMA",rupiah( (pendapatanBruto * 0.5) + (penghasilanLainnya * 0.5)))


        }  else if(
            profesi=="Pedagang Alat Tulis dan Buku"
        ) {


            totalPendapatan = pendapatanBruto + penghasilanLainnya + gaji + bonus

            totalPengurangan = 54000000.00 + (tanggungan * 4500000) + biayaJabatan + iuranPensiunan


            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))
            ptkp =
                (pendapatanBruto * 0.36) + (penghasilanLainnya * 0.36) + gaji + bonus - totalPengurangan
            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))



            html = html.replace("NORMAPERSEN","36")

            html = html.replace("NORMA",rupiah(  (pendapatanBruto * 0.36) + (penghasilanLainnya * 0.36)))


        }
            else if(
                profesi=="Pengacara"
            ){



                totalPendapatan = pendapatanBruto + penghasilanLainnya + gaji+ bonus

            totalPengurangan = 54000000.00 + (tanggungan * 4500000) + biayaJabatan + iuranPensiunan



                html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
                html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))
                ptkp = (pendapatanBruto *0.51) + (penghasilanLainnya*0.51) + gaji+ bonus - totalPengurangan
                html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))



            html = html.replace("NORMAPERSEN","51")

            html = html.replace("NORMA",rupiah( (pendapatanBruto * 0.51) + (penghasilanLainnya * 0.51)))




            }else {


            html = html.replace("TOTALPENDAPATAN", (rupiah(totalPendapatan)))
            html = html.replace("TOTALPENGURANGAN", (rupiah(totalPengurangan)))
            ptkp = totalPendapatan - totalPengurangan
            html = html.replace("TOTALKENAPAJAK", rupiah(ptkp))


        }




        if(ptkp<0){

            ptkp =0.0
        }

        var total = 0.0

        if (ptkp >= 60000000) {
            html = html.replace("TIER1", rupiah(0.05 * 60000000))

            total = (0.05 * 60000000)

            if ((ptkp - 60000000) >= 190000000) {

                total += (0.15 * 190000000)
                html = html.replace("TIER2", rupiah(0.15 * 190000000))

                if ((ptkp - 250000000) >= 250000000) {

                    total += (0.25 * 250000000)
                    html = html.replace("TIER3", rupiah(0.25 * 250000000))

                    if ((ptkp - 500000000) >= 4500000000) {

                        total += (0.3 * 4500000000)
                        html = html.replace("TIER4", rupiah(0.3 * 4500000000))

                        if ((ptkp - 5000000000) >= 0) {

                            total += (0.35 * (ptkp - 5000000000))
                            html = html.replace("TIER5", rupiah(0.35 * (ptkp - 5000000000)))
                        }

                    } else {
                        html = html.replace("TIER4", rupiah(0.3 * (ptkp - 500000000)))

                        html = html.replace("TIER5", rupiah(0.0))

                        total += 0.3 * (ptkp - 500000000)
                    }

                } else {
                    html = html.replace("TIER3", rupiah(0.25 * (ptkp - 250000000)))
                    html = html.replace("TIER4", rupiah(0.0))
                    html = html.replace("TIER5", rupiah(0.0))

                    total += 0.25 * (ptkp - 250000000)
                }

            } else {


                html = html.replace("TIER2", rupiah(0.15 * (ptkp - 60000000)))
                html = html.replace("TIER3", rupiah(0.0))
                html = html.replace("TIER4", rupiah(0.0))
                html = html.replace("TIER5", rupiah(0.0))


                total += 0.15 * (ptkp - 60000000)
            }

        } else {

            html = html.replace("TIER1", rupiah(0.05 * ptkp))
            html = html.replace("TIER2", rupiah(0.0))
            html = html.replace("TIER3", rupiah(0.0))
            html = html.replace("TIER4", rupiah(0.0))
            html = html.replace("TIER5", rupiah(0.0))

            total += 0.05 * ptkp

        }


        html = html.replace("PAJAKTERHUTANG", rupiah(total))

        html = html.replace("TOTALPAJAK", rupiah(total-kreditPajak))


        html = html.replace("KREDITPAJAK", rupiah(kreditPajak))


        val mime = "text/html";
        val encoding = "utf-8";

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, html, mime, encoding, null)



        llLoader.visibility = View.GONE;


    }


    fun rupiah(number: Double): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(number).toString()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun printWebPage(webview: WebView) {
        // on below line we are initializing
        // print button pressed variable to true.
        printBtnPressed = true
        // on below line we are initializing
        // our print manager variable.
        val printManager =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                this
                    .getSystemService(Context.PRINT_SERVICE) as PrintManager
            } else {
                TODO("VERSION.SDK_INT < KITKAT")
            }

        // on below line we are creating a variable for job name
        var jobName = "PDF_PAJAK_PENGHASILAN"

        if (jobName == "") {

            val localDateTime = LocalDateTime.parse(LocalDateTime.now().toString())
            val formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm")
            val output = formatter.format(localDateTime)
            jobName = "PDF_PAJAK_PENGHASILAN"+ "_" + output

        }
        // on below line we are initializing our print adapter.
        val printAdapter =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                // on below line we are creating
                // our print document adapter.
                webView.createPrintDocumentAdapter(jobName)
            } else {
                TODO("VERSION.SDK_INT < LOLLIPOP")
            }
        // on below line we are checking id
        // print manager is not null
        assert(printManager != null)

        // on below line we are initializing
        // our print job with print manager
        printJob = printManager.print(
            jobName, printAdapter,
            // on below line we are calling
            // build method for print attributes.
            PrintAttributes.Builder().build()
        )
    }

    private var flag: Boolean = false


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onResume() {
        super.onResume()

        if (flag) {

            flag = false


        }

        // on below line we are checking
        // if print button is pressed.
        if (printBtnPressed) {
            // in this case we are simply checking
            // if the print job is completed.
            if (printJob.isCompleted) {
                // in this case we are simply displaying a completed toast message
                Toast.makeText(this, "Completed..", Toast.LENGTH_SHORT).show()
            }

            // below method is called if the print job has started.
            else if (printJob.isStarted) {
                Toast.makeText(this, "Started..", Toast.LENGTH_SHORT).show()
            }

            // below method is called if print job has blocked.
            else if (printJob.isBlocked) {
                Toast.makeText(this, "Blocked..", Toast.LENGTH_SHORT).show()
            }

            // below method is called if print job has cancelled.
            else if (printJob.isCancelled) {
                Toast.makeText(this, "Cancelled..", Toast.LENGTH_SHORT).show()
            }

            // below method is called is print job is failed.
            else if (printJob.isFailed) {
                Toast.makeText(this, "Failed..", Toast.LENGTH_SHORT).show()
            }

            // below method is called if print job is queued.
            else if (printJob.isQueued) {
                Toast.makeText(this, "Jon Queued..", Toast.LENGTH_SHORT).show()
            }

            // on below line we are simply initializing
            // our print button pressed as false
            printBtnPressed = false
        }
    }

    private var interstitialAd: InterstitialAd? = null

    private fun loadInterstitialAd() {

        val testDeviceIds = listOf(
            "A27D54F21D395038C90E85A8C00C1DBD"
        )
        val configuration =
            RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)


        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.interstitial_ad_unit_id), adRequest,
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
                                Log.d(title.toString(), "The ad was dismissed.")


                                loadInterstitialAd()
                                hitung()

                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                interstitialAd = null
                                Log.d(title.toString(), "The ad failed to show.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                Log.d(title.toString(), "The ad was shown.")
                            }


                        })
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.i(title.toString(), loadAdError.message)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun hitung() {


        if (webView != null) {
            // if web view is not null we are calling
            // print web page method to generate pdf.
            printWebPage(webView)
        } else {
            // in else condition we are simply displaying a toast message
            Toast.makeText(this, "Webpage not loaded..", Toast.LENGTH_SHORT).show()
        }

        // if web view is null or not.

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showInterstitial() {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.


        if (interstitialAd != null) {
            interstitialAd!!.show(this)
        } else {
            hitung()
        }

    }


}