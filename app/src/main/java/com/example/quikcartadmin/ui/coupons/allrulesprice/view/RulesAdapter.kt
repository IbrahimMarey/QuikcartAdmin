package com.example.quikcartadmin.ui.coupons.allrulesprice.view

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcartadmin.databinding.RuleItemBinding
import com.example.quikcartadmin.helpers.GetTime
import com.example.quikcartadmin.models.entities.coupons.DiscountCode
import com.example.quikcartadmin.models.entities.coupons.PriceRule

class RulesAdapter(
    val onDeleteClick: (PriceRule) -> Unit)
 : ListAdapter<PriceRule, RulesAdapter.RulesViewHolder>(RulesDiffUtil) {

    class RulesViewHolder(val binding: RuleItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RulesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RuleItemBinding.inflate(inflater, parent, false)
        return RulesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RulesViewHolder, position: Int) {
        val current = getItem(position)

        holder.binding.titleTextview.text = current.title
        holder.binding.valueTextview.text = if (current.value_type == "fixed_amount") {
            "${current.value} EGP"
        } else {
            "${current.value}%"
        }

        holder.binding.createdAtTextview.text = GetTime.formatDateString(current.created_at)

        holder.binding.startAtTextview.text = Html.fromHtml("<b>From:</b> ${GetTime.formatDateString(current.starts_at)}")
        Log.i("TAG", "onBindViewHolder: ${GetTime.formatDateString(current.created_at)}/////${current.created_at}")
        holder.binding.endsAtTextview.text = if (current.ends_at.isNullOrEmpty()) {
            Html.fromHtml("<b>To:</b> Not determined yet")
        } else {
            Html.fromHtml("<b>To:</b> ${GetTime.formatDateString(current.ends_at)}")
        }

        holder.binding.ruleCardView.setOnClickListener {
            holder.binding.root.findNavController()
                .navigate(AllRulesFragmentDirections.actionAllRulesFragmentToAllCoupons(current))
        }
        holder.binding.editImage.setOnClickListener {
            holder.binding.root.findNavController()
                .navigate(AllRulesFragmentDirections.actionAllRulesFragmentToUpdateRulePriceFragment(current))
        }

        holder.binding.deleteImage.setOnClickListener {
            onDeleteClick(current)
        }
    }
}

object RulesDiffUtil : DiffUtil.ItemCallback<PriceRule>() {
    override fun areItemsTheSame(oldItem: PriceRule, newItem: PriceRule): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PriceRule, newItem: PriceRule): Boolean {
        return oldItem == newItem
    }
}
