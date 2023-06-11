package com.ukm.pajak.models

import java.util.*

data class DataTrackingModel(
    var gaji: Double,
    val bonus: Double,
    val tanggungan: Int,
    val biayajabatan: Double,
    val biayausaha: Double,

    val biayausahalainnya: Double,
    val iuranpensiun: Double,
    val kreditpajak: Double,
    val bruto: Double,
    val penghasilanlainnya: Double,
    val biayalainnya: Double,

    val penghasilandosen: Double,
    val pajak: Int
)