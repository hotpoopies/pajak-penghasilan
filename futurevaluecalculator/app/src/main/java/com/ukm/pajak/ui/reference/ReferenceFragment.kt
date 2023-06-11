package com.ukm.pajak.ui.reference

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*


import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ukm.pajak.R
import com.ukm.pajak.databinding.FragmentReferenceBinding
import com.ukm.pajak.models.ReferenceModel
import com.ukm.pajak.tools.ActivityTools
import java.util.*


class ReferenceFragment : Fragment()   {

    private var _binding: FragmentReferenceBinding? = null

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
        var newList =  arrayListOf<ReferenceModel>()

        newList.add(ReferenceModel("Time Value Of Money","The time value of money refers to the concept that money received or paid out at different points in time is not equivalent in value. In other words, a dollar today is worth more than a dollar received at a future date due to the potential earning power of that money.\n" +
                "\n" +
                "This is because money can be invested and earn interest over time. Therefore, the present value of money is higher than the future value of the same amount of money.\n" +
                "\n" +
                "This concept is important in finance and investing because it helps in making decisions about whether to invest money now or in the future, and how much return should be expected for a given investment. It is also used to calculate the present value of future cash flows, such as investment returns, loan repayments, and other financial transactions."))


        newList.add(ReferenceModel("Future Value","Future value (FV) refers to the amount of money that an investment will be worth at a specified point in the future, assuming a certain interest rate or rate of return. It is the value of an investment after a specified period of time, based on the compounding of interest.\n" +
                "\n" +
                "The future value of an investment can be calculated using a formula that takes into account the present value of the investment, the interest rate, and the length of time over which the investment will earn interest. The formula for calculating future value is:\n" +
                "\n" +
                "FV = PV x (1 + r)^n\n" +
                "\n" +
                "Where:\n" +
                "\n" +
                "FV is the future value of the investment\n" +
                "PV is the present value of the investment\n" +
                "r is the interest rate or rate of return earned by the investment\n" +
                "n is the number of periods over which the investment will earn interest.\n" +
                "By using this formula, investors can determine the future value of their investment and make more informed decisions about where to invest their money to achieve their financial goals. It is important to note that the actual return earned on an investment may differ from the expected return due to fluctuations in the market, inflation, and other factors."))
        newList.add(ReferenceModel("Present value (PV)","Present value (PV) is the current value of a future amount of money, based on the time value of money. It is the amount of money that needs to be invested today at a given interest rate to equal a specified future amount of money.\n" +
                "\n" +
                "The concept of present value is important because it allows individuals and businesses to compare the value of money at different points in time. By calculating the present value of future cash flows, they can determine the value of an investment, project, or other financial transaction in today's dollars.\n" +
                "\n" +
                "The formula for present value is:\n" +
                "\n" +
                "PV = FV / (1 + r)^n\n" +
                "\n" +
                "Where:\n" +
                "\n" +
                "PV is the present value of the future cash flow\n" +
                "FV is the future value of the cash flow\n" +
                "r is the interest rate or rate of return that could be earned on the investment\n" +
                "n is the number of periods over which the investment will earn interest.\n" +
                "This formula takes into account the time value of money by factoring in the interest rate or rate of return that could be earned on the investment over time. By discounting the future cash flow back to its present value, investors can make more informed decisions about where to invest their money to achieve their financial goals."))
        newList.add(ReferenceModel("Periodic Deposit (PMT)","A periodic deposit is a regular investment of a fixed amount of money at a fixed interval, such as weekly, monthly, or quarterly. This type of investment is also known as a recurring deposit or systematic investment plan (SIP).\n" +
                "\n" +
                "Periodic deposits are a popular way to invest in long-term financial goals, such as retirement, education, or buying a house. They allow individuals to save and invest a fixed amount of money regularly over a period of time, which can help in building wealth and achieving financial goals.\n" +
                "\n" +
                "The amount of money invested in a periodic deposit can be small or large, depending on an individual's financial situation and goals. These deposits can be made into savings accounts, fixed deposits, mutual funds, or other investment vehicles.\n" +
                "\n" +
                "The benefits of periodic deposits include:\n" +
                "\n" +
                "Regular savings: Investing a fixed amount of money regularly helps in building a habit of saving and investing.\n" +
                "Dollar-cost averaging: Regular investments can help in buying more shares or units when the market is down and fewer when the market is up, which can help in reducing the impact of market volatility.\n" +
                "Compounding: Investing regularly over a long period of time allows for the power of compounding to work, which can significantly increase the value of the investment over time.\n" +
                "It is important to choose an investment vehicle that matches an individual's investment goals and risk tolerance. It is also important to review and adjust the periodic deposit amount and investment vehicle periodically to ensure that the investment strategy remains aligned with the individual's financial goals.\n" +
                "\n" +
                "\n" +
                "\n"))
        newList.add(ReferenceModel("Interest rate / year","Interest rate per year, also known as the annual interest rate, is the percentage rate at which interest is charged or earned on an investment or loan over a one-year period. It is the cost of borrowing or the return on investment expressed as a percentage of the amount borrowed or invested.\n" +
                "\n" +
                "The interest rate is a critical factor in determining the value of an investment or loan. The higher the interest rate, the higher the return on investment or the cost of borrowing. Conversely, a lower interest rate will result in a lower return on investment or lower cost of borrowing.\n" +
                "\n" +
                "The interest rate is influenced by several factors, including the inflation rate, monetary policy, economic conditions, and supply and demand for credit. The interest rate can also vary depending on the type of investment or loan, the creditworthiness of the borrower, and the term of the investment or loan.\n" +
                "\n" +
                "It is important to compare interest rates when selecting an investment or loan, as even a small difference in the interest rate can have a significant impact on the overall return or cost. Investors and borrowers should also consider the risks associated with the investment or loan and ensure that the interest rate is appropriate for their investment goals or borrowing needs.\n" +
                "\n" +
                "\n" +
                "\n"))
        newList.add(ReferenceModel("Payment Frequency","Payment frequency in investments refers to the frequency at which interest or principal payments are made on an investment or loan. The payment frequency can vary depending on the type of investment or loan, and it can have a significant impact on the overall return or cost.\n" +
                "\n" +
                "The most common payment frequencies for investments are annually, semi-annually, quarterly, monthly, and daily. The payment frequency determines how often interest is compounded, which affects the overall return on investment. The more frequently interest is compounded, the higher the effective annual rate of return will be.\n" +
                "\n" +
                "For example, suppose an investor invests \$10,000 in a fixed deposit account with an annual interest rate of 5% and a term of one year. If the interest is compounded annually, the investor will earn \$500 in interest at the end of the year. However, if the interest is compounded monthly, the investor will earn \$512.68 in interest at the end of the year, resulting in a higher effective annual rate of return of 5.13%.\n" +
                "\n" +
                "In addition to affecting the return on investment, the payment frequency can also affect the cash flow for borrowers. Borrowers who make more frequent payments on their loans will pay off the loan faster and pay less interest over the life of the loan.\n" +
                "\n" +
                "It is important to consider the payment frequency when selecting an investment or loan and ensure that it aligns with an individual's financial goals and cash flow needs.\n" +
                "\n" +
                "\n" +
                "\n"))
        newList.add(ReferenceModel("Nominal Rate","A nominal rate is the stated or advertised rate of interest or return on an investment or loan. It is the rate that is expressed in the terms of the contract, without taking into account the effects of inflation or compounding.\n" +
                "\n" +
                "For example, if a bank advertises a fixed deposit account with a nominal rate of 5%, it means that the account will earn 5% interest per year, regardless of inflation or the frequency of compounding. Similarly, a loan with a nominal interest rate of 7% per year means that the borrower will pay 7% interest per year on the principal amount borrowed, without taking into account inflation or the frequency of compounding.\n" +
                "\n" +
                "The nominal rate does not reflect the actual return or cost of the investment or loan, as it does not take into account the effects of inflation or compounding. To determine the actual return or cost of an investment or loan, it is necessary to consider the real rate of return or effective interest rate, which takes into account the effects of inflation and compounding.\n" +
                "\n" +
                "The difference between the nominal rate and the real rate of return or effective interest rate can be significant, particularly over a long period of time. For example, if inflation is 2% per year, a nominal interest rate of 5% per year would result in a real rate of return of only 3% per year.\n" +
                "\n" +
                "It is important to understand the difference between nominal rates and real rates when making investment or borrowing decisions and to consider the effects of inflation and compounding on the overall return or cost."))
        newList.add(ReferenceModel("Effective Rate","An effective rate, also known as the effective annual rate or the annual percentage yield (APY), is the actual rate of interest or return earned or paid on an investment or loan after taking into account the effects of compounding.\n" +
                "\n" +
                "Compounding is the process of reinvesting the interest earned on an investment, which allows the investment to grow at an accelerated rate over time. The more frequently interest is compounded, the higher the effective rate of return will be.\n" +
                "\n" +
                "The effective rate takes into account both the nominal rate and the frequency of compounding to provide a more accurate representation of the actual return or cost of an investment or loan. It reflects the true annual percentage rate (APR) of an investment or loan, including the effects of compounding.\n" +
                "\n" +
                "For example, if an investor invests \$10,000 in a fixed deposit account with an annual interest rate of 5% and monthly compounding, the effective rate of return would be 5.12% per year. This is higher than the nominal rate of 5% per year because the interest is compounded monthly, resulting in a higher return on investment.\n" +
                "\n" +
                "Similarly, for a loan with a nominal interest rate of 7% per year and monthly compounding, the effective annual rate would be higher than 7% per year, reflecting the actual cost of borrowing.\n" +
                "\n" +
                "Investors and borrowers should always consider the effective rate when making investment or borrowing decisions, as it provides a more accurate representation of the actual return or cost of the investment or loan. It is also important to compare the effective rates of different investments or loans to determine which offers the best value."))

