package com.thequietz.travelog.place.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentPlaceDetailBinding
import com.thequietz.travelog.place.adapter.PlaceDetailAdapter
import com.thequietz.travelog.place.model.PlaceRecommendModel
import com.thequietz.travelog.place.model.PlaceSearchModel
import com.thequietz.travelog.place.viewmodel.PlaceDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceDetailFragment : Fragment() {

    private var _binding: FragmentPlaceDetailBinding? = null
    private val binding get() = _binding!!
    private val navArgs: PlaceDetailFragmentArgs by navArgs()
    private val placeDetailViewModel: PlaceDetailViewModel by viewModels()

    private var isRecommended: Boolean = false
    private lateinit var adapter: PlaceDetailAdapter

    private var searchModel: PlaceSearchModel? = null
    private var recommendModel: PlaceRecommendModel? = null

    private lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_place_detail, container, false)
        return binding.root
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

        binding.viewModel = placeDetailViewModel
        binding.rvPlaceDetail.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner

        placeDetailViewModel.detail.observe(viewLifecycleOwner, {
            (binding.rvPlaceDetail.adapter as PlaceDetailAdapter).submitList(it.images)

            if (it.phoneNumber.isEmpty()) {
                binding.tvPlaceDetailPhoneTitle.visibility = View.GONE
                binding.tvPlaceDetailPhoneNumber.visibility = View.GONE
            }

            if (it.operation.operation.isEmpty()) {
                binding.tvPlaceDetailOperation.visibility = View.GONE
            }

            if (it.overview == null || it.overview.isEmpty()) {
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
