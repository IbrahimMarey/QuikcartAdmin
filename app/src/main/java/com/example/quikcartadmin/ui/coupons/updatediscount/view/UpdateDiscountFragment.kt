package com.example.quikcartadmin.ui.coupons.updatediscount.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.quikcartadmin.databinding.FragmentUpdateDiscountBinding
import com.example.quikcartadmin.helpers.Constants
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.DiscountCode
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeResponse
import com.example.quikcartadmin.models.entities.coupons.PriceRule
import com.example.quikcartadmin.ui.coupons.creatediscount.view.DiscountListener
import com.example.quikcartadmin.ui.coupons.updatediscount.viewmodel.UpdateDiscountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpdateDiscountFragment : DialogFragment() {

    private lateinit var updateDiscountBinding: FragmentUpdateDiscountBinding

    private val viewModel: UpdateDiscountViewModel by viewModels()
    private val alertDialog: AlertDialog by lazy {
        Constants.createAlertDialog(requireContext(), "")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        updateDiscountBinding = FragmentUpdateDiscountBinding.inflate(inflater, container, false)
        return updateDiscountBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateDiscountBinding.codeEt.setText(discountCode.code!!)

        updateDiscountBinding.addBtn.setOnClickListener {
            if (discountCode.code == updateDiscountBinding.codeEt.text.toString()) {
                updateDiscountBinding.codeEt.error = "code have no change to save"
            } else if (!updateDiscountBinding.codeEt.text.toString().isNullOrEmpty()) {
                handleEditAction()
            } else {
                updateDiscountBinding.codeEt.error = "should have a code"
            }
        }

        observeUpdateDiscount()
    }


    companion object {
        private lateinit var priceRule: PriceRule
        private lateinit var discountCode: DiscountCode
        private lateinit var discountListener: DiscountListener
        const val TAG = "UpdateDiscountFragment"

        fun newInstance(
            priceRule: PriceRule,
            discountCode: DiscountCode,
            discountListener: DiscountListener
        ): UpdateDiscountFragment {
            Companion.priceRule = priceRule
            Companion.discountCode = discountCode
            Companion.discountListener = discountListener

            return UpdateDiscountFragment()
        }
    }




    private fun observeUpdateDiscount() {
        lifecycleScope.launch {
            viewModel.updateDiscountState.collectLatest {
                when (it) {
                    is UiState.Loading -> {
                        alertDialog.show()
                    }
                    is UiState.Success -> {
                        alertDialog.dismiss()
                        discountListener.getDiscounts()
                        Toast.makeText(requireActivity(), "Updated successfully", Toast.LENGTH_LONG)
                            .show()
                        dismiss()
                    }
                    else -> {
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(), "Update failed", Toast.LENGTH_LONG).show()
                        dismiss()
                    }
                }
            }
        }
    }

    private fun handleEditAction() {
        AlertDialog.Builder(requireContext())
            .setTitle("Edit Discount")
            .setMessage("Are you sure you want to save edit?")
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                edit()
            }
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }


    private fun edit() {
        viewModel.updateDiscount(
            priceRule.id!!, discountCode.id!!,
            DiscountCodeResponse(
                DiscountCode(
                    discountCode.id!!,
                    code = updateDiscountBinding.codeEt.text.toString()
                )
            )
        )
        alertDialog.show()
    }
}