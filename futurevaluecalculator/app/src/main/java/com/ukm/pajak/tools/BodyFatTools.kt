package com.ukm.pajak.tools

import kotlin.math.log10
import kotlin.math.pow

class BodyFatTools {

    companion object {

         fun jacksonPollock3(

            genders: Int,
            age: Int,
            tricep: Double,
            thigh: Double,
            suprailliac: Double,
            chest: Double,
            abdominal: Double
        )  : Double {


            var bodyFatValue = 0.0
            if (genders == 2) {

                bodyFatValue = 1.10938 -  (0.0008267 * (chest+ thigh+ abdominal)) + (0.0000016 * (chest+ thigh+ abdominal).pow(2)) - (0.0002574 * age)

                bodyFatValue = (495/ bodyFatValue)-450

            }else{

                bodyFatValue = 1.0994921 -  (0.0009929 * (tricep+ thigh+ suprailliac)) + (0.0000023 * (tricep+ thigh+ suprailliac).pow(2)) - (0.0001392 * age)

                bodyFatValue = (495/ bodyFatValue)-450
            }


            return bodyFatValue

        }

        fun jacksonPollock4(
            genders: Int,
            age: Int,
            tricep: Double,
            thigh: Double,
            suprailliac: Double,

            abdominal: Double
        )  : Double {

            var bodyFatValue = 0.0
            if (genders == 1) {

                bodyFatValue = (0.29669  * (tricep+ thigh+ suprailliac+ abdominal)) - (0.00043 * (tricep+ thigh+ suprailliac+ abdominal).pow(2)) + (0.02963 * age) +  1.4072



            }else{

                bodyFatValue = (0.29288  * (tricep+ thigh+ suprailliac+ abdominal)) - (0.0005 * (tricep+ thigh+ suprailliac+ abdominal).pow(2)) + (0.15845 * age) - 5.76377

            }

            return bodyFatValue

        }

        fun durninWomersley(
            genders: Int,
            age: Int,
            tricep: Double,
            suprailliac: Double,
            subscapular: Double,
            bicep: Double
        )  : Double  {

            var bodyFatValue = 0.0
            if (genders == 1) {

                if(age <17){

                    bodyFatValue = 1.1369 - (0.0598 *  log10(tricep+ bicep+ suprailliac +subscapular))
                }

                if(age in 17..19){

                    bodyFatValue = 1.1549 - (0.0678 *  log10(tricep+ bicep+ suprailliac +subscapular))
                }

                if(age in 20..29){

                    bodyFatValue =1.1599 - (0.0717 *  log10(tricep+ bicep+ suprailliac +subscapular))
                }


                if(age in 30..39){

                    bodyFatValue = 1.1423 - (0.0632 *  log10(tricep+ bicep+ suprailliac +subscapular))
                }


                if(age in 40..49){

                    bodyFatValue = 1.1333 - (0.0612 *  log10(tricep+ bicep+ suprailliac +subscapular))
                }

                if(age >50){

                    bodyFatValue = 1.1339 - (0.0645 *  log10(tricep+ bicep+ suprailliac +subscapular))
                }

            }else{

                if(age <17){

                    bodyFatValue = 1.1533 - (0.0643 *  log10(tricep+ bicep+ suprailliac +subscapular))
                }

                if(age in 17..19){

                    bodyFatValue = 1.1620 - (0.0630 *  log10(tricep+ bicep+ suprailliac +subscapular))
                }

                if(age in 20..29){

                    bodyFatValue =1.1631 - (0.0632 *  log10 (tricep+ bicep+ suprailliac +subscapular))
                }


                if(age in 30..39){

                    bodyFatValue = 1.1422 - (0.0544 *  log10(tricep+ bicep+ suprailliac +subscapular))
                }


                if(age in 40..49){

                    bodyFatValue = 1.1620 - (0.0700 *  log10(tricep+ bicep+ suprailliac +subscapular))
                }

                if(age >50){

                    bodyFatValue = 1.1715 - (0.0779 *  log10(tricep+ bicep+ suprailliac +subscapular))
                }
            }

            bodyFatValue = (495/ bodyFatValue)-450

            return bodyFatValue

        }

        fun jacksonPollock7(
            genders: Int,
            age: Int,
            tricep: Double,
            thigh: Double,
            suprailliac: Double,
            abdominal: Double,
            subscapular: Double,
            midxaxillary: Double,
            chest: Double,

        )  : Double {


            var bodyFatValue = 0.0
            if (genders == 1) {

                bodyFatValue = 1.097  -  (0.00046971 * (tricep+ thigh+ suprailliac+ chest+ subscapular+ midxaxillary+ abdominal)) + (0.00000056 * (tricep+ thigh+ suprailliac+ chest+ subscapular+ midxaxillary+ abdominal).pow(2)) - (0.00012828 * age)

                bodyFatValue = (495/ bodyFatValue)-450

            }else{

                bodyFatValue = 1.112  -  (0.00043499 * (tricep+ thigh+ suprailliac+ chest+ subscapular+ midxaxillary+ abdominal)) + (0.00000055 * (tricep+ thigh+ suprailliac+ chest+ subscapular+ midxaxillary+ abdominal).pow(2)) - (0.00028826* age)

                bodyFatValue = (495/ bodyFatValue)-450
            }

            return bodyFatValue


        }

        fun covertBailey(
            units: Int,
            genders: Int,
            age: Int,
            wrist: Double,
            thigh: Double,
            calf: Double,
            forearm: Double,
            hip: Double,
            waist: Double
        )  : Double {


            var waistInches = waist!!
            var hipInches = hip!!
            var calfInches  = calf!!
            var thighInches = thigh!!
            var wristInches = wrist!!
            var forearmInches = forearm!!
            if(units==2){

                hipInches *= 0.393701
                waistInches *= 0.393701
                calfInches *= 0.393701

                thighInches *= 0.393701

                wristInches *= 0.393701
                forearmInches *= 0.393701

            }
            var bodyFatValue = 0.0

            if (genders == 1) {

                if (age <= 30) {


                    bodyFatValue = hipInches + (0.8 * thighInches) - (2 * calfInches) - wristInches


                } else {


                    bodyFatValue = hipInches + ( thighInches) - (2 * calfInches) - wristInches

                }

            } else {

                if (age <= 30) {


                    bodyFatValue = waistInches + (0.5 * hipInches) - (3 * forearmInches) - wristInches


                } else {


                    bodyFatValue = waistInches + (0.5 * hip) - (2.7 * forearmInches) - wristInches


                }

            }

            return bodyFatValue


        }

        fun usNavy(
            units: Int,
            height: Double,
            heightUS: Double,
            genders: Int,
            waist: Double,
            neck: Double,
            hip: Double
        )  : Double {
            var bodyFatValue = 0.0
            var heightInches = (height!! * 12) + heightUS
            var waistInches = waist!!
            var neckInches = neck!!
            var hipInches = hip!!

            if (genders == 2) {

                bodyFatValue = if (units == 2) {

                    (495 / (1.0324 - 0.19077 * (log10(waist!! - neck!!)) + 0.15456 * (log10(height!!))) - 450);

                } else {


                    (86.010 * log10(waistInches!! - neckInches!!)) - (70.041 * log10(heightInches)) + 36.76

                }


            } else {


                bodyFatValue = if (units == 2) {

                    (495 / (1.29579 - 0.35004 * (log10(waist!! + hip!! - neck!!)) + 0.22100 * (log10(
                        height!!
                    ))) - 450);

                } else {


                    (163.205 * log10(waistInches!! + hipInches!! - neckInches)) - (97.684 * log10(
                        heightInches
                    )) - 78.387

                }


            }

            return bodyFatValue
        }

        fun bmi(
            units: Int,
            weight: Double,
            height: Double,
            heightUS: Double,
            age: Int,
            genders: Int,
        ) : Double{


            var bodyFat = getBodyFatBMI(units!!, weight!!, height!!, heightUS!!, age!!, genders!!)

            return bodyFat
        }

        fun getBodyFatBMI(
            units: Int,
            weight: Double,
            height: Double,
            heightUS: Double,
            age: Int,
            genders: Int
        ): Double {

            var result = 0.0


            result = if (units == 1) {


                703 * (weight / (((height * 12) + heightUS).pow(2)))
            } else {

                weight / ((height / 100).pow(2))
            }



            if (age > 19) {

                result = if (genders == 1) {


                    1.20 * result + 0.23 * age - 5.4
                } else {

                    1.20 * result + 0.23 * age - 16.2
                }
            } else {

                result = if (genders == 1) {


                    1.51 * result - 0.7 * age + 1.4
                } else {


                    1.51 * result - 0.7 * age - 2.2
                }
            }

            return result

        }


    }
}