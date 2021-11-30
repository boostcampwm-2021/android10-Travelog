package com.thequietz.travelog.guide.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.thequietz.travelog.databinding.FragmentOtherInfoBinding
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.adapter.OtherInfoAdapter
import com.thequietz.travelog.guide.viewmodel.OtherInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class OtherInfoFragment : Fragment() {
    private var _binding: FragmentOtherInfoBinding? = null
    private val binding get() = _binding!!
    private val otherInfoViewModel by viewModels<OtherInfoViewModel>()
    private val args: OtherInfoFragmentArgs by navArgs()
    private val vacationAdapter by lazy { OtherInfoAdapter() }
    private val festivalAdapter by lazy { OtherInfoAdapter() }
    private val foodAdapter by lazy { OtherInfoAdapter() }
    private lateinit var thisPlace: Place

    var vacationPrevItemCount = 0
    var foodPrevItemCount = 0
    var festivalPrevItemCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisPlace = Gson().fromJson(args.item, Place::class.java)

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = otherInfoViewModel
            rvVacationSpot.adapter = vacationAdapter
            rvFood.adapter = foodAdapter
            rvFestival.adapter = festivalAdapter
        }

        with(otherInfoViewModel) {
            initPlace(thisPlace)
            vacationSpotList.observe(viewLifecycleOwner, { it ->
                it?.let { vacationAdapter.submitList(it) }
            })
            foodList.observe(viewLifecycleOwner, { it ->
                it?.let { foodAdapter.submitList(it) }
            })
            festivalList.observe(viewLifecycleOwner, { it ->
                it?.let { festivalAdapter.submitList(it) }
            })
        }
        setListener()
    }

    private fun setListener() {
        with(binding) {
            rvVacationSpot.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    with(binding.rvVacationSpot) {
                        val visibleItemCount = childCount
                        val totalItemCount = layoutManager?.itemCount!!
                        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        if (totalItemCount <= (firstVisibleItem + visibleItemCount) && (visibleItemCount - vacationPrevItemCount == 1)) {
                            otherInfoViewModel.addVacationData()
                        }
                        vacationPrevItemCount = visibleItemCount
                    }
                    if (!binding.rvVacationSpot.canScrollHorizontally(1)) {
                        if (otherInfoViewModel.vacationPageEnd.value == true) {
                            Toast.makeText(requireContext(), "마지막 페이지입니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
            rvFood.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    with(binding.rvFood) {
                        val visibleItemCount = childCount
                        val totalItemCount = layoutManager?.itemCount!!
                        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        if (totalItemCount <= (firstVisibleItem + visibleItemCount) && (visibleItemCount - foodPrevItemCount == 1)) {
                            otherInfoViewModel.addFoodData()
                        }
                        foodPrevItemCount = visibleItemCount
                    }
                    if (!binding.rvFood.canScrollHorizontally(1)) {
                        if (otherInfoViewModel.foodPageEnd.value == true) {
                            Toast.makeText(requireContext(), "마지막 페이지입니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
            rvFestival.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    with(binding.rvFestival) {
                        val visibleItemCount = childCount
                        val totalItemCount = layoutManager?.itemCount!!
                        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        if (totalItemCount <= (firstVisibleItem + visibleItemCount) && (visibleItemCount - festivalPrevItemCount == 1)) {
                            otherInfoViewModel.addFestivalData()
                        }
                        festivalPrevItemCount = visibleItemCount
                    }
                    if (!binding.rvFestival.canScrollHorizontally(1)) {
                        if (otherInfoViewModel.festivalPageEnd.value == true) {
                            Toast.makeText(requireContext(), "마지막 페이지입니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
            ivNoVation.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        otherInfoViewModel.vacationAgain()
                    }
                    if (otherInfoViewModel.vacationSpotList.value?.size == 0) {
                        Toast.makeText(requireContext(), "해당 데이터가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            ivNoFood.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        otherInfoViewModel.foodAgain()
                    }
                    if (otherInfoViewModel.foodList.value?.size == 0) {
                        Toast.makeText(requireContext(), "해당 데이터가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            ivNoFestival.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        otherInfoViewModel.festivalAgain()
                    }
                    if (otherInfoViewModel.festivalList.value?.size == 0) {
                        Toast.makeText(requireContext(), "해당 데이터가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            slLayout.setOnRefreshListener {
                otherInfoViewModel.initPlace(thisPlace)
                slLayout.isRefreshing = false
            }
            btnMakePlan.setOnClickListener {
                val action = OtherInfoFragmentDirections
                    .actionOtherInfoFragmentToSchedulePlaceFragmentFromGuide()
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
