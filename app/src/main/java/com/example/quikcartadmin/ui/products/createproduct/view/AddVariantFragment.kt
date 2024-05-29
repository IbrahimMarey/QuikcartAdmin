package com.example.quikcartadmin.ui.products.createproduct.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quikcartadmin.databinding.FragmentAddVariantBinding

class AddVariantFragment : Fragment() {

    private lateinit var variantsBinding : FragmentAddVariantBinding
    private lateinit var variantsAdapter: AddVariantsAdapter
    private val args : AddVariantFragmentArgs by navArgs()
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

        variantsBinding.addProduct.setOnClickListener {
            openDialogToAddVariant()
        }
    }
    private fun setVariantsRecyclerView(){
        variantsAdapter = AddVariantsAdapter {
            // Handle item click if necessary
        }

        variantsBinding.recyclerViewImages.layoutManager = GridLayoutManager(
            requireContext(),
            2,
            GridLayoutManager.VERTICAL,
            false
        )

        variantsBinding.recyclerViewImages.adapter = variantsAdapter
        variantsAdapter.submitList(args.productsItem?.variants ?: emptyList())
    }

    private fun openDialogToAddVariant() {

    }


}