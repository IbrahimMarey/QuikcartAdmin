package com.example.quikcartadmin.ui.coupons.allrulesprice.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.FragmentAllRulesBinding
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.PriceRule
import com.example.quikcartadmin.ui.coupons.allrulesprice.viewmodel.AllPriceRulesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllRulesFragment : Fragment() {

    private lateinit var rulesBinding: FragmentAllRulesBinding

    private lateinit var rulesAdapter: RulesAdapter
    private val viewModel: AllPriceRulesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rulesBinding = FragmentAllRulesBinding.inflate(inflater, container, false)

        setUpRecyclerView()
        observeGetAllPriceRules()

        return rulesBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rulesBinding.addFloatingBtn.setOnClickListener {
            findNavController().navigate(R.id.createRulePriceFragment)
        }

      //  setUpRecyclerView()
      //  observeGetAllPriceRules()
    }

    private fun observeGetAllPriceRules() {
        viewModel.getPriceRules()

        lifecycleScope.launch {
            viewModel.ruleState.collectLatest {
                when (it) {
                    is UiState.Loading -> {
                        rulesBinding.progressbar.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        rulesBinding.progressbar.visibility = View.GONE
                        rulesAdapter.submitList(it.data)
                    }
                    else -> {
                        rulesBinding.progressbar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        rulesAdapter = RulesAdapter{
            handleDeleteAction(it.id!!)
        }

        rulesBinding.rulesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rulesAdapter
        }
    }

    private fun handleDeleteAction(id : Long){
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Discount")
            .setMessage("Are you sure you want to delete this Price Rule ?")
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                viewModel.deletePriceRule(id)
                viewModel.getPriceRules()
            }
            .setNegativeButton("Cancel", null)
            .setIcon(R.drawable.baseline_add_alert_24)
            .show()
    }

}