package com.yong.nyamnyamgood

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import com.yong.nyamnyamgood.databinding.DialogCustomBinding

class CustomDialog(val context: Context) {
    val dialog = Dialog(context)
    val binding = DialogCustomBinding.inflate(LayoutInflater.from(context))

    fun showDialog(dataVOStore: DataVOStore) {
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
        )
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        binding.address.text = dataVOStore.address
        binding.interneturl.text = dataVOStore.url
        binding.reprsntMenu.text = dataVOStore.reprsntMenu
        binding.telNo.text = dataVOStore.telNo
        binding.storeName.text = dataVOStore.storeName
        binding.useTime.text = dataVOStore.useTime
        binding.subwayInfo.text = dataVOStore.subwayInfo

        binding.btnReturn.setOnClickListener {
            dialog.dismiss()
        }

        val dialogIntent = CustomDialog_intent(binding.root.context)

        if(binding.interneturl.text.isNotEmpty()){
            binding.interneturl.setOnClickListener {
                dialogIntent.showDialog(dataVOStore, 1)
            }
        }

        if(binding.telNo.text.isNotEmpty()){
            binding.telNo.setOnClickListener {
                dialogIntent.showDialog(dataVOStore, 2)
            }
        }
    }
}