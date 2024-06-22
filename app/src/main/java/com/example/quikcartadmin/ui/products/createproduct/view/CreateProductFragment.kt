package com.example.quikcartadmin.ui.products.createproduct.view

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.FragmentCreateProductBinding
import com.example.quikcartadmin.helpers.FirebaseStorageHelper
import com.example.quikcartadmin.helpers.GetTime
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.products.Image
import com.example.quikcartadmin.models.entities.products.ImagesItem
import com.example.quikcartadmin.models.entities.products.OptionsItem
import com.example.quikcartadmin.models.entities.products.Product
import com.example.quikcartadmin.models.entities.products.ProductBody
import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import com.example.quikcartadmin.models.entities.products.VariantsItem
import com.example.quikcartadmin.ui.products.createproduct.viewmodel.CreateProductViewModel
import com.example.quikcartadmin.ui.products.createproduct.viewmodel.UpdateProductViewModel
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
    private val variantsList = mutableListOf<VariantsItem>()
    private var uploadedImageUrl: String? = null
    private var vendorType = ""
    private var categoryType = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createProductBinding = FragmentCreateProductBinding.inflate(inflater, container, false)
        return createProductBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if (args.isCreated){
            createProductBinding.editProductBtn.setText("Update Product")
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
            setUpdatingArgsInUi()

        }else if (args.isCreated == false){
            createProductBinding.editProductBtn.setText("Create Product")

            createProductBinding.addImages.setOnClickListener {
                pickImageForUpload { imageUrl ->
                    if (imageUrl != null) {
                        Log.i("TAG", "after selected ${imageUrl}")
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.product)
                            .error(R.drawable.ic_close)
                            .into(createProductBinding.imageOfProductCreate)
                    }
                }
            }
            createProductBinding.addVaranint.setOnClickListener {
                showInputDialogToAddVariant(option1 = "", option2 = "", price = 0)
            }
        }

        createProductBinding.editProductBtn.setOnClickListener {

            if (args.isCreated) {
                handleEditAction()
            } else {
                createProductObservation()
            }
        }
        setupMenuVendor()
        setupMenuCategory()
        handleSelectedTypeValue()
        handleSelectedTypeCateogry()
    }

    private fun createProductObservation() {
        if (uploadedImageUrl == null) {
            alertToAdd("Please upload an image")
            return
        }

        if (variantsList.isEmpty()) {
            alertToAdd("Please add at least one variant")
            return
        }

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
                            findNavController().navigate(R.id.action_createProductFragment_to_home)
                            makeAlert("Product Created Successfully", "Creating new Product to our store is done")
                        }
                        is UiState.Failed -> {
                            // Handle error state
                            createProductBinding.progressBar.visibility = View.GONE
                            makeAlert("Failed to create product", "Make sure about your connection to create new Product.")
                        }
                    }
                }
            }

            if (productBody != null) {
                createProductViewModel.createProduct(productBody)
            }
        }
    }

    private fun uploadImageAndCreateProduct(callback: (ProductBody?) -> Unit) {
        pickImageForUpload { imageUrl ->
            if (imageUrl != null) {
                uploadedImageUrl = imageUrl
                val srcPart = imageUrl.substringAfter("src=")
                val srcUrl = srcPart.substringBefore(", ")

                Log.i("TAG", "when click ${srcUrl}")
                createProductWithImage(srcUrl) { productBody ->
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
        val category = categoryType
        val vendor = vendorType

        if (title.isEmpty() || description.isEmpty() || vendor.isEmpty() || category.isEmpty()) {
            alertToAdd("Please, fill all fields")
            callback(null)
            return
        }

        val productId =  Random.nextLong()
        val image_id = Random.nextLong()
        Log.i("TAG", "upload ${imageUrl}")
        Log.i("TAG", "upload ${productId}")
        val product = Product(
            image = Image(
                updatedAt = GetTime.getCurrentTime(),
                src = imageUrl,
                productId = productId,
                adminGraphqlApiId = "gid://shopify/ProductImage/${image_id}",
                alt = null,
                width = 123,
                createdAt = GetTime.getCurrentTime(),
                variantIds = null,
                id = image_id,
                position = 1,
                height = 459
            ),
            bodyHtml = description,
            images = arrayOf(
                ImagesItem(
                    id = Random.nextLong(6),
                    productId = productId,
                    src = imageUrl
                )
            ).toList(),
            createdAt = GetTime.getCurrentTime(),
            handle = "burton-custom-freestyle-151",
            variants = variantsList,
            title = title,
            tags = "",
            publishedScope = "",
            productType = category,
            templateSuffix = "",
            updatedAt = GetTime.getCurrentTime(),
            vendor = vendor,
            adminGraphqlApiId = "gid://shopify/Product/${productId}",
            options = arrayOf(
                OptionsItem(
                    id = Random.nextLong(4),
                    productId = productId,
                    name = "Size",
                    position = 1,
                    values = arrayOf(
                        variantsList[0].option1
                    )
                ),
                OptionsItem(
                    id = Random.nextLong(4),
                    productId = productId,
                    name = "Color",
                    position = 2,
                    values = arrayOf(
                        variantsList[0].option2
                    )
                )
            ).toList(),
            id = productId,
            publishedAt = "",
            status = "draft"
        )

        val productBody = ProductBody(product)
        Log.i("TAG", "************: ${productBody.product?.variants}")
        callback(productBody)
    }


    private fun observeUpdateViewModel() {
        lifecycleScope.launch {
            updateProductViewModel.updateProductState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        createProductBinding.progressBar.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        makeAlert("Updating successfully", "Updating product is done.")
                        createProductBinding.progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_createProductFragment_to_home)
                    }
                    is UiState.Failed -> {
                        //error state
                        val errorMessage = state.msg.message
                        createProductBinding.progressBar.visibility = View.GONE
                        makeAlert("Updating Failed", "updating failed, make sure about connection to can update.")
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

        if (title.isEmpty() || description.isEmpty() || category.isEmpty() || vendor.isEmpty()) {
            alertToAdd("Please, fill all fields")
            return null
        }

        val updatingProduct = args.productInfo

        val updatedProduct = Product(
            id = updatingProduct?.id ?: 0L,
            title = title,
            bodyHtml = description,
            productType = category,
            vendor = vendor,
            variants = updatingProduct?.variants ?: emptyList(),

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

        val imageUrl = updatingProduct?.image?.src ?: "https://cdn.shopify.com/s/files/1/0703/5830/2955/files/8cd561824439482e3cea5ba8e3a6e2f6.jpg?v=1716233144"
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.product)
            .error(R.drawable.ic_close)
            .into(createProductBinding.imageOfProductCreate)
    }

    fun pickImageForUpload(callback: (String?) -> Unit) {
        if (uploadedImageUrl.isNullOrEmpty()) {
            imageUploadCallback = callback
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, IMAGE_PICK_CODE)
        } else {
            createProductWithImage(uploadedImageUrl!!) { productBody ->
                callback(productBody?.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            selectedImageUri?.let { uri ->
                firebaseStorageHelper.uploadImage(uri,
                    onSuccess = { imageUrl ->
                        uploadedImageUrl = imageUrl
                        Log.i("TAG", "${imageUrl}")
                        imageUploadCallback?.invoke(imageUrl)
                    },
                    onFailure = { exception ->
                        makeAlert("Failed to upload image", "Ensure about connection and try again.")
                        imageUploadCallback?.invoke(null)
                    }
                )
            } ?: run {
                makeAlert("Failed to upload image", "No image selected.")
                imageUploadCallback?.invoke(null)
            }
        }
    }

    companion object {
        const val IMAGE_PICK_CODE = 1003
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

                val variantsId = Random.nextLong(6)
                val variantItem = VariantsItem(
                    id = variantsId,
                    product_id = 0L,
                    title = "$_option1/$_option2",
                    price = _price.toString(),
                    sku = "",
                    position = variantsList.size + 1,
                    inventory_policy = "deny",
                    compare_at_price = null,
                    fulfillment_service = "manual",
                    inventory_management = "shopify",
                    option1 = _option1,
                    option2 = _option2,
                    option3 = null,
                    created_at = GetTime.getCurrentTime(),
                    updated_at = GetTime.getCurrentTime(),
                    taxable = true,
                    barcode = null,
                    grams = 0,
                    weight = 0.0,
                    weight_unit = "kg",
                    inventory_item_id = 47449209635051,
                    inventory_quantity = 9,
                    old_inventory_quantity = 9,
                    requires_shipping = true,
                    admin_graphql_api_id = "gid://shopify/ProductVariant/${variantsId}",
                    image_id = null
                )

                variantsList.add(variantItem)
            }
                .setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.xd_dark_pink))
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.xd_dark_pink))
        }

        alertDialog.show()
    }


    private fun setupMenuVendor() {
        val valueTypeList =
            listOf(
                "VANS",
                "PUMA",
                "TIMBERLAND",
                "SUPRA",
                "NIKE",
                "PALLADIUM",
                "HERSCHEL",
                "FLEX FIT",
                "DR. MARTENS",
                "ADIDAS",
                "CONVERSE",
                "ASICS TIGER"
                )
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.select_dialog_item, R.id.text1,valueTypeList)

        createProductBinding.vendorEt.threshold = 1
        createProductBinding.vendorEt.setAdapter(adapter)
        createProductBinding.vendorEt.setTextColor(Color.BLACK)
    }


    private fun handleSelectedTypeValue() {
        createProductBinding.vendorEt.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                vendorType = parent.getItemAtPosition(position).toString()
            }
    }



    private fun setupMenuCategory() {
        val valueTypeList =
            listOf(
                "ACCESSORIES",
                "T-SHIRTS",
                "SHOES"
            )
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.select_dialog_item, R.id.text1,valueTypeList)

        createProductBinding.categoryEt.threshold = 1
        createProductBinding.categoryEt.setAdapter(adapter)
        createProductBinding.categoryEt.setTextColor(Color.BLACK)
    }


    private fun handleSelectedTypeCateogry() {
        createProductBinding.categoryEt.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                categoryType = parent.getItemAtPosition(position).toString()
            }
    }


    private fun handleEditAction() {
        AlertDialog.Builder(requireContext())
            .setTitle("Edit Product")
            .setMessage("Are you sure you want to save changes?")
            .setPositiveButton(
                "OK"
            ) { _, _ ->

                val updatedProduct = collectProductData()
                observeUpdateViewModel()
                if (updatedProduct != null) {
                    updateProductViewModel.updateProduct(args.productInfo?.id ?: 0, updatedProduct)
                }


            }
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }


    private fun alertToAdd(msg: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Alert")
            .setMessage(msg)
            .setPositiveButton(
                "OK"
            ) { _, _ ->

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