package com.example.quikcartadmin.ui.products.productdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.ImagesDetailsItemBinding
import com.example.quikcartadmin.models.entities.products.ImagesItem

class ImagesAdapter (val context: Context,val onClick:(ImagesItem) -> Unit)
    : ListAdapter<ImagesItem, ImagesAdapter.ImagesViewHolder>(ImagesDiffUtil) {
    class ImagesViewHolder(val imagesItemBinding: ImagesDetailsItemBinding)
        : RecyclerView.ViewHolder(imagesItemBinding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val imagesItemBinding = ImagesDetailsItemBinding.inflate(inflater, parent,false)
        return ImagesAdapter.ImagesViewHolder(imagesItemBinding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val current = getItem(position)

        val imageUrl = current.src ?: "https://cdn.shopify.com/s/files/1/0703/5830/2955/files/8cd561824439482e3cea5ba8e3a6e2f6.jpg?v=1716233144"
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.product)
            .error(R.drawable.ic_close)
            .into(holder.imagesItemBinding.imageOfProductItem)

    }
}

object ImagesDiffUtil : DiffUtil.ItemCallback<ImagesItem>() {
    override fun areItemsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
        return oldItem == newItem
    }

}
