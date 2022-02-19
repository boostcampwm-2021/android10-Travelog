package com.thequietz.travelog.schedule.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.thequietz.travelog.R
import com.thequietz.travelog.common.BaseFragment
import com.thequietz.travelog.databinding.FragmentScheduleBinding
import com.thequietz.travelog.schedule.adapter.ScheduleRecyclerAdapter
import com.thequietz.travelog.schedule.viewmodel.ScheduleViewModel
import com.thequietz.travelog.util.ScheduleControlType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(R.layout.fragment_schedule) {
    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        initRecyclerView()

        return view
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
                val action =
                    ScheduleFragmentDirections.actionScheduleFragmentToScheduleDetailFragment(
                        it.schedulePlace.toTypedArray(),
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
