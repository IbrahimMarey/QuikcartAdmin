package com.example.quikcartadmin.ui.products.createproduct.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.FragmentCreateProductBinding
import com.example.quikcartadmin.helpers.FirebaseStorageHelper
import com.example.quikcartadmin.helpers.GetTime
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.products.Image
import com.example.quikcartadmin.models.entities.products.Product
import com.example.quikcartadmin.models.entities.products.ProductBody
import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import com.example.quikcartadmin.ui.products.createproduct.viewmodel.CreateProductViewModel
import com.example.quikcartadmin.ui.products.createproduct.viewmodel.UpdateProductViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class CreateProductFragment : Fragment() {

    private lateinit var createProductBinding: FragmentCreateProductBinding
    private val args: CreateProductFragmentArgs by navArgs()
    private val updateProductViewModel: UpdateProductViewModel by viewModels()
    private val createProductViewModel: CreateProductViewModel by viewModels()
    private val firebaseStorageHelper = FirebaseStorageHelper()
    private var imageUploadCallback: ((String?) -> Unit)? = null

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
            setUpdatingArgsInUi()

        }else if (args.isCreated == false){
            createProductBinding.editProductBtn.setText("Create Product")
            createProductBinding.addImages.isEnabled = false
            createProductBinding.addVaranint.isEnabled = false
        }

        createProductBinding.editProductBtn.setOnClickListener {

            if (args.isCreated) {
                val updatedProduct = collectProductData()
                observeViewModel()
                if (updatedProduct != null) {
                    updateProductViewModel.updateProduct(args.productInfo?.id ?: 0, updatedProduct)
                }

            } else {
                createProductObservation()
            }
        }

        val categoryTextView = createProductBinding.categoryEt
        val vendorTextView = createProductBinding.vendorEt

        categoryTextView.setOnClickListener { showCategoryPopupMenu(categoryTextView) }
        vendorTextView.setOnClickListener { showVendorPopupMenu(vendorTextView) }

    }

    private fun createProductObservation() {

        uploadImageAndCreateProduct {productBody ->
            lifecycleScope.launchWhenStarted {
                createProductViewModel.createProductState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            createProductBinding.progressBar.visibility = View.VISIBLE
                        }
                        is UiState.Success -> {
                            // Handle success state
                            createProductBinding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), "Product Created Successfully", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Failed -> {
                            // Handle error state
                            createProductBinding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), "Failed to create product: ${state.msg}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            if (productBody != null) {
                createProductViewModel.createProduct(productBody)
            }
        }
    }

    fun uploadImageAndCreateProduct(callback: (ProductBody?) -> Unit) {
        pickImageForUpload { imageUrl ->
            if (imageUrl != null) {
                createProductWithImage(imageUrl) { productBody ->
                    callback(productBody)
                }
            } else {
                callback(null)
            }
        }
    }


    private fun createProductWithImage(imageUrl: String, callback: (ProductBody?) -> Unit) {

        val title = createProductBinding.titleEt.text.toString()
        val description = createProductBinding.descriptionEt.text.toString()
        val price = createProductBinding.valueEt.text.toString().toDoubleOrNull()
        val category = createProductBinding.categoryEt.text.toString()
        val vendor = createProductBinding.vendorEt.text.toString()

        if (title.isEmpty() || description.isEmpty() || category.isEmpty() || vendor.isEmpty() || price == null) {
            Toast.makeText(requireContext(), "Please, fill all fields", Toast.LENGTH_SHORT).show()
            callback(null)
            return
        }

        val product = Product(
            image = Image(
                updatedAt = GetTime.getCurrentTime(),
                src = imageUrl,
                productId = null,
                adminGraphqlApiId = null,
                alt = null,
                width = null,
                createdAt = GetTime.getCurrentTime(),
                variantIds = null,
                id = null,
                position = null,
                height = null
            ),
            bodyHtml = description,
            images = emptyList(),
            createdAt = GetTime.getCurrentTime(),
            handle = "burton-custom-freestyle-151",
            variants = emptyList(),
            title = title,
            tags = "",
            publishedScope = "",
            productType = category,
            templateSuffix = "",
            updatedAt = GetTime.getCurrentTime(),
            vendor = vendor,
            adminGraphqlApiId = "",
            options = emptyList(),
            id = Random.nextLong(),
            publishedAt = "",
            status = "draft"

        )

        val productBody = ProductBody(product)

        callback(productBody)
    }

    private fun setProductValues(callback: (ProductBody) -> Unit) {
        val title = createProductBinding.titleEt.text.toString()
        val description = createProductBinding.descriptionEt.text.toString()
        val price = createProductBinding.valueEt.text.toString().toDoubleOrNull()
        val category = createProductBinding.categoryEt.text.toString()
        val vendor = createProductBinding.vendorEt.text.toString()


        if (title.isEmpty() || description.isEmpty() || category.isEmpty() || vendor.isEmpty() || price == null) {
            Toast.makeText(requireContext(), "Please, fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        pickImageForUpload { imageUrl ->
            if (imageUrl != null) {
                Toast.makeText(requireContext(), "Image uploaded: $imageUrl", Toast.LENGTH_SHORT).show()

                val product = Product(
                    image = Image(
                        updatedAt = GetTime.getCurrentTime(),
                        src = imageUrl,
                        productId = null,
                        adminGraphqlApiId = null,
                        alt = null,
                        width = null,
                        createdAt = GetTime.getCurrentTime(),
                        variantIds = null,
                        id = null,
                        position = null,
                        height = null
                    ),
                    bodyHtml = description,
                    images = emptyList(),
                    createdAt = GetTime.getCurrentTime(),
                    handle = "burton-custom-freestyle-151",
                    variants = emptyList(),
                    title = title,
                    tags = "",
                    publishedScope = "",
                    productType = category,
                    templateSuffix = "",
                    updatedAt = GetTime.getCurrentTime(),
                    vendor = vendor,
                    adminGraphqlApiId = "",
                    options = emptyList(),
                    id = Random.nextLong(),
                    publishedAt = "",
                    status = "draft"
                )

                val productBody = ProductBody(product)
                callback(productBody)

            } else {
                Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        }
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

    fun pickImageForUpload(callback: (String?) -> Unit) {
        imageUploadCallback = callback
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            selectedImageUri?.let { uri ->
                firebaseStorageHelper.uploadImage(uri,
                    onSuccess = { imageUrl ->
                        imageUploadCallback?.invoke(imageUrl)
                    },
                    onFailure = { exception ->
                        Toast.makeText(requireContext(), "Failed to upload image: ${exception.message}", Toast.LENGTH_SHORT).show()
                        imageUploadCallback?.invoke(null)
                    }
                )
            } ?: run {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
                imageUploadCallback?.invoke(null)
            }
        }
    }
    companion object {
        const val IMAGE_PICK_CODE = 1001
    }


    private fun showCategoryPopupMenu(anchorView: View) {
        val popupMenu = PopupMenu(requireContext(), anchorView)
        popupMenu.menuInflater.inflate(R.menu.category_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.accessories -> createProductBinding.categoryEt.setText("ACCESSORIES")
                R.id.shoes -> createProductBinding.categoryEt.setText("SHOES")
                R.id.tshirts -> createProductBinding.categoryEt.setText("T-SHIRTS")
            }
            true
        }

        popupMenu.show()
    }


    private fun showVendorPopupMenu(anchorView: View) {
        val popupMenu = PopupMenu(requireContext(), anchorView)
        popupMenu.menuInflater.inflate(R.menu.vendor_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.vans -> createProductBinding.vendorEt.setText("VANS")
                R.id.puma -> createProductBinding.vendorEt.setText("PUMA")
                R.id.timberland -> createProductBinding.vendorEt.setText("TIMBERLAND")
                R.id.supra -> createProductBinding.vendorEt.setText("SUPRA")
                R.id.nike -> createProductBinding.vendorEt.setText("NIKE")
                R.id.palladium -> createProductBinding.vendorEt.setText("PALLADIUM")
                R.id.herschel -> createProductBinding.vendorEt.setText("HERSCHEL")
                R.id.flexfit -> createProductBinding.vendorEt.setText("FLEX FIT")
                R.id.drmartens -> createProductBinding.vendorEt.setText("Dr. Martens")
                R.id.adidas -> createProductBinding.vendorEt.setText("Adidas")
                R.id.asicstiger -> createProductBinding.vendorEt.setText("Asics Tiger")
                R.id.converse -> createProductBinding.vendorEt.setText("Converse")
            }
            true
        }

        popupMenu.show()
    }

}