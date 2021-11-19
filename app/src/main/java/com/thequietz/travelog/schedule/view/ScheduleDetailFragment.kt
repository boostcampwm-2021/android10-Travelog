package com.thequietz.travelog.schedule.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.gms.maps.model.LatLng
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentScheduleDetailBinding
import com.thequietz.travelog.map.GoogleMapFragment
import com.thequietz.travelog.place.model.PlaceDetailModel
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

        binding.btnNext.setOnClickListener {
            args.schedule.apply {
                viewModel.createSchedule(name, place, date)
                val action =
                    ScheduleDetailFragmentDirections.actionScheduleDetailFragmentToScheduleFragment()
                findNavController().navigate(action)
            }
        }

        val stateHandle = findNavController().currentBackStackEntry?.savedStateHandle

        stateHandle?.getLiveData<PlaceDetailModel>("result")?.observe(
            viewLifecycleOwner,
            {
                viewModel.addSchedule(it)

                val newTarget =
                    LatLng(it.geometry.location.latitude, it.geometry.location.longitude)

                if (viewModel.placeDetailList.value?.size ?: 0 < 2)
                    targetList.value = mutableListOf(newTarget)
                else
                    targetList.value = targetList.value.apply {
                        this?.add(newTarget)
                    }

                createMarker(newTarget, isNumbered = true)
                markerList.forEachIndexed { index, marker ->
                    if (index + 1 < markerList.size)
                        createPolyline(marker, markerList[index + 1])
                }
                stateHandle.remove<PlaceDetailModel>("result")
            }
        )
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
        adapter = ScheduleDetailAdapter(viewModel) {
            val action =
                ScheduleDetailFragmentDirections.actionScheduleDetailFragmentToPlaceRecommendFragment()
            this.findNavController().navigate(action)
        }
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
        if (isInitial)
            targetList.value =
                args.schedule.place.map { LatLng(it.mapY.toDouble(), it.mapX.toDouble()) }
                    .toMutableList()
    }
}
