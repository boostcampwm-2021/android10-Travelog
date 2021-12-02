package com.thequietz.travelog.record.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentRecordBinding
import com.thequietz.travelog.record.adapter.RecordAdapter
import com.thequietz.travelog.record.viewmodel.RecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordFragment : Fragment(R.layout.fragment_record) {
    private lateinit var binding: FragmentRecordBinding

    private val viewModel by viewModels<RecordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordBinding.inflate(inflater, container, false)

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
}
