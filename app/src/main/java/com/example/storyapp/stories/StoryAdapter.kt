package com.example.storyapp.stories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ItemListStoryBinding
import com.example.storyapp.response.ListStoryItem

class StoryAdapter
    : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    private var oldStoryItem = emptyList<ListStoryItem>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemListStoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(oldStoryItem[position])

        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(oldStoryItem[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = oldStoryItem.size

    class ViewHolder(private val binding: ItemListStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(listStoryItem: ListStoryItem){
            Glide.with(itemView)
                .load(listStoryItem.photoUrl)
                .into(binding.imgPhoto)

            binding.tvName.text = listStoryItem.name
        }
    }

    fun setData(newStoryItem: List<ListStoryItem>) {
        val diffUtil = StoriesDiffUtil(oldStoryItem, newStoryItem)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldStoryItem = newStoryItem
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickCallback {
        fun onItemClicked(item: ListStoryItem)
    }
}