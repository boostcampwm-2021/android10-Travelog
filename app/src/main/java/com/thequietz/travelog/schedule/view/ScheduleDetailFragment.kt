package com.thequietz.travelog.schedule.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.gms.maps.model.LatLng
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentScheduleDetailBinding
import com.thequietz.travelog.map.GoogleMapFragment
import com.thequietz.travelog.place.model.PlaceDetailModel
import com.thequietz.travelog.schedule.adapter.ScheduleDetailAdapter
import com.thequietz.travelog.schedule.adapter.ScheduleTouchHelperCallback
import com.thequietz.travelog.schedule.viewmodel.ScheduleDetailViewModel
import com.thequietz.travelog.util.ScheduleControlType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleDetailFragment :
    GoogleMapFragment<FragmentScheduleDetailBinding, ScheduleDetailViewModel>() {
    override val layoutId = R.layout.fragment_schedule_detail
    override val viewModel by viewModels<ScheduleDetailViewModel>()
    lateinit var adapter: ScheduleDetailAdapter
    private val args: ScheduleDetailFragmentArgs by navArgs()

    override var drawMarker = true
    override var isMarkerNumbered = true
    override var drawOrderedPolyline = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.type == ScheduleControlType.TYPE_CREATE)
            args.scheduleModel.run { viewModel.createSchedule(name, schedulePlace, date) }
        else
            viewModel.loadSchedule(args.scheduleModel)

        setToolbar()

        val stateHandle = findNavController().currentBackStackEntry?.savedStateHandle

        stateHandle?.getLiveData<PlaceDetailModel>("result")?.observe(
            viewLifecycleOwner,
            {
                viewModel.addScheduleDetail(it)

                stateHandle.remove<PlaceDetailModel>("result")
            }
        )
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
        initRecycler()
        initItemObserver()
    }

    private fun setToolbar() {
        val navController = findNavController()
        val appBarConfig = AppBarConfiguration.Builder(navController.graph).build()

        binding.toolbar.apply {
            setupWithNavController(navController, appBarConfig)
            title = "일정 설정"
            inflateMenu(R.menu.menu_schedule_detail)
            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_next) {
                    val schedule = viewModel.schedule
                    val scheduleDetails = viewModel.detailList.value?.toTypedArray()
                        ?: return@setOnMenuItemClickListener false
                    viewModel.saveSchedule()

                    val action =
                        ScheduleDetailFragmentDirections.actionScheduleDetailFragmentToConfirmFragment(
                            schedule, scheduleDetails
                        )
                    navController.navigate(action)
                    return@setOnMenuItemClickListener true
                }
                return@setOnMenuItemClickListener false
            }
        }
    }

    private fun initRecycler() {
        val startDate = args.scheduleModel.date.split("~")[0]
        val endDate = args.scheduleModel.date.split("~")[1]
        viewModel.initItemList(startDate, endDate)
        adapter = ScheduleDetailAdapter(
            { addItem() },
            { viewModel.deleteSchedule(it) },
            { viewModel.moveItem(it) },
            { idx, date -> viewModel.changeSelected(idx, date) }
        )
        val callback = ScheduleTouchHelperCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvSchedule)
        binding.rvSchedule.adapter = adapter
    }

    private fun addItem() {
        val action =
            ScheduleDetailFragmentDirections.actionScheduleDetailFragmentToPlaceRecommendFragment(
                args.schedulePlaceArray
            )
        this.findNavController().navigate(action)
    }

    private fun initItemObserver() {
        viewModel.detailList.observe(viewLifecycleOwner, { list ->
            targetList.value = mutableListOf()
            val tempList = list.map {
                LatLng(
                    it.destination.geometry.location.latitude,
                    it.destination.geometry.location.longitude
                )
            }.toMutableList()
            targetList.value = tempList
        })

        viewModel.itemList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        viewModel.colorList.observe(viewLifecycleOwner, {
            markerColorList.value = it
        })

        viewModel.placeDetailList.observe(viewLifecycleOwner, {
            viewModel.placeDetailList.removeObservers(viewLifecycleOwner)
        })
    }

    override fun initTargetList() {
        if (isInitial)
            baseTargetList =
                args.scheduleModel.schedulePlace.map { LatLng(it.mapY.toDouble(), it.mapX.toDouble()) }
                    .toMutableList()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        baseTargetList.clear()
        binding.rvSchedule.adapter = null
    }
}
