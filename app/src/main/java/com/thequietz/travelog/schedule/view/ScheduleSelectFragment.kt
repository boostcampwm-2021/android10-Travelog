package com.thequietz.travelog.schedule.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentScheduleSelectBinding
import com.thequietz.travelog.onThrottleClick
import com.thequietz.travelog.schedule.model.ScheduleModel
import com.thequietz.travelog.schedule.viewmodel.ScheduleSelectViewModel
import com.thequietz.travelog.util.ScheduleControlType
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class ScheduleSelectFragment : Fragment() {
    private var _binding: FragmentScheduleSelectBinding? = null
    private val binding get() = _binding!!

    private val scheduleSelectViewModel by viewModels<ScheduleSelectViewModel>()
    private val args: ScheduleSelectFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_select, container, false)
        initEditText()
        initDatePicker()
        initHideKeyboard()
        initNextButton()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = scheduleSelectViewModel
        setToolbar()
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            val navController = findNavController()
            val appBarConfig = AppBarConfiguration.Builder(navController.graph).build()
            setupWithNavController(navController, appBarConfig)
            inflateMenu(R.menu.menu_with_complete)

            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_next) {
                    val keyword = scheduleSelectViewModel.travelName.value?.toString()
                    if (keyword == null || keyword.isEmpty()) {
                        return@setOnMenuItemClickListener false
                    }

                    val schedule = ScheduleModel(
                        name = keyword,
                        schedulePlace = args.placeList.toList(),
                        date = scheduleSelectViewModel.startDate.value.toString() + "~" + scheduleSelectViewModel.endDate.value.toString()
                    )

                    val action =
                        ScheduleSelectFragmentDirections.actionScheduleSelectFragmentToScheduleDetailFragment(
                            args.placeList,
                            schedule,
                            ScheduleControlType.TYPE_CREATE
                        )
                    findNavController().navigate(action)

                    return@setOnMenuItemClickListener true
                }
                return@setOnMenuItemClickListener false
            }
        }
    }

    private fun initNextButton() {
        scheduleSelectViewModel.btnEnable.observe(viewLifecycleOwner, {
            binding.toolbar.menu.findItem(R.id.action_next).isEnabled = it
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initHideKeyboard() {
        binding.layoutScheduleSelect.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }
    }

    private fun hideKeyboard() {
        if (activity != null && activity?.currentFocus != null) {
            val inputManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity?.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun initEditText() {
        binding.etTravelName.setOnKeyListener { _, keyCode, event ->
            if ((event?.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard()
                true
            } else false
        }
        binding.etTravelName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                scheduleSelectViewModel.setTravelName(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun initDatePicker() {
        val builder =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("여행 일정을 선택해 주세요")
                .setCalendarConstraints(
                    CalendarConstraints
                        .Builder()
                        .setStart(Calendar.getInstance().timeInMillis)
                        .build()
                )
        val picker = builder.build()
        var start: String
        var end: String

        binding.tvStartDateSelected.onThrottleClick {
            binding.tvEndDateSelected.isClickable = false
            picker.show(childFragmentManager, "date_picker")
            picker.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = selection?.first ?: 0
                start = SimpleDateFormat("yyyy.MM.dd").format(calendar.time).toString()
                calendar.timeInMillis = selection?.second ?: 0
                end = SimpleDateFormat("yyyy.MM.dd").format(calendar.time).toString()
                scheduleSelectViewModel.setScheduleRange(start, end)
                binding.tvEndDateSelected.isClickable = true
            }
            picker.addOnCancelListener {
                binding.tvEndDateSelected.isClickable = true
            }
            picker.addOnDismissListener {
                binding.tvEndDateSelected.isClickable = true
            }
        }

        binding.tvEndDateSelected.onThrottleClick {
            binding.tvStartDateSelected.isClickable = false
            picker.show(childFragmentManager, "date_picker")
            picker.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = selection?.first ?: 0
                start = SimpleDateFormat("yyyy.MM.dd").format(calendar.time).toString()
                calendar.timeInMillis = selection?.second ?: 0
                end = SimpleDateFormat("yyyy.MM.dd").format(calendar.time).toString()
                scheduleSelectViewModel.setScheduleRange(start, end)
            }
            picker.addOnCancelListener {
                binding.tvStartDateSelected.isClickable = true
            }
            picker.addOnDismissListener {
                binding.tvStartDateSelected.isClickable = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
