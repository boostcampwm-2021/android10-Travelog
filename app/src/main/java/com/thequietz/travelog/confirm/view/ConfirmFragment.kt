package com.thequietz.travelog.confirm.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.SupportMapFragment
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
    override var isMarkerNumbered = true
    override var drawOrderedPolyline = false

    private var mapFragment: SupportMapFragment? = null

    override fun onMapReady(googleMap: GoogleMap) {
        super.onMapReady(googleMap)

        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment = mapFragment?.also {
            val mapOptions = GoogleMapOptions().useViewLifecycleInFragment(true)
            SupportMapFragment.newInstance(mapOptions)
        }
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

        setToolbar()

        _context = requireContext()
        dayAdapter = ConfirmDayAdapter(object : ConfirmDayAdapter.OnClickListener {
            override fun onClick(index: Int) {
                viewModel.updateSchedule("Day ${String.format("%02d", index + 1)}")
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
                    if (viewModel.currentSchedule.value.isNullOrEmpty()) {
                        targetList.value = mutableListOf()
                        return
                    }

                    val coordinates = viewModel.currentSchedule.value?.map { e ->
                        val loc = e.destination.geometry.location
                        LatLng(loc.latitude, loc.longitude)
                    }?.toMutableList() ?: mutableListOf()

                    targetList.value = coordinates
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
            if (it.isNullOrEmpty()) {
                val (_, _, mapX, mapY) = navArgs.defaultPlace
                targetList.value = mutableListOf(LatLng(mapY, mapX))
                binding.llConfirmNoItems.visibility = View.VISIBLE
                binding.tvConfirmNoItems.visibility = View.VISIBLE
                binding.vpConfirmPlace.visibility = View.GONE
                return@observe
            }
            binding.llConfirmNoItems.visibility = View.GONE
            binding.tvConfirmNoItems.visibility = View.GONE
            binding.vpConfirmPlace.visibility = View.VISIBLE

            pageAdapter.submitList(it)
            binding.vpConfirmPlace.let { pager ->
                pager.post {
                    pager.setCurrentItem(0, true)
                }
            }

            val coordinates = viewModel.currentSchedule.value?.map { e ->
                val loc = e.destination.geometry.location
                LatLng(loc.latitude, loc.longitude)
            }?.toMutableList() ?: mutableListOf()

            targetList.value = coordinates
        })

        viewModel.getSchedulesByNavArgs(navArgs.schedule, navArgs.scheduleDetails)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            title = "일정 확인"
            inflateMenu(R.menu.menu_with_close)
            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_close) {
                    val action =
                        ConfirmFragmentDirections.actionConfirmFragmentToScheduleFragment()
                    findNavController().navigate(action)
                    return@setOnMenuItemClickListener true
                }
                return@setOnMenuItemClickListener false
            }
        }
    }

    override fun initTargetList() {
        baseTargetList = navArgs.scheduleDetails.map { it ->
            val location = it.destination.geometry.location
            LatLng(location.latitude, location.latitude)
        }.toMutableList()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        map.clear()
        mapFragment?.onDestroyView()
    }
}
