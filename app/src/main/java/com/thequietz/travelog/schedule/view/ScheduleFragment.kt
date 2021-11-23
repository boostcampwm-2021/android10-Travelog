package com.thequietz.travelog.schedule.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentScheduleBinding
import com.thequietz.travelog.schedule.adapter.ScheduleRecyclerAdapter
import com.thequietz.travelog.schedule.viewmodel.ScheduleViewModel
import com.thequietz.travelog.util.ScheduleControlType
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
        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            val action = ScheduleFragmentDirections.actionScheduleFragmentToSchedulePlaceFragment()
            it.findNavController().navigate(action)
        }
    }

    private fun initRecyclerView() {
        val adapter = ScheduleRecyclerAdapter(
            {
                // TODO: 세부 일정 설정 화면 Navigation 연결
                val action =
                    ScheduleFragmentDirections.actionScheduleFragmentToScheduleDetailFragment(
                        it,
                        ScheduleControlType.TYPE_UPDATE
                    )
                findNavController().navigate(action)
            },
            {
                Log.d("Loaded Data", it.toString())
                viewModel.deleteSchedule(it)
            }
        )
        binding.rvSchedule.adapter = adapter
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: ScheduleRecyclerAdapter) {
        viewModel.schedules.observe(viewLifecycleOwner) { schedules ->
            if (schedules.isNullOrEmpty())
                binding.tvNoSchedule.visibility = View.VISIBLE
            else
                binding.tvNoSchedule.visibility = View.INVISIBLE
            adapter.submitList(schedules)
        }
    }
}
