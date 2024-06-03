package com.example.quikcartadmin.ui.coupons.allrulesprice.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
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
    private var rulesList: List<PriceRule> = ArrayList()

    private val viewModel: AllPriceRulesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rulesBinding = FragmentAllRulesBinding.inflate(inflater, container, false)
        return rulesBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rulesBinding.addFloatingBtn.setOnClickListener {
            Navigation.findNavController(rulesBinding.root).navigate(R.id.createRulePriceFragment)
        }

        setUpRecyclerView()
        observeGetAllPriceRules()
    }

    private fun observeGetAllPriceRules() {
        lifecycleScope.launch {
            viewModel.ruleState.collectLatest {
                when (it) {
                    is UiState.Loading -> {
                        rulesBinding.progressbar.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        rulesBinding.progressbar.visibility = View.GONE
                        rulesList = it.data
                        rulesAdapter.submitList(rulesList)
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
            viewModel.deletePriceRule(it.id!!)
        }

        rulesBinding.rulesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rulesAdapter
        }
    }


}