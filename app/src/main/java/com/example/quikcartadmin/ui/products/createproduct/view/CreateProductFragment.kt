package com.example.quikcartadmin.ui.products.createproduct.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.FragmentCreateProductBinding
import com.example.quikcartadmin.helpers.GetTime
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.products.Image
import com.example.quikcartadmin.models.entities.products.Product
import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import com.example.quikcartadmin.ui.products.createproduct.viewmodel.UpdateProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateProductFragment : Fragment() {

    private lateinit var createProductBinding: FragmentCreateProductBinding
    private val args: CreateProductFragmentArgs by navArgs()
    private val updateProductViewModel: UpdateProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createProductBinding = FragmentCreateProductBinding.inflate(inflater, container, false)
        return createProductBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createProductBinding.addImages.setOnClickListener {
            val action = CreateProductFragmentDirections.actionCreateProductFragmentToAddImagesFragment()
            action.setProductInfo(args.productInfo)
            findNavController().navigate(action)
        }

        createProductBinding.addVaranint.setOnClickListener {
            val action = CreateProductFragmentDirections.actionCreateProductFragmentToAddVariantFragment()
            action.setProductsItem(args.productInfo)
            findNavController().navigate(action)
        }

        if (args.isCreated){
            createProductBinding.editProductBtn.setText("Update Product")
        }else{
            createProductBinding.editProductBtn.setText("Create Product")
        }

        createProductBinding.editProductBtn.setOnClickListener {
            val updatedProduct = collectProductData()
            observeViewModel()
            if (updatedProduct != null) {
                updateProductViewModel.updateProduct(args.productInfo?.id ?: 0, updatedProduct)
            }
        }

        setUpdatingArgsInUi()
    }


    private fun observeViewModel() {
        lifecycleScope.launch {
            updateProductViewModel.updateProductState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        createProductBinding.progressBar.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        Toast.makeText(requireContext(), "Updating successfully", Toast.LENGTH_SHORT).show()
                        createProductBinding.progressBar.visibility = View.GONE
                    }
                    is UiState.Failed -> {
                        //error state
                        val errorMessage = state.msg.message
                        createProductBinding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun collectProductData(): SingleProductsResponse? {

        val title = createProductBinding.titleEt.text.toString()
        val description = createProductBinding.descriptionEt.text.toString()
        val category = createProductBinding.categoryEt.text.toString()
        val vendor = createProductBinding.vendorEt.text.toString()
        val price = createProductBinding.valueEt.text.toString().toDoubleOrNull()

        if (title.isEmpty() || description.isEmpty() || category.isEmpty() || vendor.isEmpty() || price == null) {
            Toast.makeText(requireContext(), "Please, fill all fields", Toast.LENGTH_SHORT).show()
            return null
        }

        val updatingProduct = args.productInfo

        val updatedProduct = Product(
            id = updatingProduct?.id ?: 0L,
            title = title,
            bodyHtml = description,
            productType = category,
            vendor = vendor,
            variants = updatingProduct?.variants?.map { it.copy(price = price.toString()) } ?: emptyList(),

            image = updatingProduct?.image ?: Image(
                updatedAt = GetTime.getCurrentTime(),
                src = null,
                productId = null,
                adminGraphqlApiId = null,
                alt = null,
                width = null,
                createdAt = updatingProduct?.createdAt ?: "",
                variantIds = null,
                id = null,
                position = null,
                height = null
            ),

            images = updatingProduct?.images ?: emptyList(),
            createdAt = updatingProduct?.createdAt ?: "",
            handle = updatingProduct?.handle ?: "",
            tags = updatingProduct?.tags ?: "",
            publishedScope = updatingProduct?.publishedScope ?: "",
            templateSuffix = updatingProduct?.templateSuffix ?: "",
            updatedAt = GetTime.getCurrentTime(),
            adminGraphqlApiId = updatingProduct?.adminGraphqlApiId ?: "",
            options = updatingProduct?.options ?: emptyList(),
            publishedAt = updatingProduct?.publishedAt ?: "",
            status = updatingProduct?.status ?: ""
        )

        return SingleProductsResponse(product = updatedProduct)
    }
    private fun setUpdatingArgsInUi() {
        val updatingProduct = args.productInfo

        createProductBinding.titleEt.setText(updatingProduct?.title)
        createProductBinding.descriptionEt.setText(updatingProduct?.bodyHtml)
        createProductBinding.categoryEt.setText(updatingProduct?.productType)
        createProductBinding.vendorEt.setText(updatingProduct?.vendor)
        createProductBinding.valueEt.setText(updatingProduct?.variants?.get(0)?.price)

        val imageUrl = updatingProduct?.image?.src ?: "https://cdn.shopify.com/s/files/1/0703/5830/2955/files/8cd561824439482e3cea5ba8e3a6e2f6.jpg?v=1716233144"
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.product)
            .error(R.drawable.ic_close)
            .into(createProductBinding.imageOfProductCreate)
    }
}