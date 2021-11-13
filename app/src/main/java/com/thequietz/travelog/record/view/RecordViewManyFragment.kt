package com.thequietz.travelog.record.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thequietz.travelog.databinding.FragmentRecordViewManyBinding
import com.thequietz.travelog.record.adapter.MultiViewAdapter
import com.thequietz.travelog.record.viewmodel.RecordViewManyViewModel
import com.thequietz.travelog.record.viewmodel.RecordViewOneViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordViewManyFragment : Fragment() {
    private lateinit var _binding: FragmentRecordViewManyBinding
    private val binding get() = _binding
    private val recordViewManyViewModel by viewModels<RecordViewManyViewModel>()
    private val adapter by lazy { MultiViewAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordViewManyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = recordViewManyViewModel
            rvRecordViewMany.adapter = adapter
        }
        with(recordViewManyViewModel) {
            dataList.observe(viewLifecycleOwner, { it ->
                it?.let { adapter.submitList(it) }
            })
        }
        setListener()
    }

    fun setListener() {
        binding.ibRecordViewMany.setOnClickListener {
            val action = RecordViewOneViewModel.currentPosition.value?.let { it ->
                RecordViewManyFragmentDirections
                    .actionRecordViewManyFragmentToRecordViewOneFragment(it + 1)
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
    }
}
