package com.thequietz.travelog.record.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentRecordBasicBinding
import com.thequietz.travelog.map.GoogleMapFragment
import com.thequietz.travelog.record.adapter.RecordBasicAdapter
import com.thequietz.travelog.record.adapter.RecordBasicViewHolder
import com.thequietz.travelog.record.viewmodel.RecordBasicViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

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

@AndroidEntryPoint
class RecordBasicFragment : GoogleMapFragment<FragmentRecordBasicBinding, RecordBasicViewModel>() {
    override val layoutId = R.layout.fragment_record_basic
    override val viewModel by viewModels<RecordBasicViewModel>()

    private val navArgs by navArgs<RecordBasicFragmentArgs>()
    private val adapter by lazy {
        RecordBasicAdapter(
            ::navigateToRecordViewUi,
            ::showMenu,
            ::updateTargetList
        )
    }

    /* 이미지 추가 기능 삭제 예정
    private var position = 0
    private val getImageUri: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            viewModel.addImage(uri, position)
        }
     */

    override var drawMarker = true
    override var isMarkerNumbered = true
    override var drawOrderedPolyline = true

    private var tempPosition = -1
    private var tempDay = ""

    inner class RecordBasicRecyclerViewScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val position =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

            if (tempPosition == position) return

            val viewHolder =
                recyclerView.findViewHolderForAdapterPosition(position) as RecordBasicViewHolder
            val day = viewHolder.getDay()
            val date = viewHolder.getDate()

            if (tempDay == day) return

            updateTargetList(day)
            binding.tvItemRecordBasicHeaderDay.text = day
            binding.tvItemRecordBasicHeaderDate.text = date

            tempPosition = position
            tempDay = day
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvRecordBasic.adapter = adapter
        binding.rvRecordBasic.addOnScrollListener(RecordBasicRecyclerViewScrollListener())

        val appBarConfiguration = AppBarConfiguration(findNavController().graph)

        binding.appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                if (state == State.COLLAPSED) {
                    binding.collapsingToolbarLayout.title = viewModel.title.value ?: ""
                } else {
                    binding.collapsingToolbarLayout.title = ""
                }
            }
        })

        binding.collapsingToolbarLayout.apply {
            setupWithNavController(
                binding.toolbar,
                findNavController(),
                appBarConfiguration
            )
            title = ""
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun initViewModel() {
        viewModel.loadData(navArgs.travelId, navArgs.title, navArgs.startDate, navArgs.endDate)

        subscribeUi()
    }

    override fun initTargetList() {
        updateTargetList()
    }

    private fun subscribeUi() {
        viewModel.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                Toast.makeText(requireContext(), "데이터가 없습니다. 일정을 추가해주세요.", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }
        viewModel.title.observe(viewLifecycleOwner) { title ->
            binding.tvRecordBasicTitle.text = title
        }
        viewModel.date.observe(viewLifecycleOwner) { date ->
            binding.tvRecordBasicSchedule.text = date
        }
        viewModel.recordBasicItemList.observe(viewLifecycleOwner) { recordBasicItemList ->
            updateTargetList()
            adapter.submitList(recordBasicItemList)
        }
        viewModel.recordImageList.observe(viewLifecycleOwner) {
            viewModel.createData()
        }
    }

    private fun updateTargetList(day: String = "Day1") {
        viewModel.updateTargetList(day, targetList)
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

    /*
    private fun navigateToRecordAddUi(day: String) {
        val action = RecordBasicFragmentDirections.actionRecordBasicFragmentToRecordAddFragment(
            travelId = navArgs.travelId,
            day = day
        )

        findNavController().navigate(action)
    }
    */

    private fun showMenu(view: View, position: Int) {
        PopupMenu(requireActivity(), view).apply {
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
//                    R.id.add_image -> {
//                        this@RecordBasicFragment.position = position
//                        getImageUri.launch("image/*")
//                    }
                    R.id.delete_record -> {
                        viewModel.deleteRecord(position)
                    }
                }
                true
            }
            menuInflater.inflate(R.menu.menu_record, menu)
            show()
        }
    }
}
