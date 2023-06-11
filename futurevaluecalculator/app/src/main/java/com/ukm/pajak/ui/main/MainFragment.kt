package com.ukm.pajak.ui.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*


import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skydoves.powerspinner.PowerSpinnerView
import com.ukm.pajak.R
import com.ukm.pajak.databinding.FragmentMainBinding

import com.ukm.pajak.models.ParameterModel
import com.ukm.pajak.tools.*
import com.ukm.pajak.tools.MoneyTextWatcher
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
class MainFragment : Fragment() {


    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adView: AdView

    private lateinit var spinnerProfesi: PowerSpinnerView
    private lateinit var editTextGaji: EditText
    private lateinit var editTextPendapatan: EditText


    private lateinit var listProfesi: ArrayList<ParameterModel>

    private lateinit var adRequest: AdRequest

    private lateinit var textWatcherPV: MoneyTextWatcher


    private lateinit var imageViewGaji:ImageView
    private lateinit var imageViewPendapatan:ImageView




    private lateinit var textViewPenghasilanBruto:TextView

    private lateinit var textViewPengurangan:TextView
    private lateinit var textViewGaji:TextView
    private lateinit var textViewPendapatan:TextView

    private lateinit var textViewJumlahTanggungan:TextView
    private lateinit var textViewBiayaJabatan:TextView
    private lateinit var textViewIuranPensiun:TextView

    private lateinit var imageViewJumlahTanggungan:ImageView
    private lateinit var imageViewBiayaJabatan:ImageView
    private lateinit var imageViewIuranPensiun:ImageView


    private lateinit var editTextJumlahTanggungan:EditText
    private lateinit var editTextBiayaJabatan:EditText
    private lateinit var editTextIuranPensiun:EditText

    private lateinit var linearPengahasilanBruto:LinearLayout
    private lateinit var linearPengurangan:LinearLayout


    private lateinit var textViewPendapatanBruto:TextView
    private lateinit var textViewPenghasilanLainnya:TextView
    private lateinit var textViewBiayaUsaha:TextView
    private lateinit var textViewBiayaLainnya:TextView
    private lateinit var textViewPenghasilanDosen:TextView
    private lateinit var textViewKreditPajak:TextView

    private lateinit var editTextPendapatanBruto:EditText
    private lateinit var editTextPenghasilanLainnya:EditText
    private lateinit var editTextBiayaUsaha:EditText
    private lateinit var editTextBiayaLainnya:EditText

    private lateinit var editTextPenghasilanDosen:EditText

    private lateinit var editTextKreditPajak:EditText


    private lateinit var imageViewPendapatanBruto:ImageView
    private lateinit var imageViewPenghasilanLainnya:ImageView
    private lateinit var imageViewBiayaUsaha:ImageView
    private lateinit var imageViewBiayaLainnya:ImageView
    private lateinit var imageViewPenghasilanDosen:ImageView
    private lateinit var imageViewKreditPajak:ImageView

    private var flagInit = false

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentMainBinding.inflate(inflater, container, false)

        inits()

        val root: View = binding.root



        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Pajak Penghasilan"


        loadAds()


        if (SharedPreferencesTools.getData(requireContext(),"disclaimer")!="setuju"){



            getDisclaimer()
        }

        editTextGaji.addTextChangedListener(MoneyTextWatcher(editTextGaji,"id","ID"));
        editTextBiayaJabatan.addTextChangedListener(MoneyTextWatcher(editTextBiayaJabatan,"id","ID"));
        editTextIuranPensiun.addTextChangedListener(MoneyTextWatcher(editTextIuranPensiun,"id","ID"));
        editTextPendapatan.addTextChangedListener(MoneyTextWatcher(editTextPendapatan,"id","ID"));
        editTextPenghasilanDosen.addTextChangedListener(MoneyTextWatcher(editTextPenghasilanDosen,"id","ID"));


        editTextPendapatanBruto.addTextChangedListener(MoneyTextWatcher(editTextPendapatanBruto,"id","ID"));
        editTextPenghasilanLainnya.addTextChangedListener(MoneyTextWatcher(editTextPenghasilanLainnya,"id","ID"));
        editTextBiayaLainnya.addTextChangedListener(MoneyTextWatcher(editTextBiayaLainnya,"id","ID"));
        editTextBiayaUsaha.addTextChangedListener(MoneyTextWatcher(editTextBiayaUsaha,"id","ID"));


        editTextKreditPajak.addTextChangedListener(MoneyTextWatcher(editTextKreditPajak,"id","ID"));



        spinnerProfesi.setOnSpinnerItemSelectedListener<String> { _, _, _, newText ->

            spinnerProfesi.setError(null)
            spinnerProfesi.setHint("Profesi")

            if(flagInit) {
                reset()


            }
            flagInit= true
            updateInterestCalculatorInputs(spinnerProfesi.selectedIndex)

        }


        spinnerProfesi.setOnSpinnerOutsideTouchListener { view, motionEvent ->

            spinnerProfesi.dismiss()
        }




        floatingActionButtonClose.setOnClickListener {

            SharedPreferencesTools.saveData(
                requireContext(), "spinnerInterestCalculator",
                ""
            )
            spinnerProfesi.selectItemByIndex(0)



            reset()


            Toast.makeText(
                requireContext(),
                "Data Successfully Deleted!",
                Toast.LENGTH_SHORT
            ).show()


        }

        floatingActionButtonSave.setOnClickListener {

            save()

        }

        floatingActionButtonSimulasi.setOnClickListener {


            if ( validate()) {

                save()
                showInterstitial()
            }
        }



        loadInterstitialAd()
        load()


        flag = true



