package com.ukm.pajak.tools

import android.content.Context

class SharedPreferencesTools {

    companion object {

        fun saveData(context: Context, title:String, data:String){
            val sharedPreference =  context.getSharedPreferences("simulasikredit", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString(title,data)
            editor.commit()
        }

        fun getData(context: Context, title: String) : String? {
            val sharedPreference =  context.getSharedPreferences("simulasikredit", Context.MODE_PRIVATE)
            return sharedPreference.getString(title,"")
        }

    }

}