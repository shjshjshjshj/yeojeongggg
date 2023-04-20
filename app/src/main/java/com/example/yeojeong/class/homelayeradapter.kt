package com.example.yeojeong.`class`

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yeojeong.databinding.LayerHomecardlistBinding
import com.example.yeojeong.dataset.Homelayerdata

class homelayeradapter(val context: Context, val itemList: Array<Homelayerdata>): RecyclerView.Adapter<homelayeradapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = LayerHomecardlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class Holder(var binding: LayerHomecardlistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Homelayerdata) {
            binding.model = item

            binding.homeFirstCardList.adapter = homecardadapter(context, item.innerList)
            binding.homeFirstCardList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

}