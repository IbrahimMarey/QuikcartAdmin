package com.example.quikcartadmin.ui.coupons.alldiscounts.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.FragmentAllDiscountsBinding
import com.example.quikcartadmin.helpers.Constants
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.DiscountCode
import com.example.quikcartadmin.models.entities.coupons.PriceRule
import com.example.quikcartadmin.ui.coupons.alldiscounts.viewmodel.AllDiscountsViewModel
import com.example.quikcartadmin.ui.coupons.creatediscount.view.CreateDiscountFragment
import com.example.quikcartadmin.ui.coupons.creatediscount.view.DiscountListener
import com.example.quikcartadmin.ui.coupons.updatediscount.view.UpdateDiscountFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllDiscountsFragment : Fragment(), DiscountListener {

    lateinit var discountsBinding : FragmentAllDiscountsBinding

    private lateinit var discountAdapter: DiscountAdapter
    private var discountsList: MutableList<DiscountCode> = mutableListOf()
    private lateinit var priceRule : PriceRule
    private lateinit var discountCode: DiscountCode
    private val viewModel: AllDiscountsViewModel by viewModels()
    private val args: AllDiscountsFragmentArgs by navArgs()


    private val alertDialog : AlertDialog by lazy {
        Constants.createAlertDialog(requireContext(),"")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        discountsBinding = FragmentAllDiscountsBinding.inflate(inflater, container, false)
        return discountsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        priceRule = args.priceRule

        viewModel.getDiscounts(priceRule.id!!)

        setUpRecyclerView()
        observeGetDiscounts()
        observeDeleteDiscount()
        addDiscountAction()
    }

    private fun setUpRecyclerView() {
        discountAdapter = DiscountAdapter(onClick = {
            updateDiscount(it)
        },
         onDeleteClick = {
             deleteDiscount(it)
         }, rule = priceRule
        )

        discountsBinding.discountRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = discountAdapter
        }
    }

    private fun observeGetDiscounts() {
        viewModel.getDiscounts(priceRule.id!!)

        lifecycleScope.launch {
            viewModel.getDiscountState.collectLatest {
                when (it) {
                    is UiState.Loading -> {
                        discountsBinding.progressbar.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        if(alertDialog.isShowing) alertDialog.dismiss()
                        discountsList = it.data.toMutableList()
                        discountAdapter.submitList(discountsList)
                        discountsBinding.progressbar.visibility = View.GONE
                    }
                    else -> {
                        discountsBinding.progressbar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun observeDeleteDiscount() {
        lifecycleScope.launch {
            viewModel.deleteDiscountState.collectLatest {
                when (it) {
                    is UiState.Loading -> {
                    }
                    is UiState.Success -> {
                        viewModel.getDiscounts(priceRule.id!!)
                        makeAlert("Deleted successfully","deleted discount code is code.")
                    }
                    else -> {
                        alertDialog.dismiss()
                        makeAlert("Deleted failed","make sure your connection to delete.")
                    }
                }
            }
        }
    }

    private fun addDiscountAction(){
        discountsBinding.addFloatingBtn.setOnClickListener {
            CreateDiscountFragment.newInstance(priceRule,this)
                .show(childFragmentManager, CreateDiscountFragment.TAG)
        }
    }

    private fun handleDeleteAction(){
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Discount")
            .setMessage("Are you sure you want to delete this discount?")
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                viewModel.deleteDiscount(priceRule.id!!,discountCode.id!!)
                alertDialog.show()
            }
            .setNegativeButton("Cancel", null)
            .setIcon(R.drawable.baseline_add_alert_24)
            .show()
    }

    override fun getDiscounts() {
        viewModel.getDiscounts(priceRule.id!!)
    }

    override fun updateDiscount(discountCode: DiscountCode) {
        UpdateDiscountFragment.newInstance(priceRule,discountCode,this)
            .show(childFragmentManager, UpdateDiscountFragment.TAG)
    }
    override fun deleteDiscount(discountCode: DiscountCode) {
        this.discountCode = discountCode
        handleDeleteAction()
    }

    private fun makeAlert(title: String,msg: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(
                "OK"
            ) { _, _ ->

            }
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
}
