package com.thequietz.travelog.schedule.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentSelectBinding
import com.thequietz.travelog.schedule.viewmodel.SelectViewModel

class SelectFragment : Fragment() {
    private var _binding: FragmentSelectBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SelectViewModel
    private lateinit var selectAdapter: SelectAdapter
    private lateinit var selectedAdapter: SelectedAdapter
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = context as Context

        viewModel = SelectViewModel()
        viewModel.areaList.observe(viewLifecycleOwner, {
            if (it.firstOrNull() == null) {
                return@observe
            }

            selectAdapter = SelectAdapter(it, object : SelectAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    view.alpha = 0.7F
                    viewModel.addSelectedList(position, it[position])
                }

            })
            binding.rvSelectSearch.layoutManager = GridLayoutManager(mContext, 2)
            binding.rvSelectSearch.adapter = selectAdapter
        })

        viewModel.selectedDataList.observe(viewLifecycleOwner, {
            selectedAdapter = SelectedAdapter(it, object: SelectedAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    viewModel.removeSelectedList(position)
                }

            })
            binding.rlSelectItem.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            binding.rlSelectItem.adapter = selectedAdapter
        })

        viewModel.loadAreaList()
        viewModel.initSelectedList()
    }
}
