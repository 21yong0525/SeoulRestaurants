package com.yong.nyamnyamgood

import android.R
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.yong.nyamnyamgood.databinding.DialogCustomLangBinding

class CustomDialogLang(val context: Context) {
    companion object {
        const val WIDTH = 700
        const val HEIGHT = 700
    }

    val dialog = Dialog(context)
    val binding = DialogCustomLangBinding.inflate(LayoutInflater.from(context))

    fun showDialog() {
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(WIDTH, HEIGHT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        val spinnerList = arrayOf("ko", "en", "zh-TW", "zh-CN", "ja")
        binding.spinner.adapter = ArrayAdapter(binding.root.context, R.layout.simple_spinner_dropdown_item, spinnerList)
        lateinit var lang: String

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                lang = spinnerList[position]
            }
        }

        binding.btnSelect.setOnClickListener {
            val dbHelper = DBHelper(binding.root.context, MainActivity.DB_NAME, MainActivity.VERSION)
            dbHelper.languageSet(lang)
            MainActivity.lang = lang
            val mainActivity = context as MainActivity
            mainActivity.changeLang()
            dialog.dismiss()
        }
    }
}