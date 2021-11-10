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

        return binding.root
    }
}
