package com.example.quikcartadmin.ui.products.createproduct.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.ImagesCreateItemBinding
import com.example.quikcartadmin.models.entities.products.ImagesItem

class AddImagesAdapter(
    private val context: Context,
    private val onDeleteClick: (ImagesItem) -> Unit
) : ListAdapter<ImagesItem, AddImagesAdapter.AddImagesViewHolder>(AddImagesDiffUtil) {

    class AddImagesViewHolder(val addImagesBinding: ImagesCreateItemBinding) :
        RecyclerView.ViewHolder(addImagesBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddImagesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val addImagesBinding = ImagesCreateItemBinding.inflate(inflater, parent, false)
        return AddImagesViewHolder(addImagesBinding)
    }

    override fun onBindViewHolder(holder: AddImagesViewHolder, position: Int) {
        val current = getItem(position)
        holder.addImagesBinding.apply {
            val imageUrl = current.src
                ?: "https://cdn.shopify.com/s/files/1/0703/5830/2955/files/8cd561824439482e3cea5ba8e3a6e2f6.jpg?v=1716233144"
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.product)
                .error(R.drawable.ic_close)
                .into(imageOfProductItem)

            deleteImage.setOnClickListener {
                onDeleteClick(current)
            }
        }
    }
}

object AddImagesDiffUtil : DiffUtil.ItemCallback<ImagesItem>() {
    override fun areItemsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
        return oldItem == newItem
    }
}
