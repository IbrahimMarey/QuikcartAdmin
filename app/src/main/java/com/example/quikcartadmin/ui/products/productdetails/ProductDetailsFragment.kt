package com.example.quikcartadmin.ui.products.productdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.FragmentAllProductsBinding
import com.example.quikcartadmin.databinding.FragmentProductDetailsBinding

class ProductDetailsFragment : Fragment() {

    lateinit var detailsBinding : FragmentProductDetailsBinding
    private lateinit var variantsAdapter: VariantsAdapter
    private lateinit var imagesAdapter: ImagesAdapter

    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailsBinding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return detailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataFromArgument()
        setUpVariantsRecyclerView()
        setUpImagesRecyclerView()
    }

    private fun setUpVariantsRecyclerView() {
        variantsAdapter = VariantsAdapter {
            // Handle item click if necessary
        }
        detailsBinding.recyclerViewVariants.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        detailsBinding.recyclerViewVariants.adapter = variantsAdapter
        variantsAdapter.submitList(args.productItem?.variants)
    }

    private fun setUpImagesRecyclerView(){
        imagesAdapter = ImagesAdapter(requireContext()) {
            // Handle item click if necessary
        }
        detailsBinding.recyclerViewImages.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        detailsBinding.recyclerViewImages.adapter = imagesAdapter
        imagesAdapter.submitList(args.productItem?.images)
    }

    private fun setDataFromArgument() {
        val productItem = args.productItem
        // Set the product details to the views
        detailsBinding.productTitleDetails.text = productItem?.title
        detailsBinding.description.text = productItem?.bodyHtml
        detailsBinding.rateOfProductDetails.rating = 3.5F
        detailsBinding.productVendorDetails.text = "Vendor :${productItem?.vendor}"
        detailsBinding.productTypeDetails.text = "Type :${productItem?.productType}"
        detailsBinding.price.text = "${productItem?.variants?.get(0)?.price} EG" ?: "100"

        val imageUrl = productItem?.image?.src ?: "https://cdn.shopify.com/s/files/1/0703/5830/2955/files/8cd561824439482e3cea5ba8e3a6e2f6.jpg?v=1716233144"
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.product)
            .error(R.drawable.ic_close)
            .into(detailsBinding.image)

    }

}