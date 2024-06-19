package com.example.quikcartadmin.ui.products.productdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcartadmin.databinding.VariantsDetailsItemBinding
import com.example.quikcartadmin.models.entities.products.VariantsItem

class VariantsAdapter(val onClick:(VariantsItem) -> Unit)
    : ListAdapter<VariantsItem, VariantsAdapter.VariantsViewHolder>(VariantsDiffUtil) {
    class VariantsViewHolder(val variantItemBinding: VariantsDetailsItemBinding)
        :RecyclerView.ViewHolder(variantItemBinding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantsViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val variantsItemBinding = VariantsDetailsItemBinding.inflate(inflater, parent,false)
        return VariantsAdapter.VariantsViewHolder(variantsItemBinding)
    }

    override fun onBindViewHolder(holder: VariantsViewHolder, position: Int) {
        val current = getItem(position)

        holder.variantItemBinding.colorName.text = current.title
        holder.variantItemBinding.colorPrice.text = "Price: ${current.price}"
    }
}

object VariantsDiffUtil : DiffUtil.ItemCallback<VariantsItem>() {
    override fun areItemsTheSame(oldItem: VariantsItem, newItem: VariantsItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: VariantsItem, newItem: VariantsItem): Boolean {
        return oldItem == newItem
    }

}