        newList.add(ReferenceModel("Elapsed Days","The elapsed days in an investment refer to the number of days between the investment start date and the investment end date. The elapsed days are used to calculate the actual number of days that the investment is held, which is an important factor in determining the return on investment.\n" +
                "\n" +
                "The return on investment is often calculated on an annual basis, so the elapsed days are converted into a fractional year, based on a 365-day year. This is done by dividing the elapsed days by 365. For example, if an investment was held for 100 days, the fractional year would be 100/365 = 0.27.\n" +
                "\n" +
                "The elapsed days are also used to calculate the interest earned or paid on an investment during the holding period. The interest may be calculated on a simple interest basis, which is based on the nominal interest rate and the elapsed days, or on a compound interest basis, which takes into account the effects of compounding and the frequency of compounding.\n" +
                "\n" +
                "For example, suppose an investor invests \$10,000 in a fixed deposit account with an annual interest rate of 5% and a term of 180 days. If the interest is compounded annually, the investor will earn \$250 in interest at the end of the term. However, if the interest is compounded daily, the investor will earn \$252.05 in interest at the end of the term, taking into account the effects of compounding and the elapsed days.\n" +
                "\n" +
                "In summary, the elapsed days are an important factor in calculating the return on investment and the interest earned or paid on an investment. Investors should be aware of the elapsed days and the frequency of compounding when making investment decisions."))

        adapter = ReferenceAdapter(ReferenceAdapter.OnClickListener { reference ->


            selectedReference = reference
            showInterstitial()


        })

        adapter.submitList(newList)

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


        ActivityTools.startReferenceDetailActivity(requireContext(),selectedReference.title,selectedReference.description)
    }

    private fun bindings() {




        setupRecyclerView()

    }


    private lateinit var adapter: ReferenceAdapter
    private lateinit var recyclerView : RecyclerView

    private var interstitialAd: InterstitialAd? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReferenceBinding.inflate(inflater, container, false)
        val root: View = binding.root



        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Reference"
        bindings()




        loadInterstitialAd()
        loadAds()



        return root
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




}