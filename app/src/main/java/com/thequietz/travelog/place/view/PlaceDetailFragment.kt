package com.thequietz.travelog.place.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentPlaceDetailBinding
import com.thequietz.travelog.map.GoogleMapFragment
import com.thequietz.travelog.place.adapter.PlaceDetailAdapter
import com.thequietz.travelog.place.model.PlaceRecommendModel
import com.thequietz.travelog.place.model.PlaceSearchModel
import com.thequietz.travelog.place.viewmodel.PlaceDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceDetailFragment : GoogleMapFragment<FragmentPlaceDetailBinding, PlaceDetailViewModel>() {

    override val layoutId = R.layout.fragment_place_detail
    override val viewModel by viewModels<PlaceDetailViewModel>()
    private val navArgs: PlaceDetailFragmentArgs by navArgs()
    private val placeDetailViewModel: PlaceDetailViewModel by viewModels()

    private var isRecommended: Boolean = false
    private lateinit var adapter: PlaceDetailAdapter

    private var searchModel: PlaceSearchModel? = null
    private var recommendModel: PlaceRecommendModel? = null

    private lateinit var gson: Gson

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gson = Gson()
        isRecommended = navArgs.isRecommended

        when (isRecommended) {
            true -> recommendModel = gson.fromJson(navArgs.param, PlaceRecommendModel::class.java)
            false -> searchModel = gson.fromJson(navArgs.param, PlaceSearchModel::class.java)
        }

        adapter = PlaceDetailAdapter(isRecommended)

        val params = binding.ablPlaceDetail.layoutParams as CoordinatorLayout.LayoutParams
        val newBehavior = AppBarLayout.Behavior()
        newBehavior.setDragCallback(newCallback())
        params.behavior = newBehavior

        binding.viewModel = placeDetailViewModel
        binding.rvPlaceDetail.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner

        placeDetailViewModel.detail.observe(viewLifecycleOwner, {
            (binding.rvPlaceDetail.adapter as PlaceDetailAdapter).submitList(it.images)

            if (it.phoneNumber.trim().isEmpty()) {
                binding.tvPlaceDetailPhoneTitle.visibility = View.GONE
                binding.tvPlaceDetailPhoneNumber.visibility = View.GONE
            }

            if (it.operation.operation.isEmpty()) {
                binding.tvPlaceDetailOperation.visibility = View.GONE
            }

            if (it.overview == null || it.overview.trim().isEmpty()) {
                binding.tvPlaceDetailOverview.visibility = View.GONE
            }

            if (placeDetailViewModel.isLoaded.value == false) {
                val len = it.reviews?.size ?: 0
                val parentContext = requireContext()
                it.reviews?.forEachIndexed { idx, review ->
                    Log.d("IS_LOADED", placeDetailViewModel.isLoaded.value.toString())
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
                placeDetailViewModel.isLoaded.value = true
            }
        })

        if (!isRecommended) {
            placeDetailViewModel.loadDetailBySearch(searchModel?.placeId ?: "")
        } else {
            placeDetailViewModel.loadDetailByRecommend(
                recommendModel?.contentId ?: 0,
                recommendModel?.contentTypeId ?: 0
            )
        }
    }
}

private class newCallback : AppBarLayout.Behavior.DragCallback() {
    override fun canDrag(appBarLayout: AppBarLayout): Boolean {
        return false
    }
}
