package com.thequietz.travelog.menu

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentMenuBinding
import com.thequietz.travelog.menu.alarm.AlarmReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment() {
    lateinit var binding: FragmentMenuBinding
    private val layoutId = R.layout.fragment_menu
    private val viewModel by viewModels<MenuViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        initSpinner()
        initAlarm()
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.schedule_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSchedule.adapter = adapter
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
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerRecord.adapter = adapter
        }

        binding.spinnerRecord.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                viewModel.recordTimeChange(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    fun initAlarm() {
        val context = requireActivity().applicationContext
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            AlarmReceiver.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        binding.btnAlarm.setOnCheckedChangeListener(
            CompoundButton.OnCheckedChangeListener { _, isChecked ->
                val message = if (isChecked) {
                    val triggerTime = (SystemClock.elapsedRealtime() + 10 * 1000)
                    alarmManager.set(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                    "Alarm On"
                } else {
                    alarmManager.cancel(pendingIntent)
                    "Alarm off"
                }
                Log.e("Alarm", message)
            }
        )
    }
}