        return root
    }

    private fun reset(){


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextGaji",
            "0"
        )

        editTextGaji.setText("0")

        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextPendapatan",
            "0"
        )

        editTextPendapatan.setText("0")

        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextJumlahTanggungan",
            "0"
        )

        editTextJumlahTanggungan.setText("0")

        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextBiayaJabatan",
            "0"
        )
        editTextBiayaJabatan.setText("0")


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextIuranPensiun",
            "0"
        )
        editTextIuranPensiun.setText("0")


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextBiayaLainnya",
            "0"
        )
        editTextBiayaLainnya.setText("0")
        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextBiayaUsaha",
            "0"
        )

        editTextBiayaUsaha.setText("0")
        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextPendapatanBruto",
            "0"
        )

        editTextPendapatanBruto.setText("0")

        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextPenghasilanLainnya",
            "0"
        )

        editTextPenghasilanLainnya.setText("0")



        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextPenghasilanDosen",
            "0"
        )

        editTextPenghasilanDosen.setText("0")


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextKreditPajak",
            "0"
        )

        editTextKreditPajak.setText("0")
    }

    private fun save(){






        SharedPreferencesTools.saveData(
            requireContext(), "spinnerInterestCalculator",
            spinnerProfesi.selectedIndex.toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextGaji",
            editTextGaji.text.toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextPendapatan",
            editTextPendapatan.text.toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextJumlahTanggungan",
            editTextJumlahTanggungan.text.toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextBiayaJabatan",
            editTextBiayaJabatan.text.toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextIuranPensiun",
            editTextIuranPensiun.text.toString()
        )


        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextBiayaLainnya",
            editTextBiayaLainnya.text.toString()
        )
        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextBiayaUsaha",
            editTextBiayaUsaha.text.toString()
        )

        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextJumlahTanggungan",
            editTextJumlahTanggungan.text.toString()
        )



        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextPendapatanBruto",
            editTextPendapatanBruto.text.toString()
        )
        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextBiayaUsaha",
            editTextPenghasilanLainnya.text.toString()
        )



        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextPenghasilanDosen",
            editTextPenghasilanDosen.text.toString()
        )



        SharedPreferencesTools.saveData(
            requireContext(),
            "editTextKreditPajak",
            editTextKreditPajak.text.toString()
        )



        Toast.makeText(
            requireContext(),
            "Data Successfully saved!",
            Toast.LENGTH_SHORT
        ).show()
    }
    private fun load() {



        if (SharedPreferencesTools.getData(requireContext(), "spinnerInterestCalculator") != "") {
            spinnerProfesi.selectItemByIndex(
                SharedPreferencesTools.getData(requireContext(), "spinnerInterestCalculator")!!.toInt()
            )
        }





        if(SharedPreferencesTools.getData(requireContext(), "editTextGaji")==""){
            editTextGaji.setText("0")

        }else{

            editTextGaji.setText(SharedPreferencesTools.getData(requireContext(), "editTextGaji"))

        }

        if(SharedPreferencesTools.getData(requireContext(), "editTextPendapatan")==""){
            editTextPendapatan.setText("0")

        }else{

            editTextPendapatan.setText(SharedPreferencesTools.getData(requireContext(), "editTextPendapatan"))

        }

        if(SharedPreferencesTools.getData(requireContext(), "editTextJumlahTanggungan")==""){
            editTextJumlahTanggungan.setText("0")

        }else{

            editTextJumlahTanggungan.setText(SharedPreferencesTools.getData(requireContext(), "editTextJumlahTanggungan"))

        }

        if(SharedPreferencesTools.getData(requireContext(), "editTextBiayaJabatan")==""){
            editTextBiayaJabatan.setText("0")

        }else{

            editTextBiayaJabatan.setText(SharedPreferencesTools.getData(requireContext(), "editTextBiayaJabatan"))

        }

        if(SharedPreferencesTools.getData(requireContext(), "editTextIuranPensiun")==""){
            editTextIuranPensiun.setText("0")

        }else{

            editTextIuranPensiun.setText(SharedPreferencesTools.getData(requireContext(), "editTextIuranPensiun"))

        }



        if(SharedPreferencesTools.getData(requireContext(), "editTextPendapatanBruto")==""){
            editTextPendapatanBruto.setText("0")

        }else{

            editTextPendapatanBruto.setText(SharedPreferencesTools.getData(requireContext(), "editTextPendapatanBruto"))

        }



        if(SharedPreferencesTools.getData(requireContext(), "editTextPenghasilanLainnya")==""){
            editTextPenghasilanLainnya.setText("0")

        }else{

            editTextPenghasilanLainnya.setText(SharedPreferencesTools.getData(requireContext(), "editTextPenghasilanLainnya"))

        }



        if(SharedPreferencesTools.getData(requireContext(), "editTextBiayaUsaha")==""){
            editTextBiayaUsaha.setText("0")

        }else{

            editTextBiayaUsaha.setText(SharedPreferencesTools.getData(requireContext(), "editTextBiayaUsaha"))

        }



        if(SharedPreferencesTools.getData(requireContext(), "editTextBiayaLainnya")==""){
            editTextBiayaLainnya.setText("0")

        }else{

            editTextBiayaLainnya.setText(SharedPreferencesTools.getData(requireContext(), "editTextBiayaLainnya"))

        }




        if(SharedPreferencesTools.getData(requireContext(), "editTextPenghasilanDosen")==""){
            editTextPenghasilanDosen.setText("0")

        }else{

            editTextPenghasilanDosen.setText(SharedPreferencesTools.getData(requireContext(), "editTextPenghasilanDosen"))

        }


        if(SharedPreferencesTools.getData(requireContext(), "editTextKreditPajak")==""){
            editTextKreditPajak.setText("0")

        }else{

            editTextKreditPajak.setText(SharedPreferencesTools.getData(requireContext(), "editTextKreditPajak"))

        }




    }


    lateinit var floatingActionButtonSimulasi: FloatingActionButton
    lateinit var floatingActionButtonClose: FloatingActionButton
    lateinit var floatingActionButtonSave: FloatingActionButton

    private fun inits() {
        floatingActionButtonSimulasi = binding.floatingActionButtonSimulasi!!
        floatingActionButtonClose = binding.floatingActionButtonClose!!

        floatingActionButtonSave = binding.floatingActionButtonSave!!

        editTextGaji = _binding!!.editTextGaji!!
        editTextPendapatan = _binding!!.editTextPendapatan!!

        spinnerProfesi = _binding!!.spinnerProfesi!!

        listProfesi= ArrayList<ParameterModel>()
        listProfesi.add(ParameterModel("", "0", "PMT"))
        listProfesi.add(ParameterModel("Pegawai Tetap", "2", "PMT"))
        listProfesi.add(ParameterModel("Arsitek Tanpa Pembukuan", "0", "Simulasi Kredit produk-produk elektronik dengan pilihan merk dan model, dilengkapi dengan perhitungan DP, pajak, suku bunga, dan tenor. "))
        listProfesi.add(ParameterModel("Arsitek Dengan Pembukuan", "0", "Simulasi Kredit produk-produk elektronik dengan pilihan merk dan model, dilengkapi dengan perhitungan DP, pajak, suku bunga, dan tenor. "))

        listProfesi.add(ParameterModel("Artis Norma Penghitungan Penghasilan Netto atau PPh Final senilai 0,5%", "2", "PMT"))
        listProfesi.add(ParameterModel("Artis Norma Penghitungan Penghasilan Netto dengan Klasifikasi Usaha dengan Nomor KLU 90002 (Kegiatan Pekerja Seni)", "2", "PMT"))

        listProfesi.add(ParameterModel("Desainer yang menggunakan norma", "0", "PMT"))
        listProfesi.add(ParameterModel("Desainer Sebagai Pegawai Tetap","0", "PMT"))

        listProfesi.add(ParameterModel("Desainer Royalti","0", "PMT"))

        listProfesi.add(ParameterModel("Desainer Memiliki Usaha Desain dan Memilih Dikenakan PP 23","0", "PMT"))
        listProfesi.add(ParameterModel("Dokter", "1", "PMT"))
        listProfesi.add(ParameterModel("Karyawan BUMN", "2", "PMT"))
        listProfesi.add(ParameterModel("Kepala Daerah", "0", "PMT"))
        listProfesi.add(ParameterModel("Konsultan", "2", "PMT"))

        //listProfesi.add(ParameterModel("Konsultan Dengan Pembukuan", "2", "PMT"))
        listProfesi.add(ParameterModel("Musisi Orang Pribadi Yang Menggunakan Norma", "2", "PMT"))
        listProfesi.add(ParameterModel("Musisi Orang Pribadi Pegawai Tetap", "2", "PMT"))
        listProfesi.add(ParameterModel("Musisi Royalti", "2", "PMT"))
        listProfesi.add(ParameterModel("Musisi Memiliki Kursus Musik", "2", "PMT"))
        listProfesi.add(ParameterModel("Notaris Orang Pribadi Yang Menggunakan Norma", "2", "PMT"))

        listProfesi.add(ParameterModel("Notaris Dengan Pembukuan", "2", "PMT"))
        listProfesi.add(ParameterModel("Olahragawan", "2", "PMT"))
        listProfesi.add(ParameterModel("Pedagang Eceran", "2", "PMT"))
        listProfesi.add(ParameterModel("Peneliti", "2", "PMT"))
        listProfesi.add(ParameterModel("Pengacara", "2", "PMT"))
        listProfesi.add(ParameterModel("Pengajar Sebagai Pegawai Tetap", "2", "PMT"))
        listProfesi.add(ParameterModel("Pengajar Sebagai Bukan Pegawai", "2", "PMT"))
        listProfesi.add(ParameterModel("Pengajar Sebagai Subjek Luar Negeri", "2", "PMT"))
        listProfesi.add(ParameterModel("Pengajar sebagai Orang Pribadi yang memiliki kegiatan usaha jasa pendidikan", "2", "PMT"))
            listProfesi.add(ParameterModel("Pengajar sebagai Pegawai Tetap dan Menerima Tunjangan Sertifikasi", "2", "PMT"))
        listProfesi.add(ParameterModel("Pengusaha", "2", "PMT"))
        listProfesi.add(ParameterModel("Penulis", "2", "PMT"))
        listProfesi.add(ParameterModel("Pedagang Alat Tulis dan Buku", "2", "PMT"))
        listProfesi.add(ParameterModel("Tenaga Kerja Indonesia di Luar Negeri", "2", "PMT"))


        //listInterestCalculators.add(ParameterModel("18. Periodic Deposit (PMT)", "2", "PMT"))

        spinnerProfesi.setItems(listProfesi.map { it.ParameterDisplay })

        linearPengahasilanBruto = binding.liniearPenghasilan!!
        linearPengurangan = binding.linearPengurangan!!



        imageViewGaji= _binding!!.imageViewGaji!!
        imageViewPendapatan= _binding!!.imageViewPendapatan!!



        textViewGaji= _binding!!.textViewGaji!!
        textViewPendapatan= _binding!!.textViewPendapatan!!



        textViewGaji= binding.textViewGaji!!
        textViewPenghasilanBruto= binding.textviewPenghasilanbruto!!
        textViewPengurangan= binding.textviewPengurangan!!
        textViewPendapatan= binding.textViewPendapatan!!

        textViewJumlahTanggungan= binding.textviewJumlahtanggungan!!
        textViewBiayaJabatan= binding.textviewBiayajabatan!!
        textViewIuranPensiun= binding.textviewIuranpensiun!!

        imageViewJumlahTanggungan= binding.imageJumlahtanggungan!!
        imageViewBiayaJabatan= binding.imageBiayajabatan!!
        imageViewIuranPensiun= binding.imageviewIuranpensiun!!


        editTextJumlahTanggungan= binding.edittextTanggungan!!
        editTextBiayaJabatan= binding.edittextBiayajabatan!!
        editTextIuranPensiun= binding.edittextIuranpensiun!!

        textViewPendapatanBruto = binding.textViewPeredaranBruto!!
        textViewPenghasilanLainnya= binding.textViewPenghasilanLainnya!!
        textViewBiayaUsaha= binding.textviewBiayaUsaha!!
        textViewBiayaLainnya= binding.textviewBiayaLainnya!!
        textViewPenghasilanDosen= binding.textViewPenghasilanDosen!!
        textViewKreditPajak= binding.textviewKreditPajak!!

        editTextPendapatanBruto= binding.editTextPeredaranBruto!!
        editTextPenghasilanLainnya= binding.editTextPenghasilanLainnya!!
        editTextBiayaUsaha= binding.edittextBiayaUsaha!!
        editTextBiayaLainnya= binding.edittextBiayaLainnya!!
        editTextPenghasilanDosen= binding.editTextPenghasilanDosen!!
        editTextKreditPajak= binding.editTextKreditPajak!!


        imageViewPendapatanBruto= binding.imageViewPeredaranBruto!!
        imageViewPenghasilanLainnya= binding.imageViewPenghasilanLainnya!!
        imageViewBiayaUsaha= binding.imageviewBiayaUsaha!!
        imageViewBiayaLainnya= binding.imageviewBiayaLainnya!!
        imageViewPenghasilanDosen= binding.imageViewPenghasilanDosen!!

        imageViewKreditPajak= binding.imageviewKreditPajak!!




        updateInterestCalculatorInputs(0)

    }


    private fun updateInterestCalculatorInputs(interestCalculator: Int){

        if(listProfesi[interestCalculator].ParameterDisplay==""){
            imageViewGaji.visibility = View.GONE
            imageViewPendapatan.visibility = View.GONE


            textViewGaji.visibility = View.GONE
            textViewPendapatan.visibility = View.GONE


            spinnerProfesi.visibility = View.VISIBLE

            editTextGaji.visibility = View.GONE
            editTextPendapatan.visibility = View.GONE


            textViewJumlahTanggungan.visibility = View.GONE
            textViewBiayaJabatan.visibility = View.GONE
            textViewIuranPensiun.visibility = View.GONE

            imageViewJumlahTanggungan.visibility = View.GONE
            imageViewBiayaJabatan.visibility = View.GONE
            imageViewIuranPensiun.visibility = View.GONE


            editTextJumlahTanggungan.visibility = View.GONE
            editTextBiayaJabatan.visibility = View.GONE
            editTextIuranPensiun.visibility = View.GONE
            textViewPenghasilanBruto.visibility = View.GONE
            textViewPengurangan.visibility = View.GONE

            linearPengahasilanBruto.visibility = View.GONE

            linearPengurangan.visibility = View.GONE


            textViewPendapatanBruto.visibility = View.GONE
            textViewPenghasilanLainnya.visibility = View.GONE
            textViewBiayaUsaha.visibility = View.GONE
            textViewBiayaLainnya.visibility = View.GONE

            editTextPendapatanBruto.visibility = View.GONE
            editTextPenghasilanLainnya.visibility = View.GONE
            editTextBiayaUsaha.visibility = View.GONE
            editTextBiayaLainnya.visibility = View.GONE


            imageViewPendapatanBruto.visibility = View.GONE
            imageViewPenghasilanLainnya.visibility = View.GONE
            imageViewBiayaUsaha.visibility = View.GONE
            imageViewBiayaLainnya.visibility = View.GONE


            imageViewPenghasilanDosen.visibility = View.GONE
            textViewPenghasilanDosen.visibility = View.GONE
            editTextPenghasilanDosen.visibility = View.GONE

            imageViewKreditPajak.visibility = View.VISIBLE
            textViewKreditPajak.visibility = View.VISIBLE
            editTextKreditPajak.visibility = View.VISIBLE

        }

        if(listProfesi[interestCalculator].ParameterDisplay=="Pegawai Tetap" ||
            listProfesi[interestCalculator].ParameterDisplay=="Karyawan BUMN" ||
            listProfesi[interestCalculator].ParameterDisplay=="Kepala Daerah" ||
            listProfesi[interestCalculator].ParameterDisplay=="Desainer Sebagai Pegawai Tetap"||
            listProfesi[interestCalculator].ParameterDisplay=="Musisi Orang Pribadi Pegawai Tetap" ||
            listProfesi[interestCalculator].ParameterDisplay=="Pengajar Sebagai Pegawai Tetap" ||


            listProfesi[interestCalculator].ParameterDisplay=="Pengajar sebagai Pegawai Tetap dan Menerima Tunjangan Sertifikasi"||
            listProfesi[interestCalculator].ParameterDisplay=="Tenaga Kerja Indonesia di Luar Negeri"

        ){
            imageViewGaji.visibility = View.VISIBLE
            imageViewPendapatan.visibility = View.VISIBLE

            textViewGaji.text ="Total Gaji / Tahun"
            textViewPendapatan.text ="Total Pendapatan Bonus dll / Tahun"
            textViewGaji.visibility = View.VISIBLE
            textViewPendapatan.visibility = View.VISIBLE


            spinnerProfesi.visibility = View.VISIBLE

            editTextGaji.visibility = View.VISIBLE
            editTextPendapatan.visibility = View.VISIBLE


            textViewJumlahTanggungan.visibility = View.VISIBLE
            textViewBiayaJabatan.visibility = View.VISIBLE
            textViewIuranPensiun.visibility = View.VISIBLE

            imageViewJumlahTanggungan.visibility = View.VISIBLE
            imageViewBiayaJabatan.visibility = View.VISIBLE
            imageViewIuranPensiun.visibility = View.VISIBLE


            editTextJumlahTanggungan.visibility = View.VISIBLE
            editTextBiayaJabatan.visibility = View.VISIBLE
            editTextIuranPensiun.visibility = View.VISIBLE
            textViewPenghasilanBruto.visibility = View.VISIBLE
            textViewPengurangan.visibility = View.VISIBLE


            linearPengahasilanBruto.visibility = View.VISIBLE

            linearPengurangan.visibility = View.VISIBLE

            textViewPendapatanBruto.visibility = View.GONE
            textViewPenghasilanLainnya.visibility = View.GONE
            textViewBiayaUsaha.visibility = View.GONE
            textViewBiayaLainnya.visibility = View.GONE

            editTextPendapatanBruto.visibility = View.GONE
            editTextPenghasilanLainnya.visibility = View.GONE
            editTextBiayaUsaha.visibility = View.GONE
            editTextBiayaLainnya.visibility = View.GONE


            imageViewPendapatanBruto.visibility = View.GONE
            imageViewPenghasilanLainnya.visibility = View.GONE
            imageViewBiayaUsaha.visibility = View.GONE
            imageViewBiayaLainnya.visibility = View.GONE


            imageViewPenghasilanDosen.visibility = View.GONE
            textViewPenghasilanDosen.visibility = View.GONE
            editTextPenghasilanDosen.visibility = View.GONE

            imageViewKreditPajak.visibility = View.VISIBLE
            textViewKreditPajak.visibility = View.VISIBLE
            editTextKreditPajak.visibility = View.VISIBLE

        }

        if(
            listProfesi[interestCalculator].ParameterDisplay=="Peneliti"

        ){
            imageViewGaji.visibility = View.VISIBLE
            imageViewPendapatan.visibility = View.VISIBLE

            textViewGaji.text ="Total Gaji / Tahun"
            textViewPendapatan.text ="Total Pendapatan Bonus dll / Tahun"

            textViewPendapatanBruto.text ="Total Dana Penelitian / Tahun"
            textViewGaji.visibility = View.VISIBLE
            textViewPendapatan.visibility = View.VISIBLE


            spinnerProfesi.visibility = View.VISIBLE

            editTextGaji.visibility = View.VISIBLE
            editTextPendapatan.visibility = View.VISIBLE


            textViewJumlahTanggungan.visibility = View.VISIBLE
            textViewBiayaJabatan.visibility = View.VISIBLE
            textViewIuranPensiun.visibility = View.VISIBLE

            imageViewJumlahTanggungan.visibility = View.VISIBLE
            imageViewBiayaJabatan.visibility = View.VISIBLE
            imageViewIuranPensiun.visibility = View.VISIBLE


            editTextJumlahTanggungan.visibility = View.VISIBLE
            editTextBiayaJabatan.visibility = View.VISIBLE
            editTextIuranPensiun.visibility = View.VISIBLE
            textViewPenghasilanBruto.visibility = View.VISIBLE
            textViewPengurangan.visibility = View.VISIBLE


            linearPengahasilanBruto.visibility = View.VISIBLE

            linearPengurangan.visibility = View.VISIBLE

            textViewPendapatanBruto.visibility = View.VISIBLE
            textViewPenghasilanLainnya.visibility = View.GONE
            textViewBiayaUsaha.visibility = View.GONE
            textViewBiayaLainnya.visibility = View.GONE

            editTextPendapatanBruto.visibility = View.VISIBLE
            editTextPenghasilanLainnya.visibility = View.GONE
            editTextBiayaUsaha.visibility = View.GONE
            editTextBiayaLainnya.visibility = View.GONE


            imageViewPendapatanBruto.visibility = View.VISIBLE
            imageViewPenghasilanLainnya.visibility = View.GONE
            imageViewBiayaUsaha.visibility = View.GONE
            imageViewBiayaLainnya.visibility = View.GONE


            imageViewPenghasilanDosen.visibility = View.GONE
            textViewPenghasilanDosen.visibility = View.GONE
            editTextPenghasilanDosen.visibility = View.GONE

            imageViewKreditPajak.visibility = View.VISIBLE
            textViewKreditPajak.visibility = View.VISIBLE
            editTextKreditPajak.visibility = View.VISIBLE

        }

        if(listProfesi[interestCalculator].ParameterDisplay=="Pengusaha" ||
            listProfesi[interestCalculator].ParameterDisplay=="Pedagang Eceran" ||
            listProfesi[interestCalculator].ParameterDisplay=="Arsitek Dengan Pembukuan" ||

            listProfesi[interestCalculator].ParameterDisplay=="Konsultan Dengan Pembukuan" ||
            listProfesi[interestCalculator].ParameterDisplay=="Notaris Dengan Pembukuan"){

                textViewPenghasilanBruto.text ="Total Peredaran Bruto / Tahun"


            linearPengahasilanBruto.visibility = View.VISIBLE

            linearPengurangan.visibility = View.VISIBLE
            imageViewGaji.visibility = View.GONE
            imageViewPendapatan.visibility = View.GONE


            textViewGaji.visibility = View.GONE
            textViewPendapatan.visibility = View.GONE


            spinnerProfesi.visibility = View.VISIBLE


            editTextGaji.visibility = View.GONE
            editTextPendapatan.visibility = View.GONE

            textViewPendapatanBruto.visibility = View.VISIBLE
            textViewPenghasilanLainnya.visibility = View.VISIBLE
            textViewBiayaUsaha.visibility = View.VISIBLE
            textViewBiayaLainnya.visibility = View.VISIBLE
            textViewJumlahTanggungan.visibility = View.VISIBLE

            editTextPendapatanBruto.visibility = View.VISIBLE
            editTextPenghasilanLainnya.visibility = View.VISIBLE
            editTextBiayaUsaha.visibility = View.VISIBLE
            editTextBiayaLainnya.visibility = View.VISIBLE
            editTextJumlahTanggungan.visibility = View.VISIBLE


            imageViewPendapatanBruto.visibility = View.VISIBLE
            imageViewPenghasilanLainnya.visibility = View.VISIBLE
            imageViewBiayaUsaha.visibility = View.VISIBLE
            imageViewBiayaLainnya.visibility = View.VISIBLE
            imageViewJumlahTanggungan.visibility = View.VISIBLE


            imageViewPenghasilanDosen.visibility = View.GONE
            textViewPenghasilanDosen.visibility = View.GONE
            editTextPenghasilanDosen.visibility = View.GONE

            imageViewKreditPajak.visibility = View.VISIBLE
            textViewKreditPajak.visibility = View.VISIBLE
            editTextKreditPajak.visibility = View.VISIBLE

            imageViewIuranPensiun.visibility = View.GONE
            textViewIuranPensiun.visibility = View.GONE
            editTextIuranPensiun.visibility = View.GONE

            imageViewBiayaJabatan.visibility = View.GONE
            textViewBiayaJabatan.visibility = View.GONE
            editTextBiayaJabatan.visibility = View.GONE

        }


        if(listProfesi[interestCalculator].ParameterDisplay=="Arsitek Tanpa Pembukuan" ||
            listProfesi[interestCalculator].ParameterDisplay=="Desainer yang menggunakan norma" ||
            listProfesi[interestCalculator].ParameterDisplay=="Pengajar Sebagai Bukan Pegawai" ||
            listProfesi[interestCalculator].ParameterDisplay=="Musisi Orang Pribadi Yang Menggunakan Norma"||
            listProfesi[interestCalculator].ParameterDisplay=="Notaris Orang Pribadi Yang Menggunakan Norma"||
            listProfesi[interestCalculator].ParameterDisplay=="Olahragawan"||
            listProfesi[interestCalculator].ParameterDisplay=="Pengajar sebagai Orang Pribadi yang memiliki kegiatan usaha jasa pendidikan"||
            listProfesi[interestCalculator].ParameterDisplay=="Pengajar Sebagai Subjek Luar Negeri"||


            listProfesi[interestCalculator].ParameterDisplay=="Pedagang Alat Tulis dan Buku" ||
            listProfesi[interestCalculator].ParameterDisplay=="Penulis" ||

            listProfesi[interestCalculator].ParameterDisplay=="Artis Norma Penghitungan Penghasilan Netto dengan Klasifikasi Usaha dengan Nomor KLU 90002 (Kegiatan Pekerja Seni)"
        ){



                textViewPenghasilanBruto.text ="Total Penghasilan Bruto / Tahun"







            linearPengahasilanBruto.visibility = View.VISIBLE

            linearPengurangan.visibility = View.VISIBLE
            imageViewGaji.visibility = View.GONE
            imageViewPendapatan.visibility = View.GONE


            textViewGaji.visibility = View.GONE
            textViewPendapatan.visibility = View.GONE


            spinnerProfesi.visibility = View.VISIBLE


            editTextGaji.visibility = View.GONE
            editTextPendapatan.visibility = View.GONE

            textViewPendapatanBruto.visibility = View.VISIBLE
            textViewPenghasilanLainnya.visibility = View.GONE
            textViewBiayaUsaha.visibility = View.GONE
            textViewBiayaLainnya.visibility = View.GONE
            textViewJumlahTanggungan.visibility = View.VISIBLE

            editTextPendapatanBruto.visibility = View.VISIBLE
            editTextPenghasilanLainnya.visibility = View.GONE
            editTextBiayaUsaha.visibility = View.GONE
            editTextBiayaLainnya.visibility = View.GONE
            editTextJumlahTanggungan.visibility = View.VISIBLE


            imageViewPendapatanBruto.visibility = View.VISIBLE
            imageViewPenghasilanLainnya.visibility = View.GONE
            imageViewBiayaUsaha.visibility = View.GONE
            imageViewBiayaLainnya.visibility = View.GONE
            imageViewJumlahTanggungan.visibility = View.VISIBLE

            textViewBiayaJabatan.visibility = View.GONE
            imageViewBiayaJabatan.visibility = View.GONE
            editTextBiayaJabatan.visibility = View.GONE

            textViewIuranPensiun.visibility = View.GONE
            imageViewIuranPensiun.visibility = View.GONE
            editTextIuranPensiun.visibility = View.GONE


            imageViewPenghasilanDosen.visibility = View.GONE
            textViewPenghasilanDosen.visibility = View.GONE
            editTextPenghasilanDosen.visibility = View.GONE

            imageViewKreditPajak.visibility = View.VISIBLE
            textViewKreditPajak.visibility = View.VISIBLE
            editTextKreditPajak.visibility = View.VISIBLE


        }

        if(listProfesi[interestCalculator].ParameterDisplay=="Artis Norma Penghitungan Penghasilan Netto atau PPh Final senilai 0,5%" ||
            listProfesi[interestCalculator].ParameterDisplay=="Desainer Royalti" ||
            listProfesi[interestCalculator].ParameterDisplay=="Musisi Royalti"||
            listProfesi[interestCalculator].ParameterDisplay=="Musisi Memiliki Kursus Musik"||
            listProfesi[interestCalculator].ParameterDisplay=="Desainer Memiliki Usaha Desain dan Memilih Dikenakan PP 23"
        ){



            textViewPenghasilanBruto.text ="Total Penghasilan / Tahun"



            linearPengahasilanBruto.visibility = View.VISIBLE

            linearPengurangan.visibility = View.VISIBLE
            imageViewGaji.visibility = View.GONE
            imageViewPendapatan.visibility = View.GONE


            textViewGaji.visibility = View.GONE
            textViewPendapatan.visibility = View.GONE


            spinnerProfesi.visibility = View.VISIBLE


            editTextGaji.visibility = View.GONE
            editTextPendapatan.visibility = View.GONE

            textViewPendapatanBruto.visibility = View.VISIBLE
            textViewPenghasilanLainnya.visibility = View.GONE
            textViewBiayaUsaha.visibility = View.GONE
            textViewBiayaLainnya.visibility = View.GONE
            textViewJumlahTanggungan.visibility = View.VISIBLE

            editTextPendapatanBruto.visibility = View.VISIBLE
            editTextPenghasilanLainnya.visibility = View.GONE
            editTextBiayaUsaha.visibility = View.GONE
            editTextBiayaLainnya.visibility = View.GONE
            editTextJumlahTanggungan.visibility = View.VISIBLE


            imageViewPendapatanBruto.visibility = View.VISIBLE
            imageViewPenghasilanLainnya.visibility = View.GONE
            imageViewBiayaUsaha.visibility = View.GONE
            imageViewBiayaLainnya.visibility = View.GONE
            imageViewJumlahTanggungan.visibility = View.VISIBLE

            textViewBiayaJabatan.visibility = View.GONE
            imageViewBiayaJabatan.visibility = View.GONE
            editTextBiayaJabatan.visibility = View.GONE

            textViewIuranPensiun.visibility = View.GONE
            imageViewIuranPensiun.visibility = View.GONE
            editTextIuranPensiun.visibility = View.GONE


            imageViewPenghasilanDosen.visibility = View.GONE
            textViewPenghasilanDosen.visibility = View.GONE
            editTextPenghasilanDosen.visibility = View.GONE


            imageViewKreditPajak.visibility = View.VISIBLE
            textViewKreditPajak.visibility = View.VISIBLE
            editTextKreditPajak.visibility = View.VISIBLE


        }

        if(listProfesi[interestCalculator].ParameterDisplay=="Dokter"
        ){

            textViewGaji.text ="Total Gaji (Dari Pekerjaan Karyawan) / Tahun"
            textViewPendapatan.text ="Total Pendapatan Bonus dll / Tahun (Dari Pekerjaan Karyawan)"
            textViewPendapatanBruto.text ="Total Penghasilan (kegiatan usaha dan pekerjaan bebas)"
            textViewPenghasilanLainnya.text ="Total Penghasilan Lainnya (kegiatan usaha dan pekerjaan bebas)"


            imageViewGaji.visibility = View.VISIBLE
            imageViewPendapatan.visibility = View.VISIBLE


            textViewGaji.visibility = View.VISIBLE
            textViewPendapatan.visibility = View.VISIBLE


            spinnerProfesi.visibility = View.VISIBLE

            editTextGaji.visibility = View.VISIBLE
            editTextPendapatan.visibility = View.VISIBLE


            textViewJumlahTanggungan.visibility = View.VISIBLE
            textViewBiayaJabatan.visibility = View.VISIBLE
            textViewIuranPensiun.visibility = View.VISIBLE

            imageViewJumlahTanggungan.visibility = View.VISIBLE
            imageViewBiayaJabatan.visibility = View.VISIBLE
            imageViewIuranPensiun.visibility = View.VISIBLE


            editTextJumlahTanggungan.visibility = View.VISIBLE
            editTextBiayaJabatan.visibility = View.VISIBLE
            editTextIuranPensiun.visibility = View.VISIBLE
            textViewPenghasilanBruto.visibility = View.VISIBLE
            textViewPengurangan.visibility = View.VISIBLE


            linearPengahasilanBruto.visibility = View.VISIBLE

            linearPengurangan.visibility = View.VISIBLE

            textViewPendapatanBruto.visibility = View.VISIBLE
            textViewPenghasilanLainnya.visibility = View.VISIBLE
            textViewBiayaUsaha.visibility = View.GONE
            textViewBiayaLainnya.visibility = View.GONE

            editTextPendapatanBruto.visibility = View.VISIBLE
            editTextPenghasilanLainnya.visibility = View.VISIBLE
            editTextBiayaUsaha.visibility = View.GONE
            editTextBiayaLainnya.visibility = View.GONE


            imageViewPendapatanBruto.visibility = View.VISIBLE
            imageViewPenghasilanLainnya.visibility = View.VISIBLE
            imageViewBiayaUsaha.visibility = View.GONE
            imageViewBiayaLainnya.visibility = View.GONE


            imageViewPenghasilanDosen.visibility = View.VISIBLE
            textViewPenghasilanDosen.visibility = View.VISIBLE
            editTextPenghasilanDosen.visibility = View.VISIBLE

            imageViewKreditPajak.visibility = View.VISIBLE
            textViewKreditPajak.visibility = View.VISIBLE
            editTextKreditPajak.visibility = View.VISIBLE

        }

        if(
            listProfesi[interestCalculator].ParameterDisplay=="Konsultan"
        ){

            textViewGaji.text ="Total Gaji (Dari Pekerjaan Karyawan) / Tahun"
            textViewPendapatan.text ="Total Pendapatan Bonus dll / Tahun (dari Pekerjaan Karyawan)"
            textViewPendapatanBruto.text ="Total Penghasilan (kegiatan usaha dan pekerjaan bebas)"
            textViewPenghasilanLainnya.text ="Total Penghasilan Lainnya (kegiatan usaha dan pekerjaan bebas)"


            imageViewGaji.visibility = View.VISIBLE
            imageViewPendapatan.visibility = View.VISIBLE


            textViewGaji.visibility = View.VISIBLE
            textViewPendapatan.visibility = View.VISIBLE


            spinnerProfesi.visibility = View.VISIBLE

            editTextGaji.visibility = View.VISIBLE
            editTextPendapatan.visibility = View.VISIBLE


            textViewJumlahTanggungan.visibility = View.VISIBLE
            textViewBiayaJabatan.visibility = View.VISIBLE
            textViewIuranPensiun.visibility = View.VISIBLE

            imageViewJumlahTanggungan.visibility = View.VISIBLE
            imageViewBiayaJabatan.visibility = View.VISIBLE
            imageViewIuranPensiun.visibility = View.VISIBLE


            editTextJumlahTanggungan.visibility = View.VISIBLE
            editTextBiayaJabatan.visibility = View.VISIBLE
            editTextIuranPensiun.visibility = View.VISIBLE
            textViewPenghasilanBruto.visibility = View.VISIBLE
            textViewPengurangan.visibility = View.VISIBLE


            linearPengahasilanBruto.visibility = View.VISIBLE

            linearPengurangan.visibility = View.VISIBLE

            textViewPendapatanBruto.visibility = View.VISIBLE
            textViewPenghasilanLainnya.visibility = View.VISIBLE
            textViewBiayaUsaha.visibility = View.GONE
            textViewBiayaLainnya.visibility = View.GONE

            editTextPendapatanBruto.visibility = View.VISIBLE
            editTextPenghasilanLainnya.visibility = View.VISIBLE
            editTextBiayaUsaha.visibility = View.GONE
            editTextBiayaLainnya.visibility = View.GONE


            imageViewPendapatanBruto.visibility = View.VISIBLE
            imageViewPenghasilanLainnya.visibility = View.VISIBLE
            imageViewBiayaUsaha.visibility = View.GONE
            imageViewBiayaLainnya.visibility = View.GONE


            imageViewPenghasilanDosen.visibility = View.GONE
            textViewPenghasilanDosen.visibility = View.GONE
            editTextPenghasilanDosen.visibility = View.GONE

            imageViewKreditPajak.visibility = View.VISIBLE
            textViewKreditPajak.visibility = View.VISIBLE
            editTextKreditPajak.visibility = View.VISIBLE

        }
        if(
            listProfesi[interestCalculator].ParameterDisplay=="Pengacara"
        ){

            textViewGaji.text ="Total Gaji (Dari Pekerjaan Karyawan) / Tahun"
            textViewPendapatan.text ="Total Pendapatan Bonus dll / Tahun (dari Pekerjaan Karyawan)"
            textViewPendapatanBruto.text ="Total Penghasilan (dari kegiatan usaha dan pekerjaan bebas)"
            textViewPenghasilanLainnya.text ="Total Penghasilan Lainnya (dari kegiatan usaha dan pekerjaan bebas)"

            imageViewGaji.visibility = View.VISIBLE
            imageViewPendapatan.visibility = View.VISIBLE


            textViewGaji.visibility = View.VISIBLE
            textViewPendapatan.visibility = View.VISIBLE


            spinnerProfesi.visibility = View.VISIBLE

            editTextGaji.visibility = View.VISIBLE
            editTextPendapatan.visibility = View.VISIBLE


            textViewJumlahTanggungan.visibility = View.VISIBLE
            textViewBiayaJabatan.visibility = View.VISIBLE
            textViewIuranPensiun.visibility = View.VISIBLE

            imageViewJumlahTanggungan.visibility = View.VISIBLE
            imageViewBiayaJabatan.visibility = View.VISIBLE
            imageViewIuranPensiun.visibility = View.VISIBLE


            editTextJumlahTanggungan.visibility = View.VISIBLE
            editTextBiayaJabatan.visibility = View.VISIBLE
            editTextIuranPensiun.visibility = View.VISIBLE
            textViewPenghasilanBruto.visibility = View.VISIBLE
            textViewPengurangan.visibility = View.VISIBLE


            linearPengahasilanBruto.visibility = View.VISIBLE

            linearPengurangan.visibility = View.VISIBLE

            textViewPendapatanBruto.visibility = View.VISIBLE
            textViewPenghasilanLainnya.visibility = View.VISIBLE
            textViewBiayaUsaha.visibility = View.GONE
            textViewBiayaLainnya.visibility = View.GONE

            editTextPendapatanBruto.visibility = View.VISIBLE
            editTextPenghasilanLainnya.visibility = View.VISIBLE
            editTextBiayaUsaha.visibility = View.GONE
            editTextBiayaLainnya.visibility = View.GONE


            imageViewPendapatanBruto.visibility = View.VISIBLE
            imageViewPenghasilanLainnya.visibility = View.VISIBLE
            imageViewBiayaUsaha.visibility = View.GONE
            imageViewBiayaLainnya.visibility = View.GONE


            imageViewPenghasilanDosen.visibility = View.GONE
            textViewPenghasilanDosen.visibility = View.GONE
            editTextPenghasilanDosen.visibility = View.GONE

            imageViewKreditPajak.visibility = View.VISIBLE
            textViewKreditPajak.visibility = View.VISIBLE
            editTextKreditPajak.visibility = View.VISIBLE

        }



    }




    private fun loadAds() {


        adView = binding.adView!!


        MobileAds.initialize(requireContext())

        val testDeviceIds = listOf(
            "A27D54F21D395038C90E85A8C00C1DBD"
        )
        val configuration =
            RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)


        // on below line we are
        // initializing our ad request.
        adRequest = AdRequest.Builder().build()




        // on below line we are `loadin`g our
        // ad view with the ad request
        adView.loadAd(adRequest)




    }

    fun ConstraintLayout.updateConstraints(applier: (cs: ConstraintSet) -> Unit) {
        this.let {
            ConstraintSet().let { cs ->
                cs.clone(this)
                applier(cs)
                cs.applyTo(this)
            }
        }
    }


    private var disclaimer = mutableListOf<ParameterModel>()


    private fun getDisclaimer() {

        disclaimer = java.util.ArrayList<ParameterModel>()


        disclaimer.add(
            ParameterModel(
                "DISCLAIMER\n" +
                        "\n" +
                        "1. Hasil Pajak Penghasilan Aplikasi ini bersifat estimasi tidak merupakan angka simulasi sebenarnya dari dirjen pajak.\n" +
                        "\n" +
                        "2. Pihak Pengguna aplikasi Pajak Penghasilan tidak akan mengajukan tuntutan hukum kepada pihak pengembang aplikasi Pajak Penghasilan jika ada kondisi yang merugikan pengguna baik langsung atau tidak langsung.\n" +
                        "\n" +
                        "3. Pajak Penghasilan bukan lah institusi penasihat keuangan dan tidak memberikan nasehat keuangan . \n" +
                        "\n" +
                        "4. Pajak Penghasilan tidak menghimpunan dana dari pengguna aplikasi.\n" +
                        "\n" +
                        "5. Pajak Penghasilan berusaha semaksimal mungkin menerapkan UU ITE Negara Kesatuan Republik Indonesia. \n" +
                        "\n" +
                        "Selamat menggunakan Aplikasi Pajak Penghasilan semoga bermanfaat! \n" +
                        "\n" +
                        "Apakah Anda setuju?", "0", "disclaimer"
            )
        )

        if (disclaimer.size > 0) {

            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage(disclaimer[0].ParameterDisplay)
                .setCancelable(false)
                .setPositiveButton("Setuju") { dialog, id ->


                    SharedPreferencesTools.saveData(requireContext(), "disclaimer", "setuju")

                }
                .setNegativeButton("Tidak") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()

                    requireActivity().finishAffinity()
                }

            val alert = builder.create()
            alert.show()
        }



        flag = true


    }



    private lateinit var layoutManager: LinearLayoutManager


    override fun onPause() {
        Log.e("DEBUG", "OnPause of HomeFragment")
        super.onPause()


    }

    override fun onResume() {
        Log.e("DEBUG", "OnPause of HomeFragment")
        super.onResume()

        //motionProgressLoaderView?.stopLoader()



    }

    private var flag: Boolean = false


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun validate() : Boolean {

        var validasi = true

        if (spinnerProfesi.selectedIndex == 0 || spinnerProfesi.selectedIndex == -1) {

            spinnerProfesi.error = "Silahkan Input Profesi"
            spinnerProfesi.hint = "Silahkan Input Profesi"

            validasi = false

            spinnerProfesi.requestFocus()

            GeneralTools.toast(requireContext(), "Silahkan Input Profesi", 0)

            return validasi
        }





        if (editTextGaji.text.toString() == "" || editTextGaji.text.toString().replace("IDR ","") == "0") {
            if( listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pegawai Tetap" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Karyawan BUMN" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Kepala Daerah" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Desainer Sebagai Pegawai Tetap"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Musisi Orang Pribadi Pegawai Tetap" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar Sebagai Pegawai Tetap" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Peneliti" ||

                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar sebagai Pegawai Tetap dan Menerima Tunjangan Sertifikasi" ||

                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Tenaga Kerja Indonesia di Luar Negeri") {

                editTextGaji.error = "Silahkan Input Gaji / Tahun"
                editTextGaji.hint = "Silahkan Input Gaji / Tahun"

                editTextGaji.requestFocus()

                validasi = false

                GeneralTools.toast(requireContext(), "Silahkan Input Gaji / Tahun", 0)

                return validasi
            }

        }

        if (editTextPenghasilanDosen.text.toString() == "") {
            if(listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Dokter"
             ) {

                editTextPendapatan.error = "Silahkan Input Penghasilan Dosen / Tahun"
                editTextPendapatan.hint = "Silahkan Input Penghasilan Dosen / Tahun"

                editTextPendapatan.requestFocus()

                validasi = false

                GeneralTools.toast(requireContext(), "Silahkan Input Penghasilan Dosen / Tahun", 0)

                return validasi
            }

        }

        if (editTextPendapatan.text.toString() == "") {
            if(listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pegawai Tetap" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Karyawan BUMN" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Kepala Daerah" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Desainer Sebagai Pegawai Tetap"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Musisi Orang Pribadi Pegawai Tetap" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar Sebagai Pegawai Tetap"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Peneliti" ||

                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar sebagai Pegawai Tetap dan Menerima Tunjangan Sertifikasi"  ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Dokter"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengacara" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Tenaga Kerja Indonesia di Luar Negeri") {

                editTextPendapatan.error = "Silahkan Input Pendapatan Bonus dll / Tahun"
                editTextPendapatan.hint = "Silahkan Input Pendapatan Bonus dll / Tahun"

                editTextPendapatan.requestFocus()

                validasi = false

                GeneralTools.toast(requireContext(), "Silahkan Input Pendapatan Bonus dll / Tahun", 0)

                return validasi
            }

        }


        if (editTextJumlahTanggungan.text.toString() == "" ) {
            if(listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pegawai Tetap" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Karyawan BUMN" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Kepala Daerah" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Desainer Sebagai Pegawai Tetap"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Musisi Orang Pribadi Pegawai Tetap" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar Sebagai Pegawai Tetap"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Peneliti"  ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar Sebagai Bukan Pegawai"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar sebagai Pegawai Tetap dan Menerima Tunjangan Sertifikasi" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Arsitek Tanpa Pembukuan" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Desainer yang menggunakan norma" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Konsultan"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Musisi Orang Pribadi Yang Menggunakan Norma"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Notaris Orang Pribadi Yang Menggunakan Norma"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Olahragawan"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar sebagai Orang Pribadi yang memiliki kegiatan usaha jasa pendidikan"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pedagang Alat Tulis dan Buku" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Penulis"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Dokter"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengacara" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Tenaga Kerja Indonesia di Luar Negeri") {

                editTextJumlahTanggungan.error = "Silahkan Input Jumlah Tanggungan (orang)"
                editTextJumlahTanggungan.hint = "Silahkan Input Jumlah Tanggungan (orang)"

                editTextJumlahTanggungan.requestFocus()

                validasi = false

                GeneralTools.toast(requireContext(), "Silahkan Input Jumlah Tanggungan (orang)", 0)

                return validasi
            }

        }


        if (editTextBiayaJabatan.text.toString() == "" || editTextBiayaJabatan.text.toString() == "0") {
            if(listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pegawai Tetap" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Karyawan BUMN" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Kepala Daerah" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Desainer Sebagai Pegawai Tetap"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Musisi Orang Pribadi Pegawai Tetap" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar Sebagai Pegawai Tetap" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Peneliti" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar Sebagai Bukan Pegawai"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar sebagai Pegawai Tetap dan Menerima Tunjangan Sertifikasi"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengacara"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Dokter" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Tenaga Kerja Indonesia di Luar Negeri") {

                editTextBiayaJabatan.error = "Silahkan Input Biaya Jabatan atau Pensiun / Tahun"
                editTextBiayaJabatan.hint = "Silahkan Input Biaya Jabatan atau Pensiun / Tahun"

                editTextBiayaJabatan.requestFocus()

                validasi = false

                GeneralTools.toast(requireContext(), "Silahkan Input Biaya Jabatan atau Pensiun / Tahun", 0)

                return validasi
            }

        }

        if (editTextIuranPensiun.text.toString() == "" || editTextIuranPensiun.text.toString() == "0") {
            if(listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pegawai Tetap" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Karyawan BUMN" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Kepala Daerah" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Desainer Sebagai Pegawai Tetap"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Musisi Orang Pribadi Pegawai Tetap" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar Sebagai Pegawai Tetap"  ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Peneliti" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar Sebagai Bukan Pegawai"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar sebagai Pegawai Tetap dan Menerima Tunjangan Sertifikasi"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Dokter"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengacara" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Tenaga Kerja Indonesia di Luar Negeri") {

                editTextIuranPensiun.error = "Silahkan Input Pensiunan / Tahun"
                editTextIuranPensiun.hint = "Silahkan Input Pensiunan / Tahun"

                editTextIuranPensiun.requestFocus()

                validasi = false

                GeneralTools.toast(requireContext(), "Silahkan Input Pensiunan / Tahun", 0)

                return validasi
            }

        }



        if (editTextPendapatanBruto.text.toString() == "" || editTextPendapatanBruto.text.toString().replace("IDR ","") == "0") {
            if( listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengusaha" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pedagang Eceran" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Arsitek Dengan Pembukuan" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar Sebagai Bukan Pegawai" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Konsultan Dengan Pembukuan" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Notaris Dengan Pembukuan" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Arsitek Tanpa Pembukuan" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Desainer yang menggunakan norma" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Konsultan"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Musisi Orang Pribadi Yang Menggunakan Norma"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Notaris Orang Pribadi Yang Menggunakan Norma"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Olahragawan"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar sebagai Orang Pribadi yang memiliki kegiatan usaha jasa pendidikan"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Penulis" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pedagang Alat Tulis dan Buku"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Dokter"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengacara" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Artis Norma Penghitungan Penghasilan Netto atau PPh Final senilai 0,5%" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Artis Norma Penghitungan Penghasilan Netto dengan Klasifikasi Usaha dengan Nomor KLU 90002 (Kegiatan Pekerja Seni)" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Desainer Royalti"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Desainer Memiliki Usaha Desain dan Memilih Dikenakan PP 23"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Musisi Royalti"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Musisi Memiliki Kursus Musik"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengajar Sebagai Subjek Luar Negeri"
            ) {

                editTextPendapatanBruto.error = "Silahkan Input " + textViewPendapatanBruto.text
                editTextPendapatanBruto.hint = "Silahkan Input " + textViewPendapatanBruto.text

                editTextPendapatanBruto.requestFocus()

                validasi = false

                GeneralTools.toast(requireContext(), "Silahkan Input " + textViewPendapatanBruto.text, 0)

                return validasi
            }

        }


        if (editTextPenghasilanLainnya.text.toString() == "" ) {
            if( listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengusaha" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pedagang Eceran" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Arsitek Dengan Pembukuan" ||

                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Konsultan Dengan Pembukuan" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Notaris Dengan Pembukuan" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay==""||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay==""
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Dokter"
                ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengacara") {

                editTextPenghasilanLainnya.error = "Silahkan Input Penghasilan Lainnya / Tahun"
                editTextPenghasilanLainnya.hint = "Silahkan Input Penghasilan Lainnya / Tahun"

                editTextPenghasilanLainnya.requestFocus()

                validasi = false

                GeneralTools.toast(requireContext(), "Silahkan Input Penghasilan Lainnya / Tahun", 0)

                return validasi
            }

        }

        if (editTextBiayaUsaha.text.toString() == "" ) {
            if( listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengusaha" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pedagang Eceran" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Arsitek Dengan Pembukuan" ||

                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Konsultan Dengan Pembukuan" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Notaris Dengan Pembukuan"||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[ spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[ spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="") {

                editTextBiayaUsaha.error = "Silahkan Input Biaya Usaha / Tahun"
                editTextBiayaUsaha.hint = "Silahkan Input Biaya Usaha / Tahun"

                editTextBiayaUsaha.requestFocus()

                validasi = false

                GeneralTools.toast(requireContext(), "Silahkan Input Biaya Usaha / Tahun", 0)

                return validasi
            }

        }

        if (editTextBiayaLainnya.text.toString() == "" ) {
            if( listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pengusaha" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Pedagang Eceran" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Arsitek Dengan Pembukuan" ||

                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Konsultan Dengan Pembukuan" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="Notaris Dengan Pembukuan" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay==""||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="" ||
                listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay=="") {

                editTextBiayaLainnya.error = "Silahkan Input Biaya Lainnya / Tahun"
                editTextBiayaLainnya.hint = "Silahkan Input Biaya Lainnya / Tahun"

                editTextBiayaLainnya.requestFocus()

                validasi = false

                GeneralTools.toast(requireContext(), "Silahkan Input Biaya Lainnya / Tahun", 0)

                return validasi
            }

        }
        return validasi



    }

    private var interstitialAd: InterstitialAd? = null


    private fun showInterstitial() {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.


        if (interstitialAd != null) {
            interstitialAd!!.show(requireActivity())
        } else {
            hitung()
        }

    }


    private fun hitung() {


        ActivityTools.startKalkulatorPajakPenghasilanResultActivity(
            requireContext(),
            editTextGaji.text.toString().replace("IDR ", "").replace(",", ""),
            editTextPendapatan.text.toString().replace("IDR ", "").replace(",", ""),
            editTextJumlahTanggungan.text.toString(),
            editTextBiayaJabatan.text.toString().replace("IDR ", "").replace(",", ""),
            editTextIuranPensiun.text.toString().replace("IDR ", "").replace(",", ""),
            editTextPendapatanBruto.text.toString().replace("IDR ", "").replace(",", ""),
            editTextPenghasilanLainnya.text.toString().replace("IDR ", "").replace(",", ""),
            editTextBiayaUsaha.text.toString().replace("IDR ", "").replace(",", ""),
            editTextBiayaLainnya.text.toString().replace("IDR ", "").replace(",", ""),

            listProfesi[spinnerProfesi.selectedIndex].ParameterDisplay,

                    editTextPenghasilanDosen.text.toString().replace("IDR ", "").replace(",", ""),
            editTextKreditPajak.text.toString().replace("IDR ", "").replace(",", "")

            )

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


                                hitung()

                                loadInterstitialAd()

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




}