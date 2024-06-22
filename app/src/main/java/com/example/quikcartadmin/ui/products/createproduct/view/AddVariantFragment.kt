package com.example.quikcartadmin.ui.products.createproduct.view

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.FragmentAddVariantBinding
import com.example.quikcartadmin.helpers.GetTime
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.PriceRuleResponse
import com.example.quikcartadmin.models.entities.products.Image
import com.example.quikcartadmin.models.entities.products.OptionsItem
import com.example.quikcartadmin.models.entities.products.Product
import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import com.example.quikcartadmin.models.entities.products.VariantsItem
import com.example.quikcartadmin.ui.products.createproduct.viewmodel.UpdateProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class AddVariantFragment : Fragment() {

    private lateinit var variantsBinding : FragmentAddVariantBinding
    private lateinit var variantsAdapter: AddVariantsAdapter
    private val args : AddVariantFragmentArgs by navArgs()
    private var variantsList = mutableListOf<VariantsItem>()
    private val updateProductViewModel: UpdateProductViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        variantsBinding = FragmentAddVariantBinding.inflate(inflater, container, false)
        return variantsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariantsRecyclerView()

        variantsBinding.addVariant.setOnClickListener {
            showInputDialogToAddVariant(option1 = "", option2 = "", price = 0)
        }

    }


    private fun setVariantsRecyclerView(){
        variantsAdapter = AddVariantsAdapter {
            // Handle item click if necessary

            variantsList.remove(it)

            variantsAdapter.submitList(variantsList)


            // add new variant to product
            val updatedProduct = collectProductData()
            observeUpdateViewModel()
            if (updatedProduct != null) {
                updateProductViewModel.updateProduct(args.productsItem?.id ?: 0, updatedProduct)
            }

        }

        variantsBinding.recyclerViewImages.layoutManager = GridLayoutManager(
            requireContext(),
            2,
            GridLayoutManager.VERTICAL,
            false
        )

        variantsBinding.recyclerViewImages.adapter = variantsAdapter
        variantsList = ((args.productsItem?.variants?: emptyList()).toMutableList())
        variantsAdapter.submitList(variantsList)
    }

    private fun showInputDialogToAddVariant(option1: String, option2: String, price: Int) {
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(16, 16, 16, 16)
        layout.elevation = 2F

        val option1Input = EditText(requireContext())
        option1Input.hint = "Enter Size"
        option1Input.setText(option1)
        layout.addView(option1Input)

        val option2Input = EditText(requireContext())
        option2Input.hint = "Enter Color"
        option2Input.setText(option2)
        layout.addView(option2Input)

        val priceInput = EditText(requireContext())
        priceInput.hint = "Enter price"
        priceInput.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        priceInput.setText(price.toString())
        layout.addView(priceInput)

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext(), R.style.RoundedAlertDialogStyle)
        builder.setTitle("Add Variant")
            .setView(layout)
            .setPositiveButton("OK") { dialog, which ->
                val _option1 = option1Input.text.toString()
                val _option2 = option2Input.text.toString()
                val _price = priceInput.text.toString().toIntOrNull() ?: 0

                val variantItem = VariantsItem(
                    product_id = args.productsItem?.id ?: 0L,
                    price = _price.toString(),
                    title = "$_option1 / $_option2",
                    option1 = _option1,
                    option2 = _option2,
                    option3 = null
                )

                variantsList.add(variantItem)

                variantsAdapter.submitList(variantsList)

                // add new variant to product
                handleEditAction()
            }
            .setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.xd_dark_pink))
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.xd_dark_pink))
        }

        alertDialog.show()
    }


    private fun collectProductData(): SingleProductsResponse {
        Log.i("TAG", "collectProductData: $variantsList")

        val updatingProduct = args.productsItem
        Log.i("TAG", "collectProductData: ${updatingProduct?.variants}")
        
        val updatedProduct = Product(
            id = updatingProduct?.id ?: 0L,
            title = updatingProduct?.title ?: "",
            bodyHtml = updatingProduct?.bodyHtml ?: "",
            productType = updatingProduct?.productType ?: "",
            vendor = updatingProduct?.vendor ?: "",
            variants = variantsList,

            image = updatingProduct?.image ?: Image(
                src = updatingProduct?.image?.src ?: "",
                productId = updatingProduct?.id ?: 0L,
                id = updatingProduct?.image?.id ?: 0L
            ),

            images = updatingProduct?.images ?: emptyList(),
            createdAt = updatingProduct?.createdAt ?: "",
            handle = updatingProduct?.handle ?: "",
            tags = updatingProduct?.tags ?: "",
            publishedScope = updatingProduct?.publishedScope ?: "",
            templateSuffix = updatingProduct?.templateSuffix ?: "",
            updatedAt = GetTime.getCurrentTime(),
            adminGraphqlApiId = updatingProduct?.adminGraphqlApiId ?: "",
            options = arrayOf(
                OptionsItem(
                    id = updatingProduct?.options?.get(0)?.id ?: 0L,
                    productId = updatingProduct?.id ?: 0L,
                    name = "Size",
                    position = 1,
                    values = arrayOf(
                        variantsList[0].option1
                    )
                ),
                OptionsItem(
                    id = updatingProduct?.options?.get(1)?.id ?: 0L,
                    productId = updatingProduct?.id ?: 0L,
                    name = "Color",
                    position = 2,
                    values = arrayOf(
                        variantsList[0].option2
                    )
                )
            ).toList(),
            publishedAt = updatingProduct?.publishedAt ?: "",
            status = updatingProduct?.status ?: ""
        )

        return SingleProductsResponse(product = updatedProduct)
    }


    private fun observeUpdateViewModel() {
        lifecycleScope.launch {
            updateProductViewModel.updateProductState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        variantsBinding.progressBar.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        makeAlert("Updating Variant's Product successfully", "Adding new variant to product is done")
                        variantsBinding.progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_addVariantFragment_to_home)
                    }
                    is UiState.Failed -> {
                        //error state
                        val errorMessage = state.msg.message
                        variantsBinding.progressBar.visibility = View.GONE
                        makeAlert("Updating Variant's Product failed", "make sure about connection to can add new variant.")

                    }
                }
            }
        }
    }


    private fun handleEditAction() {
        AlertDialog.Builder(requireContext())
            .setTitle("Add new Variant")
            .setMessage("Are you sure you want to save changes?")
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                val updatedProduct = collectProductData()
                observeUpdateViewModel()
                if (updatedProduct != null) {
                    updateProductViewModel.updateProduct(args.productsItem?.id ?: 0, updatedProduct)
                }
            }
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
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