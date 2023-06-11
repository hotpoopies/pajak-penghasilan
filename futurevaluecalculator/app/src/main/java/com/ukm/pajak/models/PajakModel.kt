package com.ukm.pajak.models

data class PajakModel (
    var gaji: Float,
    val bonus: Float,
    val tanggungan: Int,
    val biayajabatan: Float,
    val iuranpensiun: Float,
    val kreditpajak: Float,
    val bruto: Float,
    val penghasilanlainny: Float,
    val biayalainnya: Float,
    val pajak: Int,
)