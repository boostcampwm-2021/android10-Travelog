package com.thequietz.travelog.record.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thequietz.travelog.databinding.FragmentRecordBinding
import com.thequietz.travelog.record.adapter.RecordAdapter
import com.thequietz.travelog.record.viewmodel.RecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordFragment : Fragment() {
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RecordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)

        val adapter = RecordAdapter(::navigateToRecordBasicUi)
        binding.rvRecord.adapter = adapter

        viewModel.loadData()

        viewModel.recordList.observe(viewLifecycleOwner) { recordList ->
            if (recordList.isEmpty()) {
                binding.tvNoRecord.visibility = View.VISIBLE
            } else {
                binding.tvNoRecord.visibility = View.INVISIBLE
            }
            adapter.submitList(recordList)
        }

        return binding.root
    }

    private fun navigateToRecordBasicUi(
        travelId: Int,
        title: String,
        startDate: String,
        endDate: String
    ) {
        val action = RecordFragmentDirections.actionRecordFragmentToRecordBasicFragment(
            travelId,
            title,
            startDate,
            endDate
        )

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
