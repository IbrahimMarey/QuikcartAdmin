package com.example.quikcartadmin.ui.coupons.createrule.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.FragmentCreateRulePriceBinding
import com.example.quikcartadmin.helpers.Constants
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.PriceRuleBody
import com.example.quikcartadmin.models.entities.coupons.SinglePriceRule
import com.example.quikcartadmin.ui.coupons.createrule.viewmodel.CreatePriceRuleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class CreateRulePriceFragment : Fragment() {

    private lateinit var createRuleBinding: FragmentCreateRulePriceBinding

    private var startDate : String? = null
    private var endDate : String? = null
    private var valueType = ""

    private val viewModel: CreatePriceRuleViewModel by viewModels()

    private val alertDialog: AlertDialog by lazy {
        Constants.createAlertDialog(requireContext(), "")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createRuleBinding = FragmentCreateRulePriceBinding.inflate(inflater, container, false)
        return createRuleBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createRuleBinding.addBtn.setOnClickListener {
            if (isDataValidate()) {
                viewModel.createPriceRule(PriceRuleBody(buildRule()))
                alertDialog.show()
            }
        }

        observeCreateRule()
        addDatePricker()
        addTimePicker()
        setupMenu()
        handleSelectedTypeValue()

    }


    private fun setupMenu() {
        val valueTypeList =
            listOf("percentage", "fixed_amount")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.select_dialog_item, R.id.text1,valueTypeList)

        createRuleBinding.valueTypeEditText.threshold = 1
        createRuleBinding.valueTypeEditText.setAdapter(adapter)
        createRuleBinding.valueTypeEditText.setTextColor(Color.BLACK)
    }

    private fun handleSelectedTypeValue() {
        createRuleBinding.valueTypeEditText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                valueType = parent.getItemAtPosition(position).toString()
            }
    }


    private fun observeCreateRule() {
        lifecycleScope.launch {
            viewModel.createRuleState.collectLatest {
                when (it) {
                    is UiState.Loading -> {
                    }
                    is UiState.Success -> {
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(), "Created successfully", Toast.LENGTH_LONG)
                            .show()
                        Navigation.findNavController(createRuleBinding.root).popBackStack()
                    }
                    is UiState.Failed -> {
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(), "Creation failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private fun buildRule(): SinglePriceRule {
        val value = (createRuleBinding.valueEt.text.toString()).toDouble() * -1.0

        return SinglePriceRule(
            title = createRuleBinding.titleEt.text.toString(),
            value = value.toString(),
            startsAt = startDate,
            endsAt = endDate,
            valueType = valueType,
            targetType = "line_item",
            allocationMethod = "across",
            targetSelection = "all",
            customerSelection = "all",
        )
    }


    private fun isDataValidate(): Boolean {
        if (createRuleBinding.titleEt.text.toString().isNullOrEmpty()) {
            createRuleBinding.titleEt.error = "should have a title"
            return false
        }
        if (createRuleBinding.valueEt.text.toString().isNullOrEmpty()) {
            createRuleBinding.valueEt.error = "should have a value"
            return false
        }

        if(!validateValue()){
            return false
        }
        if (createRuleBinding.startAtEditText.text.toString().isNullOrEmpty()) {
            createRuleBinding.startAtEditText.error = "should have a start date"
            return false
        }
        if (createRuleBinding.startTimeEditText.text.toString().isNullOrEmpty()) {
            createRuleBinding.startTimeEditText.error = "should have a start time"
            return false
        }
        if (!createRuleBinding.endsAtEditText.text.toString().isNullOrEmpty()) {
            if (createRuleBinding.endTimeEditText.text.toString().isNullOrEmpty()) {
                createRuleBinding.endTimeEditText.error = "should have a end time"
                return false
            }
        }
        if (!createRuleBinding.endTimeEditText.text.toString().isNullOrEmpty()) {
            if (createRuleBinding.endsAtEditText.text.toString().isNullOrEmpty()) {
                createRuleBinding.endsAtEditText.error = "should have a end date"
                return false
            }
        }
        if(!validateDate()){
            Toast.makeText(requireContext(),"End date before start date",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }


    private fun validateValue() : Boolean{
        if(valueType == "percentage") {
            if ((createRuleBinding.valueEt.text.toString()).toDouble() > 100
                || (createRuleBinding.valueEt.text.toString()).toDouble() < 1
            ) {
                createRuleBinding.valueEt.error = "discount value should be between 1 and 100"
                return false
            }
        }else{
            if ((createRuleBinding.valueEt.text.toString()).toDouble() == 0.0){
                createRuleBinding.valueInput.error = "discount value should not be 0"
                return false
            }
        }
        return true
    }

    private fun validateDate() : Boolean{
        startDate = createRuleBinding.startAtEditText.text.toString() + " " + createRuleBinding.startTimeEditText.text
        endDate = createRuleBinding
            .endsAtEditText.text.toString() + " " + createRuleBinding.endTimeEditText.text

        if(!createRuleBinding.endsAtEditText.text.toString().isNullOrEmpty())
            return Constants.buildDate(startDate!!)!!.before(Constants.buildDate(endDate!!))

        return true
    }


    private fun addDatePricker() {
        createRuleBinding.startAtEditText.setOnClickListener {
            showDatePicker(it)
        }
        createRuleBinding.endsAtEditText.setOnClickListener {
            showDatePicker(it)
        }
    }

    private fun addTimePicker() {
        createRuleBinding.startTimeEditText.setOnClickListener {
            showTimeDialog(it)
        }
        createRuleBinding.endTimeEditText.setOnClickListener {
            showTimeDialog(it)
        }
    }

    private fun showDatePicker(view: View) {
        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->

                val strDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                if (view.id == createRuleBinding.startAtEditText.id) {
                    createRuleBinding.startAtEditText.setText(strDate)
                } else {
                    createRuleBinding.endsAtEditText.setText(strDate)
                }
            },
            mYear!!,
            mMonth!!,
            mDay!!
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000;
        datePickerDialog.show()
    }

    private fun showTimeDialog(view: View) {
        val c2: Calendar = Calendar.getInstance()
        val mHour = c2.get(Calendar.HOUR_OF_DAY)
        val mMinute = c2.get(Calendar.MINUTE)


        val timePickerDialog = TimePickerDialog(
            requireActivity(), { _, hourOfDay, minute ->

                val time = "$hourOfDay:$minute"
                if (view.id == createRuleBinding.startTimeEditText.id) {
                    createRuleBinding.startTimeEditText.setText(time)
                } else {
                    createRuleBinding.endTimeEditText.setText(time)
                }

            }, mHour!!, mMinute!!, true
        )
        timePickerDialog.show()
    }
}