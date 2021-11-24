package com.thequietz.travelog.confirm.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.thequietz.travelog.R
import com.thequietz.travelog.confirm.adapter.ConfirmDayAdapter
import com.thequietz.travelog.confirm.adapter.ConfirmPagerAdapter
import com.thequietz.travelog.confirm.viewmodel.ConfirmViewModel
import com.thequietz.travelog.databinding.FragmentConfirmBinding
import com.thequietz.travelog.map.GoogleMapFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmFragment : GoogleMapFragment<FragmentConfirmBinding, ConfirmViewModel>() {

    override val layoutId = R.layout.fragment_confirm
    override val viewModel: ConfirmViewModel by viewModels()
    override var drawMarker = true
    override var isMarkerNumbered = false
    override var drawOrderedPolyline = false

    override fun onMapReady(googleMap: GoogleMap) {
        super.onMapReady(googleMap)

        googleMap.setMinZoomPreference(15F)
    }

    private lateinit var _context: Context
    private lateinit var dayAdapter: ConfirmDayAdapter
    private lateinit var pageAdapter: ConfirmPagerAdapter

    private val navArgs: ConfirmFragmentArgs by navArgs()

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _context = requireContext()
        dayAdapter = ConfirmDayAdapter(object : ConfirmDayAdapter.OnClickListener {
            override fun onClick(index: Int) {
                viewModel.updateSchedule("Day ${index + 1}")
            }
        })
        pageAdapter = ConfirmPagerAdapter()

        binding.rvConfirmHeader.adapter = dayAdapter
        binding.vpConfirmPlace.adapter = pageAdapter
        binding.vpConfirmPlace.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.vpConfirmPlace.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    val currentItem = viewModel.currentSchedule.value?.get(position)
                    val location = currentItem?.destination?.geometry?.location
                    val lat = location?.latitude
                    val lng = location?.longitude

                    if (lat != null && lng != null) {
                        targetList.value = mutableListOf(LatLng(lat, lng))
                    }
                }
            })

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.schedules.observe(viewLifecycleOwner, {
            val keys = it.keys.toList()

            dayAdapter.submitList(keys)

            if (keys.isEmpty()) return@observe
            viewModel.updateSchedule(keys[0])
        })

        viewModel.currentSchedule.observe(viewLifecycleOwner, {
            pageAdapter.submitList(it)
            binding.vpConfirmPlace.let { pager ->
                pager.post {
                    pager.setCurrentItem(0, true)
                }
            }

            val currentItem = it.firstOrNull()
            val location = currentItem?.destination?.geometry?.location
            val lat = location?.latitude
            val lng = location?.longitude

            if (lat != null && lng != null) {
                targetList.value = mutableListOf(LatLng(lat, lng))
            }
        })

        viewModel.getSchedulesByNavArgs(navArgs.schedules)

        binding.btnConfirm.setOnClickListener {
            val action =
                ConfirmFragmentDirections.actionConfirmFragmentToScheduleFragment()
            findNavController().navigate(action)
        }
    }

    override fun initTargetList() {
        baseTargetList = navArgs.schedules.map { it ->
            val location = it.destination.geometry.location
            LatLng(location.latitude, location.latitude)
        }.toMutableList()
    }
}
