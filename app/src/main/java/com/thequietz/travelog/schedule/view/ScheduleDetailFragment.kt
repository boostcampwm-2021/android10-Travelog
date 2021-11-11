package com.thequietz.travelog.schedule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentScheduleDetailBinding
import com.thequietz.travelog.schedule.adapter.ScheduleDetailAdapter
import com.thequietz.travelog.schedule.viewmodel.ScheduleDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleDetailFragment : Fragment() {
    lateinit var binding: FragmentScheduleDetailBinding
    private val layoutId = R.layout.fragment_schedule_detail
    private val viewModel by viewModels<ScheduleDetailViewModel>()
    lateinit var adapter: ScheduleDetailAdapter
    private val args: ScheduleDetailFragmentArgs by navArgs()

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
        initRecycler()
        initItemObserver()
    }

    private fun initRecycler() {
        viewModel.initItemList(args.startDate, args.endDate)
        adapter = ScheduleDetailAdapter(viewModel)
        binding.rvSchedule.adapter = adapter
        viewModel.itemList.value?.let { adapter.item = it }
        adapter.notifyDataSetChanged()
    }

    private fun initItemObserver() {
        viewModel.itemList.observe(viewLifecycleOwner, {
            adapter.notifyDataSetChanged()
        })
    }
}
