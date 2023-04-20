package com.example.yeojeong.`class`

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yeojeong.databinding.ItemHomecardBinding
import com.example.yeojeong.dataset.Homecarddata

class homecardadapter(context: Context, val itemList: MutableList<Homecarddata>): RecyclerView.Adapter<homecardadapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemHomecardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class Holder(var binding: ItemHomecardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Homecarddata) {
            binding.model = item

            Glide.with(itemView.context).load(item.pimage)
                .into(binding.cardImage)
        }
    }

}
