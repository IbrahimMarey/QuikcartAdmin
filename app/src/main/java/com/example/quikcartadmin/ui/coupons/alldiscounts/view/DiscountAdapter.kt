package com.example.quikcartadmin.ui.coupons.alldiscounts.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcartadmin.databinding.DiscountItemBinding
import com.example.quikcartadmin.models.entities.coupons.DiscountCode
import com.example.quikcartadmin.models.entities.coupons.PriceRule
import com.example.quikcartadmin.models.entities.products.ProductsItem
import com.example.quikcartadmin.ui.products.allproducts.view.AllProductAdapter
import com.example.quikcartadmin.ui.products.allproducts.view.AllProductsDiffUtil

class DiscountAdapter (private val rule : PriceRule,
                       val onClick:(DiscountCode)-> Unit,
                       val onDeleteClick: (DiscountCode) -> Unit)
    : ListAdapter<DiscountCode, DiscountAdapter.AllDiscountsViewHolder>(AllDiscountsDiffUtil) {

    private lateinit var binding: DiscountItemBinding

    class AllDiscountsViewHolder (var binding: DiscountItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllDiscountsViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DiscountItemBinding.inflate(inflater, parent, false)
        return AllDiscountsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllDiscountsViewHolder, position: Int) {
        val discount = getItem(position)

        holder.binding.discountTitleTextView.text = discount.code
        if (rule.value_type == "fixed_amount") {
            holder.binding.discountValueTextView.text = rule.value+" EGP"
        }else{
            holder.binding.discountValueTextView.text = rule.value+"%"
        }

        binding.deleteImage.setOnClickListener {
            onDeleteClick(discount)
        }

        binding.editImage.setOnClickListener {
            onClick(discount)
        }
    }

}

object AllDiscountsDiffUtil : DiffUtil.ItemCallback<DiscountCode>(){
    override fun areItemsTheSame(oldItem: DiscountCode, newItem: DiscountCode): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DiscountCode, newItem: DiscountCode): Boolean {
        return oldItem  == newItem
    }

}
