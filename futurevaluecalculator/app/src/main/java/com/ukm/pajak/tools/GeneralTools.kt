package com.ukm.pajak.tools

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.Gravity
import java.text.NumberFormat
import java.util.*


class GeneralTools {

    companion object {


        fun getEmail(context: Context?): String? {
            val accountManager = AccountManager.get(context)
            val account = getAccount(accountManager)
            return account?.name
        }

        private fun getAccount(accountManager: AccountManager): Account? {
            val accounts = accountManager.getAccountsByType("com.google")
            val account: Account?
            account = if (accounts.size > 0) {
                accounts[0]
            } else {
                null
            }
            return account
        }


        fun toast(context:Context, msg:String, period: Int){



            val toast = android.widget.Toast.makeText(
                context,
                msg,
                period
            )

            toast.setGravity( Gravity.CENTER, 0, 0)


            toast.show()

        }


        fun formatMoney(number: Double,lang:String,country:String): String {
            val localeID = Locale(lang, country)
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)

            numberFormat.maximumFractionDigits = 10

            numberFormat.minimumFractionDigits =0

            return numberFormat.format(number).toString()
        }


        fun checkForInternet(context: Context): Boolean {



            // register activity with the connectivity manager service
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            // if the android version is equal to M
            // or greater we need to use the
            // NetworkCapabilities to check what type of
            // network has the internet connection
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                // Returns a Network object corresponding to
                // the currently active default data network.
                val network = connectivityManager.activeNetwork ?: return false

                // Representation of the capabilities of an active network.
                val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

                return when {
                    // Indicates this network uses a Wi-Fi transport,
                    // or WiFi has network connectivity
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                    // Indicates this network uses a Cellular transport. or
                    // Cellular has network connectivity
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                    // else return false
                    else -> false
                }
            } else {
                // if the android version is below M
                @Suppress("DEPRECATION") val networkInfo =
                    connectivityManager.activeNetworkInfo ?: return false
                @Suppress("DEPRECATION")
                return networkInfo.isConnected
            }
        }
    }

}