package com.example.quikcartadmin.ui.products.createproduct.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quikcartadmin.databinding.FragmentAddImagesBinding

class AddImagesFragment : Fragment() {

    private lateinit var imagesBinding: FragmentAddImagesBinding
    private lateinit var imagesAdapter: AddImagesAdapter

    private val args : AddImagesFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        imagesBinding = FragmentAddImagesBinding.inflate(inflater,container,false)
        return imagesBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpImagesRecyclerView()
    }

    private fun setUpImagesRecyclerView() {
        imagesAdapter = AddImagesAdapter(requireContext()) {
            // Handle item click if necessary
        }
        imagesBinding.recyclerViewImages.layoutManager = GridLayoutManager(
            requireContext(),
            3,
            GridLayoutManager.VERTICAL,
            false
        )

        imagesBinding.recyclerViewImages.adapter = imagesAdapter
        imagesAdapter.submitList(args.productInfo?.images ?: emptyList())
    }

}