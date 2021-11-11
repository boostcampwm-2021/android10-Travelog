package com.thequietz.travelog.guide.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.thequietz.travelog.FragmentType
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentSpecificGuideBinding
import com.thequietz.travelog.guide.adapter.AllPlaceAdapter
import com.thequietz.travelog.guide.viewmodel.SpecificGuideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecificGuideFragment : Fragment() {
    private lateinit var _binding: FragmentSpecificGuideBinding
    private val binding get() = _binding
    private val specificGuideViewModel by viewModels<SpecificGuideViewModel>()
    private val args: SpecificGuideFragmentArgs by navArgs()
    lateinit var adapter: AllPlaceAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_specific_guide, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AllPlaceAdapter(FragmentType.SPECIFIC)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = specificGuideViewModel
            rvSpecificPlace.adapter = adapter
        }
        with(specificGuideViewModel) {
            initCurrentArgs(args)
            currentPlaceList.observe(viewLifecycleOwner, { it ->
                it?.let { adapter.submitList(it) }
            })
        }
    }
}
