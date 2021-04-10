package com.example.robustatask.ui.weatherHistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.robustatask.databinding.ViewWeatherHistoryListItemBinding
import com.example.robustatask.model.weatherHistory.WeatherHistoryItem

/**
 *Created by Yasser.Elnaggar on 4/10/2021
 */
class WeatherHistoryItemsAdapter(private val clickListener: WeatherHistoryItemClickListener) :
    ListAdapter<WeatherHistoryItem, WeatherHistoryItemsAdapter.WeatherHistoryItemViewHolder>(
        DiffUtilCallback
    ) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherHistoryItemViewHolder {
        return WeatherHistoryItemViewHolder.from(parent, clickListener)
    }

    override fun onBindViewHolder(holder: WeatherHistoryItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    
    class WeatherHistoryItemViewHolder(
        private val binding: ViewWeatherHistoryListItemBinding,
        private val clickListener: WeatherHistoryItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeatherHistoryItem) {
            binding.item = item
            binding.position = adapterPosition
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                clickListener: WeatherHistoryItemClickListener
            ): WeatherHistoryItemViewHolder {
                return WeatherHistoryItemViewHolder(
                    ViewWeatherHistoryListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    clickListener
                )
            }
        }

    }

    interface WeatherHistoryItemClickListener{
        fun onItemClick(position: Int)
        fun onShareClick(position: Int)
    }

    companion object DiffUtilCallback : DiffUtil.ItemCallback<WeatherHistoryItem>() {
        override fun areItemsTheSame(
            oldItem: WeatherHistoryItem,
            newItem: WeatherHistoryItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: WeatherHistoryItem,
            newItem: WeatherHistoryItem
        ): Boolean {
            return oldItem == newItem
        }

    }



}


