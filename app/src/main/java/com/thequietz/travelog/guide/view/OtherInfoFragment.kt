package com.thequietz.travelog.guide.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentOtherInfoBinding
import com.thequietz.travelog.guide.adapter.OtherInfoAdapter
import com.thequietz.travelog.guide.adapter.OtherInfoViewPagerAdapter
import com.thequietz.travelog.guide.viewmodel.OtherInfoViewModel
import com.thequietz.travelog.guide.viewmodel.SpecificGuideViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class OtherInfoFragment : Fragment() {
    private lateinit var _binding: FragmentOtherInfoBinding
    private val binding get() = _binding
    private val otherInfoViewModel by viewModels<OtherInfoViewModel>()
    private val args: OtherInfoFragmentArgs by navArgs()
    private val viewPagerAdapter by lazy { OtherInfoViewPagerAdapter() }
    private val vacationAdapter by lazy { OtherInfoAdapter() }
    private val festivalAdapter by lazy { OtherInfoAdapter() }
    private val foodAdapter by lazy { OtherInfoAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOtherInfoBinding.inflate(inflater, container, false)
        initToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = otherInfoViewModel
            rvVacationSpot.adapter = vacationAdapter
            rvFood.adapter = foodAdapter
            rvFestival.adapter = festivalAdapter
            vpOtherInfo.adapter = viewPagerAdapter
            vpOtherInfo.setCurrentItem(0, false)
            ciOtherInto.attachToRecyclerView(binding.vpOtherInfo.getChildAt(0) as RecyclerView)
        }

        with(otherInfoViewModel) {
            initPlaceList(args.item)
            placeList.observe(viewLifecycleOwner, { it ->
                it?.let { viewPagerAdapter.submitList(it) }
            })
            currentVacationSpotList.observe(viewLifecycleOwner, { it ->
                it?.let { vacationAdapter.submitList(it) }
            })
            currentFoodList.observe(viewLifecycleOwner, { it ->
                it?.let { foodAdapter.submitList(it) }
            })
            currentFestivalList.observe(viewLifecycleOwner, { it ->
                it?.let { festivalAdapter.submitList(it) }
            })
        }
        setListener()
    }

    private fun initToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.tbOtherInfo)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.img_leftarrow)
        }
    }

    private fun setListener() {
        with(binding) {
            vpOtherInfo.registerOnPageChangeCallback(object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        otherInfoViewModel.setCurrentInd(position)
                    }
                }
            )
            tbOtherInfo.setNavigationOnClickListener {
                val action = OtherInfoFragmentDirections
                    .actionOtherInfoFragmentToSpecificGuideFragment(SpecificGuideViewModel.previousSearch)
                findNavController().navigate(action)
            }
            rvVacationSpot.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!binding.rvVacationSpot.canScrollHorizontally(1)) {
                        otherInfoViewModel.addVacationData()
                        if (otherInfoViewModel.vacationPageEnd.value == true) {
                            Toast.makeText(requireContext(), "마지막 페이지입니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
            rvFood.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!binding.rvFood.canScrollHorizontally(1)) {
                        otherInfoViewModel.addFoodData()
                        if (otherInfoViewModel.foodPageEnd.value == true) {
                            Toast.makeText(requireContext(), "마지막 페이지입니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
            rvFestival.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!binding.rvFestival.canScrollHorizontally(1)) {
                        otherInfoViewModel.addFestivalData()
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
                    if (otherInfoViewModel.currentVacationSpotList.value?.size == 0) {
                        Toast.makeText(requireContext(), "해당 데이터가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            ivNoFood.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        otherInfoViewModel.foodAgain()
                    }
                    if (otherInfoViewModel.currentFoodList.value?.size == 0) {
                        Toast.makeText(requireContext(), "해당 데이터가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            ivNoFestival.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        otherInfoViewModel.festivalAgain()
                    }
                    if (otherInfoViewModel.currentFestivalList.value?.size == 0) {
                        Toast.makeText(requireContext(), "해당 데이터가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            slLayout.setOnRefreshListener {
                otherInfoViewModel.initPlaceList(args.item)
                slLayout.isRefreshing = false
            }
        }
    }
}
