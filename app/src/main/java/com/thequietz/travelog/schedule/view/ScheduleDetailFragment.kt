package com.thequietz.travelog.schedule.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.gms.maps.model.LatLng
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentScheduleDetailBinding
import com.thequietz.travelog.map.GoogleMapFragment
import com.thequietz.travelog.schedule.adapter.ScheduleDetailAdapter
import com.thequietz.travelog.schedule.adapter.ScheduleTouchHelperCallback
import com.thequietz.travelog.schedule.viewmodel.ScheduleDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleDetailFragment :
    GoogleMapFragment<FragmentScheduleDetailBinding, ScheduleDetailViewModel>() {
    override val layoutId = R.layout.fragment_schedule_detail
    override val viewModel by viewModels<ScheduleDetailViewModel>()
    lateinit var adapter: ScheduleDetailAdapter
    private val args: ScheduleDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            changeMarkerOrder(3, 5, true)
        }

        binding.btnNext.setOnClickListener {
            deleteMarker(0)
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
        initRecycler()
        initItemObserver()
    }

    private fun initRecycler() {
        val startDate = args.schedule.date.split("~")[0]
        val endDate = args.schedule.date.split("~")[1]
        viewModel.initItemList(startDate, endDate)
        adapter = ScheduleDetailAdapter(viewModel)
        val callback = ScheduleTouchHelperCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvSchedule)
        binding.rvSchedule.adapter = adapter
        viewModel.itemList.value?.let { adapter.item = it }
        adapter.notifyDataSetChanged()
    }

    private fun initItemObserver() {
        viewModel.itemList.observe(viewLifecycleOwner, {
            adapter.notifyDataSetChanged()
        })
    }

    override fun initTargetList() {
        targetList = args.schedule.place.map { LatLng(it.mapY.toDouble(), it.mapX.toDouble()) }
            .toMutableList()
    }

    override fun addMapComponents() {
        createMarker(*targetList.toTypedArray(), isNumbered = true)
    }
}
