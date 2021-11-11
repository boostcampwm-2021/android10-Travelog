package com.thequietz.travelog.guide.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thequietz.travelog.FragmentType
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentGuideBinding
import com.thequietz.travelog.guide.adapter.AllPlaceAdapter
import com.thequietz.travelog.guide.adapter.RecommendPlaceAdapter
import com.thequietz.travelog.guide.viewmodel.GuideViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AREA_LIST"

@AndroidEntryPoint
class GuideFragment : Fragment() {
    private var _binding: FragmentGuideBinding? = null
    private val binding get() = _binding!!
    private val guideViewModel by viewModels<GuideViewModel>()
    lateinit var allPlaceAdapter: AllPlaceAdapter
    lateinit var recommendPlaceAdapter: RecommendPlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guide, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allPlaceAdapter = AllPlaceAdapter(FragmentType.DOSI)
        recommendPlaceAdapter = RecommendPlaceAdapter()

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = guideViewModel
            rvPlaceAll.adapter = allPlaceAdapter
            rvRecommendPlace.adapter = recommendPlaceAdapter
        }

        with(guideViewModel) {
            // initRecommendPlaceData()
            allDoSiList.observe(viewLifecycleOwner, { it ->
                it?.let { allPlaceAdapter.submitList(it) }
            })
            allRecommendPlaceList.observe(viewLifecycleOwner, { it ->
                it?.let { recommendPlaceAdapter.submitList(it) }
            })
        }
        setClickListener()
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun setClickListener() {
        binding.etSearch.setOnTouchListener(
            View.OnTouchListener { p0, p1 ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (p1?.action == MotionEvent.ACTION_UP) {
                    if (p1.rawX >= binding.etSearch.right - binding.etSearch.compoundDrawables.get(DRAWABLE_RIGHT).bounds.width()) {
                        if (binding.etSearch.text.toString() == "") {
                            Toast.makeText(requireContext(), "검색어를 입력하세요!", Toast.LENGTH_SHORT).show()
                        } else {
                            val action = GuideFragmentDirections
                                .actionGuideFragmentToSpecificGuideFragment(binding.etSearch.text.toString())
                            findNavController().navigate(action)
                        }
                    }
                }
                false
            }
        )
    }
}
