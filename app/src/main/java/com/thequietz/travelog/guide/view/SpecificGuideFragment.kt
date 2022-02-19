package com.thequietz.travelog.guide.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.thequietz.travelog.R
import com.thequietz.travelog.common.BaseFragment
import com.thequietz.travelog.databinding.FragmentSpecificGuideBinding
import com.thequietz.travelog.guide.adapter.AllPlaceAdapter
import com.thequietz.travelog.guide.viewmodel.SpecificGuideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecificGuideFragment : BaseFragment<FragmentSpecificGuideBinding>(R.layout.fragment_specific_guide) {
    private val specificGuideViewModel by viewModels<SpecificGuideViewModel>()
    private val args: SpecificGuideFragmentArgs by navArgs()
    private val adapter by lazy { AllPlaceAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            viewModel = specificGuideViewModel
            rvSpecificPlace.adapter = adapter
        }
        with(specificGuideViewModel) {
            currentPlaceList.observe(viewLifecycleOwner, { it ->
                it?.let { adapter.submitList(it) }
            })
            initCurrentItem(args, this@SpecificGuideFragment)
        }

        findNavController().apply {
            currentBackStackEntry?.savedStateHandle
                ?.getLiveData<Boolean>("toSchedulePlace")?.observe(
                    viewLifecycleOwner,
                    {
                        previousBackStackEntry?.savedStateHandle?.set("toSchedulePlace", it)
                        popBackStack()
                    }
                )
        }
    }
}
