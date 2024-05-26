package com.example.quikcartadmin.ui.products.allproducts.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.ProductItemBinding
import com.example.quikcartadmin.models.entities.products.ProductsItem

class AllProductAdapter (val context: Context,
                         val onClick:(ProductsItem)-> Unit,
                         val onDeleteClick: (ProductsItem) -> Unit)
    : ListAdapter<ProductsItem, AllProductAdapter.AllProductViewHolder>(AllProductsDiffUtil) {

    class AllProductViewHolder(val allProductsBinding : ProductItemBinding)
        :RecyclerView.ViewHolder(allProductsBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProductViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val productItemBinding = ProductItemBinding.inflate(inflater, parent,false)
        return AllProductViewHolder(productItemBinding)
    }

    override fun onBindViewHolder(holder: AllProductViewHolder, position: Int) {
        var current = getItem(position)

        holder.allProductsBinding.productPrice.text = (current.variants?.get(0)?.price ?: 100).toString()
        holder.allProductsBinding.productName.text = current.title
        holder.allProductsBinding.typeName.text = "Type: ${current.productType}"
        holder.allProductsBinding.vendorOfProduct.text = "Vendor: ${current.vendor}"
        holder.allProductsBinding.rateOfProduct.rating = 5.0F

        val imageUrl = current.image?.src ?: "https://cdn.shopify.com/s/files/1/0703/5830/2955/files/8cd561824439482e3cea5ba8e3a6e2f6.jpg?v=1716233144"
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.product)
            .error(R.drawable.ic_close)
            .into(holder.allProductsBinding.imageOfProduct)

        holder.itemView.setOnClickListener {
            onClick(current)
        }

        holder.allProductsBinding.deleteProduct.setOnClickListener {
            onDeleteClick(current)
        }
    }
}

object AllProductsDiffUtil : DiffUtil.ItemCallback<ProductsItem>() {
    override fun areItemsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
        return oldItem  == newItem
    }

}