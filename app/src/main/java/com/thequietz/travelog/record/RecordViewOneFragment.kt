package com.thequietz.travelog.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentRecordViewOneBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordViewOneFragment : Fragment() {
    private lateinit var _binding: FragmentRecordViewOneBinding
    private val binding get() = _binding
    private val recordViewOneViewModel by viewModels<RecordViewOneViewModel>()
    lateinit var adapter: ImageViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_view_one, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ImageViewPagerAdapter()
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = recordViewOneViewModel
            vpReviewViewOne.adapter = adapter
        }
        with(recordViewOneViewModel) {
            // 아이템 초기화
            // createRecord()
            // loadRecord()
            this.imageList.observe(viewLifecycleOwner, { it ->
                it?.let { adapter.submitList(it) }
            })
        }
        setListener()
    }
    fun setListener() {
        binding.vpReviewViewOne.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    recordViewOneViewModel.setCurrentImage(position)
                }
            }
        )
        binding.ibRecordViewOne.setOnClickListener {
            val action = RecordViewOneFragmentDirections
                .actionRecordViewOneFragmentToRecordViewManyFragment()
            findNavController().navigate(action)
        }
    }
}
