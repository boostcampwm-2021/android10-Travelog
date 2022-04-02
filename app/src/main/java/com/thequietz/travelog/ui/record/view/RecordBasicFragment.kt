package com.thequietz.travelog.ui.record.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.thequietz.travelog.R
import com.thequietz.travelog.common.GoogleMapFragment
import com.thequietz.travelog.common.LoadingDialog
import com.thequietz.travelog.databinding.FragmentRecordBasicBinding
import com.thequietz.travelog.ui.record.adapter.RecordBasicAdapter
import com.thequietz.travelog.ui.record.viewmodel.RecordBasicViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class RecordBasicFragment :
    GoogleMapFragment<FragmentRecordBasicBinding>(R.layout.fragment_record_basic) {
    private val viewModel by viewModels<RecordBasicViewModel>()
    private val navArgs by navArgs<RecordBasicFragmentArgs>()
    private val adapter by lazy {
        RecordBasicAdapter(
            ::navigateToRecordViewUi,
            ::updateTargetList,
            ::scrollToPosition
        )
    }
    private val loadingDialog by lazy { LoadingDialog(requireContext()) }

    override var drawMarker = true
    override var isMarkerNumbered = true
    override var drawOrderedPolyline = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadData(navArgs.travelId, navArgs.title, navArgs.startDate, navArgs.endDate)
        loadingDialog.show()
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.viewModel = viewModel
        binding.rvRecordBasic.adapter = adapter
        updateNavigationUi()
        disableDragInAppBarLayout()
        setListener()
    }

    private fun updateNavigationUi() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.collapsingToolbarLayout.setupWithNavController(
            binding.toolbar,
            findNavController(),
            appBarConfiguration
        )
    }

    private fun disableDragInAppBarLayout() {
        val params = binding.appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = AppBarLayout.Behavior()
        behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean = false
        })
        params.behavior = behavior
    }

    private fun setListener() = with(binding) {
        rvRecordBasic.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var tempPosition = -1

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (position == tempPosition) return
                val day = adapter.getDay(position)
                val date = adapter.getDate(position)
                updateTargetList(day, date)
                tempPosition = position
            }
        })
        appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                appBarState = state
            }
        })
        layoutItemRecordBasicHeader.setOnClickListener {
            val position = adapter.getPositionOfHeaderFromDay(
                this@RecordBasicFragment.viewModel.headerDay.value ?: ""
            )
            scrollToPosition(position)
        }
    }

    override fun initViewModel() {
        subscribeUi()
    }

    override fun initTargetList() {
        updateTargetList()
    }

    private fun subscribeUi() {
        viewModel.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
            if (!isEmpty) return@observe
            Toast.makeText(requireContext(), "데이터가 없습니다. 일정을 추가해주세요.", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
            loadingDialog.dismiss()
        }
        viewModel.recordBasicItemList.observe(viewLifecycleOwner) { recordBasicItemList ->
            adapter.submitList(recordBasicItemList)
            loadingDialog.dismiss()
        }
    }

    private fun updateTargetList(day: String = "Day1", date: String = "") {
        viewModel.updateTargetList(day, date, targetList)
    }

    private fun scrollToPosition(position: Int) {
        if (position < 0) return
        (binding.rvRecordBasic.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
            position,
            0
        )
    }

    private fun navigateToRecordViewUi(day: String, place: String) {
        val action =
            RecordBasicFragmentDirections.actionRecordBasicFragmentToRecordViewOneFragment(
                travelId = navArgs.travelId,
                day = day,
                place = place
            )

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        binding.rvRecordBasic.clearOnScrollListeners()
        super.onDestroyView()
    }
}

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {
    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private var mCurrentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        when {
            verticalOffset == 0 -> {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED)
                }
                mCurrentState = State.EXPANDED
            }
            abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED)
                }
                mCurrentState = State.COLLAPSED
            }
            else -> {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE)
                }
                mCurrentState = State.IDLE
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State)
}
