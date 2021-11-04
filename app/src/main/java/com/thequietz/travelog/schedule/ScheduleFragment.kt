package com.thequietz.travelog.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentScheduleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)
        val adapter = ScheduleRecyclerAdapter()
        binding.rvSchedule.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            // TODO: 여행 지역 선택화면으로 연결
        }
    }

    private fun subscribeUi(adapter: ScheduleRecyclerAdapter) {
        viewModel.schedules.observe(viewLifecycleOwner) { schedules ->
            if (schedules.isEmpty())
                binding.tvNoSchedule.visibility = View.VISIBLE
            else {
                binding.tvNoSchedule.visibility = View.INVISIBLE
                adapter.submitList(schedules)
            }
        }
    }
}
