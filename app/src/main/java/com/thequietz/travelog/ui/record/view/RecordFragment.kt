package com.thequietz.travelog.ui.record.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thequietz.travelog.R
import com.thequietz.travelog.common.BaseFragment
import com.thequietz.travelog.databinding.FragmentRecordBinding
import com.thequietz.travelog.ui.record.adapter.RecordAdapter
import com.thequietz.travelog.ui.record.viewmodel.RecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordFragment : BaseFragment<FragmentRecordBinding>(R.layout.fragment_record) {
    private val viewModel by viewModels<RecordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        val adapter = RecordAdapter(::navigateToRecordBasicUi)
        binding.rvRecord.adapter = adapter

        viewModel.loadData()

        viewModel.recordList.observe(viewLifecycleOwner) { recordList ->
            binding.isEmpty = recordList.isEmpty()
            adapter.submitList(recordList)
        }

        return view
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
