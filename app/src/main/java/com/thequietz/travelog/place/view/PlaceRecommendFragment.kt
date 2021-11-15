package com.thequietz.travelog.place.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentPlaceRecommendBinding
import com.thequietz.travelog.place.adapter.PlaceRecommendAdapter
import com.thequietz.travelog.place.viewmodel.PlaceRecommendViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceRecommendFragment : Fragment() {

    private var _binding: FragmentPlaceRecommendBinding? = null
    private val binding get() = _binding!!

    private lateinit var _context: Context
    private val viewModel: PlaceRecommendViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_place_recommend, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _context = requireContext()

        viewModel.dataList.observe(viewLifecycleOwner, {
            binding.lifecycleOwner = viewLifecycleOwner
            binding.rvPlaceRecommend.adapter = PlaceRecommendAdapter()
            binding.rvPlaceRecommend.layoutManager = LinearLayoutManager(_context)

            (binding.rvPlaceRecommend.adapter as PlaceRecommendAdapter).submitList(it)
        })

        viewModel.loadData()
    }
}
