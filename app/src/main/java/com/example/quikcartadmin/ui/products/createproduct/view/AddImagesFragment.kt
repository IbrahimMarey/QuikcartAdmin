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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
                        Toast.makeText(requireContext(), "Image Uploaded", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Failed -> {
                        Log.i("TAG", "onViewCreated: ${state.msg}")
                        Toast.makeText(requireContext(), "Failed to upload image: ${state.msg}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setUpImagesRecyclerView() {
        imagesAdapter = AddImagesAdapter(requireContext()) {

            imagesList.remove(it)

            imagesAdapter.submitList(imagesList)
            Toast.makeText(requireContext(), "Image removed", Toast.LENGTH_SHORT).show()

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
                            id = Random.nextLong(9),
                            productId = args.productInfo?.id ?: 0L,
                            src = imageUrl,
                        )

                    imagesList.add(imageBody)

                    imagesAdapter.submitList(imagesList)
                    Toast.makeText(requireContext(), "Image added", Toast.LENGTH_SHORT).show()

                    // add new variant to product
                    val updatedProduct = collectProductData()
                    observeUpdateViewModel()
                    if (updatedProduct != null) {
                        updateProductViewModel.updateProduct(args.productInfo?.id ?: 0, updatedProduct)
                    }


                },
                onFailure = { exception ->
                    Log.i("TAG", "uploadImageToFirebase: $exception")
                    Toast.makeText(requireContext(), "Failed to upload image: ${exception.message}", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(requireContext(), "Updating Image successfully", Toast.LENGTH_SHORT).show()
                        imagesBinding.progressBar.visibility = View.GONE
                    }
                    is UiState.Failed -> {
                        //error state
                        val errorMessage = state.msg.message
                        imagesBinding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
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

}
