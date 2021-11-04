package com.thequietz.travelog.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentOtherInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherInfoFragment : Fragment() {
    private var _binding: FragmentOtherInfoBinding? = null
    private val binding get() = _binding!!
    private val otherViewModel by viewModels<OtherViewModel>()
    private val args: OtherInfoFragmentArgs by navArgs()
    lateinit var vacationAdapter: OtherInfoAdapter
    lateinit var festivalAdapter: OtherInfoAdapter
    lateinit var foodAdapter: OtherInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_other_info, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vacationAdapter = OtherInfoAdapter()
        foodAdapter = OtherInfoAdapter()
        festivalAdapter = OtherInfoAdapter()

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = otherViewModel
            rvVacationSpot.adapter = vacationAdapter
            rvFood.adapter = foodAdapter
            rvFestival.adapter = festivalAdapter
        }

        with(otherViewModel) {
            initCurrenetItem(args.item)
            initVacationSpotData()
            initFoodData()
            initFestivalData()
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
    }
    private fun initToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.tbOtherInfo)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.img_leftarrow)
        }
    }
}
