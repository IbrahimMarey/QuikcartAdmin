package com.example.quikcartadmin.ui.coupons.updaterule.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.quikcartadmin.databinding.FragmentUpdateRulePriceBinding
import com.example.quikcartadmin.helpers.Constants
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.PriceRule
import com.example.quikcartadmin.models.entities.coupons.PriceRuleResponse
import com.example.quikcartadmin.ui.coupons.updaterule.viewmodel.UpdatePriceRuleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar


@AndroidEntryPoint
class UpdateRulePriceFragment : Fragment() {

    private lateinit var updateRulePriceBinding: FragmentUpdateRulePriceBinding

    private val args: UpdateRulePriceFragmentArgs by navArgs()
    private var priceRule: PriceRule? = null
    private var startDate : String? = null
    private var endDate : String? = null
    private var startTime : String? = null
    private var endTime : String? = null
    private var valueType = ""

    private val alertDialog: AlertDialog by lazy {
        Constants.createAlertDialog(requireContext(), "")
    }

    private val viewModel : UpdatePriceRuleViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        updateRulePriceBinding = FragmentUpdateRulePriceBinding.inflate(inflater, container, false)
        return updateRulePriceBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        priceRule = args.priceRule

        bindRuleData()
        editActionBtn()
        observeUpdateRule()
        addDatePricker()
        addTimePicker()
        setupMenu()
        handleSelectedTypeValue()
    }

    private fun setupMenu() {
        val valueTypeList =
            listOf("percentage", "fixed_amount")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, valueTypeList)

        updateRulePriceBinding.valueTypeEditText.threshold = 1
        updateRulePriceBinding.valueTypeEditText.setAdapter(adapter)
        updateRulePriceBinding.valueTypeEditText.setTextColor(Color.BLACK)
    }

    private fun handleSelectedTypeValue() {
        updateRulePriceBinding.valueTypeEditText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                valueType = parent.getItemAtPosition(position).toString()
            }
    }
    private fun editActionBtn() {
        updateRulePriceBinding.addBtn.setOnClickListener {
            if (isDataValidate()) {
                handleEditAction()
            }
        }
    }

    private fun handleEditAction() {
        AlertDialog.Builder(requireContext())
            .setTitle("Edit Rule")
            .setMessage("Are you sure you want to save edit?")
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                buildRule()
                viewModel.updatePriceRule(priceRule?.id!!, PriceRuleResponse(priceRule!!))
                alertDialog.show()
            }
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun observeUpdateRule() {
        lifecycleScope.launch {
            viewModel.updateRuleState.collectLatest {
                when (it) {
                    is UiState.Loading -> {
                    }
                    is UiState.Success -> {
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(), "Updated successfully", Toast.LENGTH_LONG)
                            .show()
                        findNavController().popBackStack()
                    }
                    is UiState.Failed -> {
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(), "Update failed", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun buildRule() {
        val value = (updateRulePriceBinding.valueEt.text.toString()).toDouble() * -1.0

        priceRule?.title = updateRulePriceBinding.titleEt.text.toString()
        priceRule?.value = value.toString()
        priceRule?.starts_at = startDate
        priceRule?.ends_at = endDate
        priceRule?.value_type = valueType
    }

    private fun bindRuleData() {
        val value = ((priceRule?.value?.substring(1))?.toDouble() ?: 1.0)

        if (priceRule != null) {
            updateRulePriceBinding.titleEt.setText(priceRule?.title)
            updateRulePriceBinding.valueEt.setText(value.toString())
            updateRulePriceBinding.startAtEditText.setText(priceRule?.starts_at?.substring(0,10))
            updateRulePriceBinding.endsAtEditText.setText(priceRule?.ends_at?.substring(0,10))
            updateRulePriceBinding.startTimeEditText.setText(priceRule?.starts_at?.substring(11,16))
            updateRulePriceBinding.endTimeEditText.setText(priceRule?.ends_at?.substring(11,16))
            updateRulePriceBinding.valueTypeEditText.setText(priceRule?.value_type)
            valueType = priceRule?.value_type!!
        }
    }


    private fun isDataValidate(): Boolean {
        if (updateRulePriceBinding.titleEt.text.toString().isNullOrEmpty()) {
            updateRulePriceBinding.titleEt.error = "should have a title"
            return false
        }
        if (updateRulePriceBinding.valueEt.text.toString().isNullOrEmpty()) {
            updateRulePriceBinding.titleEt.error = "should have a value"
            return false
        }
        if ((updateRulePriceBinding.valueEt.text.toString()).toDouble() > 100
            || (updateRulePriceBinding.valueEt.text.toString()).toDouble() < 1
        ) {
            updateRulePriceBinding.valueEt.error = "discount value should be between 1 and 100"
            return false
        }
        if (updateRulePriceBinding.valueTypeEditText.text.toString().isNullOrEmpty()) {
            updateRulePriceBinding.valueTypeEditText.error = "should have a value type"
            return false
        }
        if(!validateValue()){
            return false
        }
        if (updateRulePriceBinding.startAtEditText.text.toString().isNullOrEmpty()) {
            updateRulePriceBinding.startAtEditText.error = "should have a start date"
            return false
        }
        if (updateRulePriceBinding.startTimeEditText.text.toString().isNullOrEmpty()) {
            updateRulePriceBinding.startTimeEditText.error = "should have a start time"
            return false
        }
        if (!updateRulePriceBinding.endsAtEditText.text.toString().isNullOrEmpty()) {
            if (updateRulePriceBinding.endTimeEditText.text.toString().isNullOrEmpty()) {
                updateRulePriceBinding.endTimeEditText.error = "should have a end time"
                return false
            }
        }
        if (!updateRulePriceBinding.endTimeEditText.text.toString().isNullOrEmpty()) {
            if (updateRulePriceBinding.endsAtEditText.text.toString().isNullOrEmpty()) {
                updateRulePriceBinding.endsAtEditText.error = "should have a end date"
                return false
            }
        }
        if(!validateDate()){
            Toast.makeText(requireContext(),"End date before start date",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun validateDate() : Boolean{
        startDate = updateRulePriceBinding.startAtEditText.text.toString() + " " + updateRulePriceBinding.startTimeEditText.text
        endDate = updateRulePriceBinding.endsAtEditText.text.toString() + " " + updateRulePriceBinding.endTimeEditText.text

        if(!updateRulePriceBinding.endsAtEditText.text.toString().isNullOrEmpty())
            return Constants.buildDate(startDate!!)!!.before(Constants.buildDate(endDate!!))

        return true
    }

    private fun validateValue() : Boolean{
        if(valueType == "percentage") {
            if ((updateRulePriceBinding.valueEt.text.toString()).toDouble() > 100
                || (updateRulePriceBinding.valueEt.text.toString()).toDouble() < 1
            ) {
                updateRulePriceBinding.valueEt.error = "discount value should be between 1 and 100"
                return false
            }
        }else{
            if ((updateRulePriceBinding.valueEt.text.toString()).toDouble() == 0.0){
                updateRulePriceBinding.valueEt.error = "discount value should not be  0"
                return false
            }
        }
        return true
    }


    private fun addDatePricker() {
        updateRulePriceBinding.startAtEditText.setOnClickListener {
            showDatePicker(it)
        }
        updateRulePriceBinding.endsAtEditText.setOnClickListener {
            showDatePicker(it)
        }
    }

    private fun addTimePicker() {
        updateRulePriceBinding.startTimeEditText.setOnClickListener {
            showTimeDialog(it)
        }
        updateRulePriceBinding.endTimeEditText.setOnClickListener {
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
                if (view.id == updateRulePriceBinding.startAtEditText.id) {
                    updateRulePriceBinding.startAtEditText.setText(strDate)
                } else {
                    updateRulePriceBinding.endsAtEditText.setText(strDate)
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

                //  val time = get12HourFormat(hourOfDay, minute)
                val time = "$hourOfDay:$minute"
                if (view.id == updateRulePriceBinding.startTimeEditText.id) {
                    updateRulePriceBinding.startTimeEditText.setText(time)
                } else {
                    updateRulePriceBinding.endTimeEditText.setText(time)
                }

            }, mHour!!, mMinute!!, false
        )
        timePickerDialog.show()
    }

}