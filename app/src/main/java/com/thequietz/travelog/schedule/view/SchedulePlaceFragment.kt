package com.thequietz.travelog.schedule.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentSchedulePlaceBinding
import com.thequietz.travelog.schedule.adapter.SchedulePlaceAdapter
import com.thequietz.travelog.schedule.adapter.SchedulePlaceSelectedAdapter
import com.thequietz.travelog.schedule.viewmodel.SchedulePlaceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchedulePlaceFragment : Fragment() {
    private var _binding: FragmentSchedulePlaceBinding? = null
    private val binding get() = _binding!!

    private lateinit var schedulePlaceAdapter: SchedulePlaceAdapter
    private lateinit var schedulePlaceSelectedAdapter: SchedulePlaceSelectedAdapter
    private lateinit var mContext: Context
    private lateinit var gson: Gson

    private val viewModel: SchedulePlaceViewModel by viewModels()

    private fun Button.setEnable() {
        this.isEnabled = true
        this.isClickable = true
        this.alpha = 1F
    }

    private fun Button.setDisable() {
        this.isEnabled = false
        this.isClickable = false
        this.alpha = 0.4F
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_place, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = context as Context
        gson = Gson()

        setToolbar()

        viewModel.placeList.observe(viewLifecycleOwner, {

            binding.tvEmptyResult.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }

            schedulePlaceAdapter = SchedulePlaceAdapter(
                object : SchedulePlaceAdapter.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int, toggle: Boolean) {
                        val state = binding.rvSelectSearch.layoutManager?.onSaveInstanceState()
                        viewModel.saveViewState(state)
                        when (toggle) {
                            true -> viewModel.addPlaceSelectedList(it[position])

                            false -> viewModel.removePlaceSelectedList(it[position].cityName)
                        }
                    }
                }
            )

            binding.rvSelectSearch.adapter = schedulePlaceAdapter
            binding.rvSelectSearch.layoutManager = GridLayoutManager(mContext, 2)
            binding.lifecycleOwner = viewLifecycleOwner

            binding.rvSelectSearch.layoutManager?.onRestoreInstanceState(viewModel.viewState.value)
            schedulePlaceAdapter.submitList(it)
        })

        viewModel.placeSelectedList.observe(viewLifecycleOwner, {
            if (it.size == 0) {
                binding.toolbar.menu.findItem(R.id.action_next).isEnabled = false
                binding.rvSelectItem.visibility = View.GONE
            } else {
                binding.toolbar.menu.findItem(R.id.action_next).isEnabled = true
                binding.rvSelectItem.visibility = View.VISIBLE
            }

            schedulePlaceSelectedAdapter = SchedulePlaceSelectedAdapter(
                object : SchedulePlaceSelectedAdapter.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val state = binding.rvSelectSearch.layoutManager?.onSaveInstanceState()
                        viewModel.saveViewState(state)

                        viewModel.removePlaceSelectedList(it[position].cityName)
                    }
                }
            )
            binding.rvSelectItem.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            binding.rvSelectItem.adapter = schedulePlaceSelectedAdapter
            binding.lifecycleOwner = viewLifecycleOwner

            schedulePlaceSelectedAdapter.submitList(it)
        })

        viewModel.loadPlaceList()
        viewModel.initPlaceSelectedList()
    }

    private fun setToolbar() {
        val navController = findNavController()
        val appBarConfig = AppBarConfiguration.Builder(navController.graph).build()

        binding.toolbar.apply {
            setupWithNavController(navController, appBarConfig)
            title = ""
            inflateMenu(R.menu.menu_schedule_place)

            val searchView = (menu.findItem(R.id.action_search).actionView as SearchView)
            searchView.apply {
                queryHint = "목적지를 검색해보세요!"
                setIconifiedByDefault(false)

                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query.isNullOrEmpty()) {
                            viewModel.loadPlaceList()
                        } else {
                            viewModel.searchPlaceList(query)
                        }
                        isIconified = true
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }

            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_next) {
                    val placeList = viewModel.placeSelectedList.value
                    val action =
                        SchedulePlaceFragmentDirections.actionSchedulePlaceFragmentToScheduleSelectFragment(
                            placeList?.toTypedArray() ?: arrayOf()
                        )
                    findNavController().navigate(action)

                    return@setOnMenuItemClickListener true
                }
                return@setOnMenuItemClickListener false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
