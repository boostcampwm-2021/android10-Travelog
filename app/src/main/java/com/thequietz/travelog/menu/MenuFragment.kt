package com.thequietz.travelog.menu

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.thequietz.travelog.R
import com.thequietz.travelog.common.BaseFragment
import com.thequietz.travelog.databinding.FragmentMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>(R.layout.fragment_menu) {
    private val viewModel by viewModels<MenuViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initSpinner()
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.schedule_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            binding.spinnerSchedule.adapter = adapter
            viewModel.scheduleAlarmTime.value?.let { binding.spinnerSchedule.setSelection(it) }
        }

        binding.spinnerSchedule.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long,
                ) {
                    viewModel.scheduleTimeChange(position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.record_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            binding.spinnerRecord.adapter = adapter
            viewModel.recordAlarmTime.value?.let { binding.spinnerRecord.setSelection(it, false) }
        }

        binding.spinnerRecord.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                viewModel.recordTimeChange(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        viewModel.recordAlarm.observe(this, {
            binding.spinnerRecord.isEnabled = it
        })

        viewModel.scheduleAlarm.observe(this, {
            binding.spinnerSchedule.isEnabled = it
        })

        viewModel.alarmPermission.observe(this, {
            if (it == false) {
                binding.spinnerSchedule.isEnabled = false
                binding.spinnerRecord.isEnabled = false
            } else {
                if (viewModel.recordAlarm.value == true) binding.spinnerRecord.isEnabled = true
                if (viewModel.scheduleAlarm.value == true) binding.spinnerSchedule.isEnabled = true
            }
        })
    }
}
