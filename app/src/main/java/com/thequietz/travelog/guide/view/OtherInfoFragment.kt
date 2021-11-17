package com.thequietz.travelog.guide.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            vacationSpotList4.observe(viewLifecycleOwner, { it ->
                it?.let { vacationAdapter.submitList(it) }
            })
            foodList4.observe(viewLifecycleOwner, { it ->
                it?.let { foodAdapter.submitList(it) }
            })
            festivalList4.observe(viewLifecycleOwner, { it ->
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
            tvVacationSpotMore.setOnClickListener {
                viewModel!!.vacationSpotList.observe(viewLifecycleOwner, { it ->
                    it?.let { vacationAdapter.submitList(it.get(otherInfoViewModel.currentInd.value!!)) }
                })
            }
            tvFoodMore.setOnClickListener {
                viewModel!!.foodList.observe(viewLifecycleOwner, { it ->
                    it?.let { foodAdapter.submitList(it.get(otherInfoViewModel.currentInd.value!!)) }
                })
            }
            tvFestivalMore.setOnClickListener {
                viewModel!!.festivalList.observe(viewLifecycleOwner, { it ->
                    it?.let { festivalAdapter.submitList(it.get(otherInfoViewModel.currentInd.value!!)) }
                })
            }
            tbOtherInfo.setNavigationOnClickListener {
                val action = OtherInfoFragmentDirections
                    .actionOtherInfoFragmentToSpecificGuideFragment(SpecificGuideViewModel.previousSearchCode)
                findNavController().navigate(action)
            }
        }
    }
}
