package com.yong.nyamnyamgood

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yong.nyamnyamgood.databinding.ItemListBinding

class CustomAdapter(val storeList: MutableList<DataVOStore>?) : RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val mainActivity = parent.context as MainActivity
        val customViewHolder = CustomViewHolder(binding)

        customViewHolder.itemView.setOnClickListener {
            val position: Int = customViewHolder.adapterPosition
            val dataVOStore = storeList?.get(position)
            val dialog = CustomDialog(parent.context)
            if (dataVOStore != null) {
                dialog.showDialog(dataVOStore)
            }
        }

        customViewHolder.itemView.setOnLongClickListener {
            val position: Int = customViewHolder.adapterPosition
            true
        }

        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = holder.binding
        val dataVOStore = storeList?.get(position)
        if (dataVOStore?.storeName != null){
            binding.itemListStoreName.text = dataVOStore.storeName.toString()
        }
        if (dataVOStore?.telNo != null) {
            binding.itemListStoreTel.text = dataVOStore.telNo.toString()
        }

        when(dataVOStore?.star){
            0 -> binding.tvStar.setBackgroundResource(R.drawable.ic_star_outline_24)
            1 -> binding.tvStar.setBackgroundResource(R.drawable.ic_star_24)
        }

        binding.tvStar.setOnClickListener {
            if (dataVOStore?.star == 0) {
                binding.tvStar.setBackgroundResource(R.drawable.ic_star_24)
                dataVOStore?.star = 1
            } else {
                binding.tvStar.setBackgroundResource(R.drawable.ic_star_outline_24)
                dataVOStore?.star = 0
            }

            val dbHelper = DBHelper(binding.root.context , MainActivity.DB_NAME, MainActivity.VERSION)
            if (dataVOStore != null) {
                val flag = dbHelper.updateStar(dataVOStore)
                if (flag){
                    notifyDataSetChanged()
                } else {
                    Log.e("nyamnyamgood","업데이트 실패")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return storeList?.size ?: 0
    }

    class CustomViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)
}