package com.thequietz.travelog.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.thequietz.travelog.databinding.FragmentRecordBasicBinding

class RecordBasicFragment : Fragment() {
    private val viewModel by viewModels<RecordBasicViewModel>()
    private val adapter by lazy { RecordBasicAdapter() }

    private lateinit var binding: FragmentRecordBasicBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordBasicBinding.inflate(inflater, container, false)

        binding.rvRecordBasic.adapter = adapter

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        viewModel.title.observe(viewLifecycleOwner) { title ->
            binding.tvRecordBasicTitle.text = title
        }
        viewModel.date.observe(viewLifecycleOwner) { date ->
            binding.tvRecordBasicSchedule.text = date
        }
        viewModel.travelDestinations.observe(viewLifecycleOwner) { travelDestinations ->
            adapter.submitList(travelDestinations)
        }
    }
}
