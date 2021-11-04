package com.thequietz.travelog.schedule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentScheduleBinding
import com.thequietz.travelog.schedule.ScheduleViewModel
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
            val action = ScheduleFragmentDirections.actionScheduleFragmentToSchedulePlaceFragment()
            it.findNavController().navigate(action)
        }
    }

    private fun subscribeUi(adapter: ScheduleRecyclerAdapter) {
        viewModel.schedules.observe(viewLifecycleOwner) { schedules ->
            adapter.submitList(schedules)
        }
    }
}
