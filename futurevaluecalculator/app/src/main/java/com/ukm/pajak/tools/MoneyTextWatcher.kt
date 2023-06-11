package com.ukm.pajak.tools

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*


class MoneyTextWatcher(editText: EditText, lang: String, country: String) : TextWatcher {
    private val editTextWeakReference: WeakReference<EditText> = WeakReference(editText)

    private val lang: String = lang
    private val country: String = country
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        val editText = editTextWeakReference.get()
        if (editText == null || editText.text.toString().isEmpty() || editText.text.toString().contains('.')) {
            return
        }
        editText.removeTextChangedListener(this)
        val parsed = parseCurrencyValue(
            editText.text.toString(),
            lang, country
        )
        val formatted = formatter.format(parsed)
        editText.setText(formatted)


        if ((lang == "vi" && country == "VN") ||
            (lang == "hy" && country == "AM") ||
            (lang == "az" && country == "AZ") ||


            (lang == "ca" && country == "ES") ||

            (lang == "et" && country == "EE") ||
            (lang == "fi" && country == "FI") ||
            (lang == "fr" && country == "BE") ||
            (lang == "fr" && country == "CA") ||
            (lang == "fr" && country == "FR") ||
            (lang == "fr" && country == "LU") ||
            (lang == "fr" && country == "MC") ||

            (lang == "gl" && country == "ES") ||
            (lang == "el" && country == "GR") ||
            (lang == "ka" && country == "GE") ||
            (lang == "he" && country == "IL") ||
            (lang == "it" && country == "IT") ||
            (lang == "kk" && country == "KZ") ||
            (lang == "it" && country == "LT") ||
            (lang == "lv" && country == "LV") ||
            (lang == "ps" && country == "AR") ||
            (lang == "pt" && country == "PT") ||
            (lang == "ru" && country == "RU") ||
            (lang == "se" && country == "FI") ||
            (lang == "sk" && country == "SK") ||
            (lang == "sl" && country == "SI") ||
            (lang == "es" && country == "ES") ||
            (lang == "sr" && country == "SP") ||
            (lang == "sv" && country == "FI") ||
            (lang == "tt" && country == "RU") ||

            (lang == "eu" && country == "ES") ||
            (lang == "de" && country == "DE") ||

            (lang == "de" && country == "LU") ||
            (lang == "vi" && country == "VN")
        ) {
            if (formatted.length <= editText.text.length) {
                editText.setSelection(formatted.length - 3)
            }


        } else if ((lang == "bs" && country == "BA") ||
            (lang == "hr" && country == "BA") ||
            (lang == "hr" && country == "HR") ||
            (lang == "fo" && country == "FO") ||
            (lang == "hu" && country == "HU") ||
            (lang == "nn" && country == "NO") ||
            (lang == "pl" && country == "PL") ||
            (lang == "se" && country == "NO") ||
            (lang == "se" && country == "SE") ||
            (lang == "sr" && country == "BA") ||
            (lang == "be" && country == "BY") ||
            (lang == "cs" && country == "CZ") ||
            (lang == "sv" && country == "SE")

        ) {
            if (formatted.length <= editText.text.length) {
                editText.setSelection(formatted.length - 4)
            }

        } else if ((lang == "da" && country == "DK") ||
            (lang == "fr" && country == "CH") ||
            (lang == "is" && country == "IS") ||
            (lang == "ky" && country == "KG") ||
            (lang == "ro" && country == "RO") ||
            (lang == "bg" && country == "BG") ||
            (lang == "uk" && country == "UA")

        ) {
            if (formatted.length <= editText.text.length) {
                editText.setSelection(formatted.length - 5)
            }

        }
        else if
                     ( (lang == "mk" && country == "MK") || (lang == "uz" && country == "UZ"))

        {
            if (formatted.length <= editText.text.length) {
                editText.setSelection(formatted.length - 6)
            }

        }
        else {
            if (formatted.length <= editText.text.length) {
                editText.setSelection(formatted.length)
            }
        }
        editText.addTextChangedListener(this)
    }

    companion object {
        private var locale = Locale("id", "ID")
        private var formatter = NumberFormat.getCurrencyInstance(locale) as DecimalFormat
        fun parseCurrencyValue(value: String, lang: String, country: String): BigDecimal {
            try {

                locale = Locale("en", country)

                formatter = NumberFormat.getCurrencyInstance(locale) as DecimalFormat

                formatter.maximumFractionDigits = 0
                formatter.roundingMode = RoundingMode.FLOOR
                val symbol = DecimalFormatSymbols(locale)
                symbol.currencySymbol = symbol.currencySymbol + " "
                formatter.decimalFormatSymbols = symbol
                val replaceRegex = String.format(
                    "[%s,.\\s]",
                    Objects.requireNonNull(formatter.currency).getSymbol(locale)
                )
                val currencyValue = value.replace(replaceRegex.toRegex(), "")
                return BigDecimal(currencyValue)
            } catch (e: Exception) {
                Log.e("App", e.message, e)
            }
            return BigDecimal.ZERO
        }
    }

    init {
        formatter.maximumFractionDigits = 0
        formatter.roundingMode = RoundingMode.FLOOR
        val symbol = DecimalFormatSymbols(locale)
        symbol.currencySymbol = symbol.currencySymbol + " "
        formatter.decimalFormatSymbols = symbol
    }
}