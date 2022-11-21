package com.yong.nyamnyamgood

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.WindowManager
import com.yong.nyamnyamgood.databinding.DialogCustomIntentBinding

class CustomDialog_intent(val context: Context) {
    companion object {
        const val HEIGHT = 700
    }

    val dialog = Dialog(context)
    val binding = DialogCustomIntentBinding.inflate(LayoutInflater.from(context))

    fun showDialog(dataVOStore: DataVOStore, type: Int) {
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, HEIGHT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        binding.tvStoreName.text = dataVOStore.storeName

        when(type) {
            1 -> {
                binding.tvIntentInfo.text = "${dataVOStore.url} \n 으로 이동합니다."
                binding.btnAction.text = "보러가기"
                binding.btnAction.setOnClickListener {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse("${dataVOStore.url}")
                    context.startActivity(intent)
                }
            }
            2 -> {
                binding.tvIntentInfo.text = "${dataVOStore.telNo} \n 으로 전화합니다."
                binding.btnAction.text = "전화하기"
                binding.btnAction.setOnClickListener {
                    val intent = Intent()
                    intent.action = Intent.ACTION_CALL
                    intent.data = Uri.parse("tel:${dataVOStore.telNo}")
                    context.startActivity(intent)
                }
            }
        }

        binding.btnCancle.setOnClickListener {
            dialog.dismiss()
        }
    }
}