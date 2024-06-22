package com.example.quikcartadmin.ui.products.createproduct.view

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.FragmentAddImagesBinding
import com.example.quikcartadmin.helpers.FirebaseStorageHelper
import com.example.quikcartadmin.helpers.GetTime
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.products.Image
import com.example.quikcartadmin.models.entities.products.ImagesItem
import com.example.quikcartadmin.models.entities.products.Product
import com.example.quikcartadmin.models.entities.products.SingleImage
import com.example.quikcartadmin.models.entities.products.SingleImageBody
import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import com.example.quikcartadmin.models.entities.products.VariantsItem
import com.example.quikcartadmin.ui.products.createproduct.viewmodel.UpdateProductViewModel
import com.example.quikcartadmin.ui.products.createproduct.viewmodel.UploadImageViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class AddImagesFragment : Fragment() {

    private lateinit var imagesBinding: FragmentAddImagesBinding
    private lateinit var imagesAdapter: AddImagesAdapter
    private val args: AddImagesFragmentArgs by navArgs()
    private val firebaseStorageHelper = FirebaseStorageHelper()
    private val uploadImageViewModel: UploadImageViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private var dialogImageView: ImageView? = null
    private var dialog: Dialog? = null

    private var imagesList = mutableListOf<ImagesItem>()
    private val updateProductViewModel: UpdateProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        imagesBinding = FragmentAddImagesBinding.inflate(inflater, container, false)
        return imagesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpImagesRecyclerView()

        imagesBinding.addImage.setOnClickListener {
            showAddImageDialog()
        }

        observationUploadImage()
    }


    private fun observationUploadImage(){

        lifecycleScope.launchWhenStarted {
            uploadImageViewModel.uploadImageState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // Show loading indicator if needed
                    }
                    is UiState.Success -> {
                        val uploadedImage = state.data.image
                        val currentImages = imagesAdapter.currentList.toMutableList()
                        currentImages.add(
                            ImagesItem(
                                updatedAt = GetTime.getCurrentTime(),
                                productId = uploadedImage?.productId,
                                adminGraphqlApiId = uploadedImage?.adminGraphqlApiId.toString(),
                                alt = uploadedImage?.alt,
                                width = uploadedImage?.width,
                                createdAt = uploadedImage?.createdAt,
                                variantIds = emptyList(),
                                id = uploadedImage?.id,
                                position = uploadedImage?.position,
                                height = uploadedImage?.height,
                                src = uploadedImage?.src
                            )
                        )
                        imagesAdapter.submitList(currentImages)
                    }
                    is UiState.Failed -> {
                        Log.i("TAG", "onViewCreated: ${state.msg}")
                        makeAlert("Failed to upload image", "Make sure your connection to can Upload image.")
                    }
                }
            }
        }
    }

    private fun setUpImagesRecyclerView() {
        imagesAdapter = AddImagesAdapter(requireContext()) {

            imagesList.remove(it)

            imagesAdapter.submitList(imagesList)
            // add new variant to product
            val updatedProduct = collectProductData()
            observeUpdateViewModel()
            if (updatedProduct != null) {
                updateProductViewModel.updateProduct(args.productInfo?.id ?: 0, updatedProduct)
            }

        }
        imagesBinding.recyclerViewImages.layoutManager = GridLayoutManager(
            requireContext(),
            2,
            GridLayoutManager.VERTICAL,
            false
        )
        imagesBinding.recyclerViewImages.adapter = imagesAdapter
        imagesList = (args.productInfo?.images ?: emptyList()).toMutableList()
        imagesAdapter.submitList(imagesList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let {
                dialogImageView?.let { imageView ->
                    Picasso.get().load(it).into(imageView)
                }
            }
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun showAddImageDialog() {
        dialog = Dialog(requireActivity()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.add_image_dialog)

            val cancelBtn = findViewById<Button>(R.id.cancel)
            cancelBtn.setOnClickListener {
                dismiss()
            }

            val confirmBtn = findViewById<Button>(R.id.confirm)
            confirmBtn.setOnClickListener {
                uploadImageToFirebase()
                dismiss()
            }

            dialogImageView = findViewById(R.id.image_add)
            dialogImageView?.setOnClickListener {
                selectImageFromGallery()
            }

            show()
        }
    }

    private fun uploadImageToFirebase() {
        selectedImageUri?.let { uri ->
            firebaseStorageHelper.uploadImage(uri,
                onSuccess = { imageUrl ->
                    Log.i("TAG", "uploadImageToFirebase: $imageUrl")

                    val imageBody = ImagesItem(
                            productId = args.productInfo?.id ?: 0L,
                            src = imageUrl,
                        )

                    imagesList.add(imageBody)

                    imagesAdapter.submitList(imagesList)
                    // add new variant to product
                    handleEditAction()


                },
                onFailure = { exception ->
                    Log.i("TAG", "uploadImageToFirebase: $exception")
                    makeAlert("Failed to upload image", "make sure connection to can upload Image.")
                }
            )
        }
    }

    private fun observeUpdateViewModel() {
        lifecycleScope.launch {
            updateProductViewModel.updateProductState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        imagesBinding.progressBar.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        makeAlert("Updating Image's product successfully", "Updating to add new Image to product is done.")
                        imagesBinding.progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_addImagesFragment_to_home)
                    }
                    is UiState.Failed -> {
                        //error state
                        val errorMessage = state.msg.message
                        imagesBinding.progressBar.visibility = View.GONE
                        makeAlert("Updating Image's product Failed", "make sure connection to can add new Image.")
                    }
                }
            }
        }
    }

    companion object {
        const val IMAGE_PICK_CODE = 1000
    }



    private fun collectProductData(): SingleProductsResponse {

        val updatingProduct = args.productInfo
        Log.i("TAG", "collectProductData: ${updatingProduct?.images}")

        val updatedProduct = Product(
            id = updatingProduct?.id ?: 0L,
            title = updatingProduct?.title ?: "",
            bodyHtml = updatingProduct?.bodyHtml ?: "",
            productType = updatingProduct?.productType ?: "",
            vendor = updatingProduct?.vendor ?: "",
            variants = updatingProduct?.variants ?: emptyList(),

            image = updatingProduct?.image ?: Image(
                id = updatingProduct?.images?.get(0)?.id ?: 0L,
                src = updatingProduct?.image?.src ?: "",
                productId = updatingProduct?.id ?: 0L,
            ),

            images = imagesList,
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


    private fun handleEditAction() {
        AlertDialog.Builder(requireContext())
            .setTitle("Add new Image")
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
