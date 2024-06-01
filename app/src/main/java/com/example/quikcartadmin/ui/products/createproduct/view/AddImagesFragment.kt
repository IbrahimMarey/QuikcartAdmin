package com.example.quikcartadmin.ui.products.createproduct.view

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.example.quikcartadmin.models.entities.products.ImagesItem
import com.example.quikcartadmin.models.entities.products.SingleImage
import com.example.quikcartadmin.models.entities.products.SingleImageBody
import com.example.quikcartadmin.ui.products.createproduct.viewmodel.UploadImageViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

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
                                adminGraphqlApiId = null,
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
                        Toast.makeText(requireContext(), "Failed to upload image: ${state.msg}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setUpImagesRecyclerView() {
        imagesAdapter = AddImagesAdapter(requireContext()) {
            // Handle item click if necessary
        }
        imagesBinding.recyclerViewImages.layoutManager = GridLayoutManager(
            requireContext(),
            2,
            GridLayoutManager.VERTICAL,
            false
        )
        imagesBinding.recyclerViewImages.adapter = imagesAdapter
        imagesAdapter.submitList(args.productInfo?.images ?: emptyList())
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
                    val productId = args.productInfo?.id ?: return@uploadImage
                    val imageBody = SingleImageBody(
                        image = SingleImage(
                            width = 110,
                            height = 140,
                            position = 1,
                            alt = null,
                            src = imageUrl,
                            variantIds = emptyList()
                        )
                    )
                    uploadImageViewModel.uploadImageToProduct(productId, imageBody)
                },
                onFailure = { exception ->
                    Toast.makeText(requireContext(), "Failed to upload image: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    companion object {
        const val IMAGE_PICK_CODE = 1000
    }
}
