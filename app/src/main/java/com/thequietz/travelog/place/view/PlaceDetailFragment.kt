package com.thequietz.travelog.place.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentPlaceDetailBinding
import com.thequietz.travelog.map.GoogleMapFragment
import com.thequietz.travelog.place.adapter.PlaceDetailAdapter
import com.thequietz.travelog.place.model.PlaceDetailLocation
import com.thequietz.travelog.place.model.PlaceRecommendModel
import com.thequietz.travelog.place.model.PlaceSearchModel
import com.thequietz.travelog.place.viewmodel.PlaceDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceDetailFragment : GoogleMapFragment<FragmentPlaceDetailBinding, PlaceDetailViewModel>() {

    override val layoutId = R.layout.fragment_place_detail
    override val viewModel by viewModels<PlaceDetailViewModel>()
    override var drawMarker = true
    override var isMarkerNumbered = false
    override var drawOrderedPolyline = false

    private val navArgs: PlaceDetailFragmentArgs by navArgs()

    private var isRecommended: Boolean = false
    private var mapFragment: SupportMapFragment? = null
    private lateinit var adapter: PlaceDetailAdapter

    private lateinit var searchModel: PlaceSearchModel
    private lateinit var recommendModel: PlaceRecommendModel

    private lateinit var gson: Gson

    override fun onMapReady(googleMap: GoogleMap) {
        super.onMapReady(googleMap)

        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment = mapFragment?.also {
            val mapOptions = GoogleMapOptions().useViewLifecycleInFragment(true)
            SupportMapFragment.newInstance(mapOptions)
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun initTargetList() {
        gson = Gson()
        isRecommended = navArgs.isRecommended

        when (isRecommended) {
            true -> {
                recommendModel = gson.fromJson(navArgs.param, PlaceRecommendModel::class.java)
                targetList.value =
                    mutableListOf(LatLng(recommendModel.latitude, recommendModel.longitude))
            }
            false -> {
                searchModel =
                    gson.fromJson(navArgs.param, PlaceSearchModel::class.java) as PlaceSearchModel
                targetList.value = mutableListOf(
                    LatLng(
                        searchModel.geometry.location.latitude,
                        searchModel.geometry.location.longitude
                    )
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        zoomLevel = 15f

        gson = Gson()
        isRecommended = navArgs.isRecommended

        when (isRecommended) {
            true -> {
                recommendModel = gson.fromJson(navArgs.param, PlaceRecommendModel::class.java)
                Log.d("TAG", recommendModel.toString() + "$isRecommended")
            }
            false -> searchModel = gson.fromJson(navArgs.param, PlaceSearchModel::class.java)
        }

        if (navArgs.isGuide) {
            binding.btnPlaceAdd.visibility = View.GONE
        }

        adapter = PlaceDetailAdapter(isRecommended)

        val params = binding.ablPlaceDetail.layoutParams as CoordinatorLayout.LayoutParams
        val newBehavior = AppBarLayout.Behavior()
        newBehavior.setDragCallback(NewCallback())
        params.behavior = newBehavior
        binding.viewModel = viewModel

        binding.rvPlaceDetail.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner
        binding.tblPlaceDetail.setExpandedTitleColor(Color.parseColor(getString(R.string.color_transparent)))

        viewModel.detail.observe(viewLifecycleOwner, {
            (binding.rvPlaceDetail.adapter as PlaceDetailAdapter).submitList(it.images)

            if (it.phoneNumber.isNullOrEmpty()) {
                binding.tvPlaceDetailPhoneTitle.visibility = View.GONE
                binding.tvPlaceDetailPhoneNumber.visibility = View.GONE
            }

            if (it.operation == null || it.operation.operation.isNullOrEmpty()) {
                binding.tvPlaceDetailOperation.visibility = View.GONE
            }

            if (it.overview.isNullOrEmpty()) {
                binding.tvPlaceDetailOverview.visibility = View.GONE
            }

            if (viewModel.isLoaded.value == false) {
                val len = it.reviews?.size ?: 0
                val parentContext = requireContext()
                it.reviews?.forEachIndexed { idx, review ->
                    val reviewLayout = PlaceReviewLayout(
                        parentContext,
                        review
                    )
                    reviewLayout.id = idx * 31
                    binding.llPlaceDetail.addView(
                        reviewLayout
                    )
                    if (idx == len - 1) {
                        return@forEachIndexed
                    }
                    binding.llPlaceDetail.addView(
                        PlaceReviewDividerLayout(parentContext)
                    )
                }
                viewModel.isLoaded.value = true
            }
        })

        if (!isRecommended) {
            viewModel.loadDetailBySearch(searchModel.placeId ?: "")
        } else {
            viewModel.loadDetailByRecommend(
                recommendModel.contentId ?: 0,
                recommendModel.contentTypeId ?: 0
            )
        }

        binding.btnPlaceAdd.setOnClickListener {
            if (viewModel.detail.value != null) {
                findNavController().apply {
                    previousBackStackEntry?.savedStateHandle?.set(
                        "result",
                        viewModel.detail.value.apply {
                            this?.geometry?.location =
                                if (isRecommended)
                                    PlaceDetailLocation(
                                        recommendModel.latitude,
                                        recommendModel.longitude
                                    )
                                else
                                    PlaceDetailLocation(
                                        searchModel.geometry.location.latitude,
                                        searchModel.geometry.location.longitude
                                    )
                        }
                    )
                    popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        map.clear()
        mapFragment?.onDestroyView()
    }
}

private class NewCallback : AppBarLayout.Behavior.DragCallback() {
    override fun canDrag(appBarLayout: AppBarLayout): Boolean {
        return false
    }
}
