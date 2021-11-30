package com.thequietz.travelog.guide.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.thequietz.travelog.databinding.FragmentSpecificGuideBinding
import com.thequietz.travelog.guide.adapter.AllPlaceAdapter
import com.thequietz.travelog.guide.viewmodel.SpecificGuideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecificGuideFragment : Fragment() {
    private var _binding: FragmentSpecificGuideBinding? = null
    private val binding get() = _binding!!
    private val specificGuideViewModel by viewModels<SpecificGuideViewModel>()
    private val args: SpecificGuideFragmentArgs by navArgs()
    private val adapter by lazy { AllPlaceAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecificGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = specificGuideViewModel
            rvSpecificPlace.adapter = adapter
        }
        with(specificGuideViewModel) {
            currentPlaceList.observe(viewLifecycleOwner, { it ->
                it?.let { adapter.submitList(it) }
            })
            initCurrentItem(args)
        }
    }
}
