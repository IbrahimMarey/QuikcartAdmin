package com.example.quikcartadmin.ui.coupons.creatediscount.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.quikcartadmin.databinding.FragmentCreateDiscountBinding
import com.example.quikcartadmin.helpers.Constants
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeBody
import com.example.quikcartadmin.models.entities.coupons.PriceRule
import com.example.quikcartadmin.models.entities.coupons.SingleDiscountCode
import com.example.quikcartadmin.ui.coupons.creatediscount.viewmodel.CreateDiscountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateDiscountFragment : DialogFragment() {

    private lateinit var createDiscountBinding: FragmentCreateDiscountBinding

    private val viewModel: CreateDiscountViewModel by viewModels()

    private val alertDialog: AlertDialog by lazy {
        Constants.createAlertDialog(requireContext(), "")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createDiscountBinding = FragmentCreateDiscountBinding.inflate(inflater, container, false)
        return createDiscountBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createDiscountBinding.addBtn.setOnClickListener {
            if (!createDiscountBinding.codeEt.text.toString().isNullOrEmpty()) {
                viewModel.createDiscount(
                    priceRule.id ?: 0,
                    DiscountCodeBody(SingleDiscountCode(createDiscountBinding.codeEt.text.toString()))
                )
                alertDialog.show()
            } else {
                createDiscountBinding.codeEt.error = "should have a code"
            }
        }


        observeCreateDiscount()

    }

    companion object {
        private lateinit var priceRule: PriceRule
        private lateinit var discountListener: DiscountListener
        const val TAG = "CreateDiscountFragment"
        fun newInstance(
            priceRule: PriceRule,
            discountListener: DiscountListener
        ): CreateDiscountFragment {
            Companion.priceRule = priceRule
            Companion.discountListener = discountListener
            return CreateDiscountFragment()
        }
    }


    private fun observeCreateDiscount() {
        lifecycleScope.launch {
            viewModel.createDiscountState.collectLatest {
                when (it) {
                    is UiState.Loading -> {
                        alertDialog.show()
                    }
                    is UiState.Success -> {
                        alertDialog.dismiss()
                        discountListener.getDiscounts()
                        Toast.makeText(requireActivity(), "Created successfully", Toast.LENGTH_LONG)
                            .show()
                        dismiss()
                    }
                    else -> {
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(), "Creation failed", Toast.LENGTH_LONG)
                            .show()
                        dismiss()
                    }
                }
            }
        }
    }

}