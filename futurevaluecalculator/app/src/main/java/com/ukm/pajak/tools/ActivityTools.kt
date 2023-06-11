package com.ukm.pajak.tools

import android.content.Context
import android.content.Intent
import com.ukm.pajak.*
import com.ukm.pajak.ui.reference.ReferenceDetailActivity

class ActivityTools {




    companion object {


        fun startReferenceDetailActivity(context: Context, title: String, description: String){

            val intent = Intent(context, ReferenceDetailActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("description", description)
            context.startActivity(intent)
        }

        fun startKalkulatorPajakPenghasilanResultActivity( context: Context, gaji: String, bonus: String,tanggungan:String, biayaJabatan: String, iuranPensiun: String
        ,pendapatanBruto : String
        ,pendapatanLainnya : String
        ,biayaUsaha : String
        , biayaUsahaLainnya : String, profesi: String
        ,penghasilanDosen: String
        , kreditPajak : String){

            val intent = Intent(context, ResultActivity::class.java)

            intent.putExtra("gaji", gaji)
            intent.putExtra("bonus", bonus)
            intent.putExtra("tanggungan", tanggungan)

            intent.putExtra("biayaJabatan", biayaJabatan)


            intent.putExtra("iuranPensiun", iuranPensiun)

            intent.putExtra("pendapatanBruto", pendapatanBruto)
            intent.putExtra("pendapatanLainnya", pendapatanLainnya)
            intent.putExtra("biayaUsaha", biayaUsaha)
            intent.putExtra("biayaUsahaLainnya", biayaUsahaLainnya)
            intent.putExtra("profesi", profesi)

            intent.putExtra("penghasilanDosen", penghasilanDosen)
            intent.putExtra("kreditPajak", kreditPajak)

            context.startActivity(intent)

        }

    }
}