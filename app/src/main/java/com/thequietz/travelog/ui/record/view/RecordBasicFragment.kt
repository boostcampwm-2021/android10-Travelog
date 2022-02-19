package com.thequietz.travelog.ui.record.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
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
import com.thequietz.travelog.ui.record.adapter.RecordBasicViewHolder
import com.thequietz.travelog.ui.record.viewmodel.RecordBasicViewModel
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

class RecordBasicRecyclerViewScrollListener(
    private val binding: FragmentRecordBasicBinding,
    private val layoutManager: LinearLayoutManager,
    private val updateTargetList: (String) -> Unit
) : RecyclerView.OnScrollListener() {
    private var tempPosition = -1
    private var tempDay = ""

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val position = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (tempPosition == position) return
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) ?: return
        viewHolder as RecordBasicViewHolder
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

@AndroidEntryPoint
class RecordBasicFragment :
    GoogleMapFragment<FragmentRecordBasicBinding>(R.layout.fragment_record_basic) {
    private val viewModel by viewModels<RecordBasicViewModel>()
    private val navArgs by navArgs<RecordBasicFragmentArgs>()
    private val adapter by lazy {
        RecordBasicAdapter(
            ::navigateToRecordViewUi,
            ::showMenu,
            ::updateTargetList,
            ::scrollToPosition
        )
    }
    private val layoutManager by lazy {
        binding.rvRecordBasic.layoutManager as LinearLayoutManager
    }
    private val loadingDialog by lazy { LoadingDialog(requireContext()) }

    /* 이미지 추가 기능 삭제 예정
    private var position = 0
    private val getImageUri: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            viewModel.addImage(uri, position)
        }
     */

    private lateinit var scrollListener: RecordBasicRecyclerViewScrollListener

    override var drawMarker = true
    override var isMarkerNumbered = true
    override var drawOrderedPolyline = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.loadData(navArgs.travelId, navArgs.title, navArgs.startDate, navArgs.endDate)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadingDialog.show()
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.rvRecordBasic.clearOnScrollListeners()
    }

    private fun initView() {
        binding.rvRecordBasic.adapter = adapter
        updateNavigationUi()
        disableDragInAppBarLayout()
        setListener()
    }

    private fun updateNavigationUi() = with(binding) {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        collapsingToolbarLayout.setupWithNavController(
            toolbar,
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
        scrollListener = RecordBasicRecyclerViewScrollListener(
            binding,
            binding.rvRecordBasic.layoutManager as LinearLayoutManager,
            ::updateTargetList
        )
        rvRecordBasic.addOnScrollListener(scrollListener)
        appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                if (state == State.COLLAPSED) {
                    binding.collapsingToolbarLayout.title = viewModel.title.value ?: ""
                } else {
                    binding.collapsingToolbarLayout.title = ""
                }
            }
        })
        layoutItemRecordBasicHeader.setOnClickListener {
            val position =
                adapter.getPositionOfHeaderFromDay(binding.tvItemRecordBasicHeaderDay.text.toString())
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
            if (isEmpty) {
                Toast.makeText(requireContext(), "데이터가 없습니다. 일정을 추가해주세요.", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
                loadingDialog.dismiss()
            }
        }
        viewModel.title.observe(viewLifecycleOwner) { title ->
            binding.tvRecordBasicTitle.text = title
        }
        viewModel.date.observe(viewLifecycleOwner) { date ->
            binding.tvRecordBasicSchedule.text = date
            binding.tvItemRecordBasicHeaderDate.text = date
        }
        viewModel.recordImageList.observe(viewLifecycleOwner) {
            viewModel.createData()
        }
        viewModel.recordBasicItemList.observe(viewLifecycleOwner) { recordBasicItemList ->
            updateTargetList()
            adapter.submitList(recordBasicItemList)
            loadingDialog.dismiss()
        }
    }

    private fun updateTargetList(day: String = "Day1") {
        binding.tvItemRecordBasicHeaderDay.text = day
        viewModel.updateTargetList(day, targetList)
    }

    private fun scrollToPosition(position: Int) {
        if (position < 0) return
        layoutManager.scrollToPositionWithOffset(position, 0)
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
