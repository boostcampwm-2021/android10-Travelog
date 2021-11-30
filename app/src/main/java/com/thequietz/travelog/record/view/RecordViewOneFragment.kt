package com.thequietz.travelog.record.view

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.thequietz.travelog.LoadingDialog
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentRecordViewOneBinding
import com.thequietz.travelog.makeSnackBar
import com.thequietz.travelog.record.adapter.ImageViewPagerAdapter
import com.thequietz.travelog.record.viewmodel.RecordViewOneViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordViewOneFragment : Fragment() {
    private lateinit var _binding: FragmentRecordViewOneBinding
    private val binding get() = _binding
    private val recordViewOneViewModel by viewModels<RecordViewOneViewModel>()
    private val args: RecordViewOneFragmentArgs by navArgs()
    private val adapter by lazy { ImageViewPagerAdapter() }
    lateinit var loading: LoadingDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordViewOneBinding.inflate(inflater, container, false)
        loading = LoadingDialog(requireContext())
        loading.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = recordViewOneViewModel
            vpReviewViewOne.adapter = adapter
            ciReviewViewOne.attachToRecyclerView(binding.vpReviewViewOne.getChildAt(0) as RecyclerView)
        }
        with(recordViewOneViewModel) {
            setListener()
            initVariable(args)
            loadRecord()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.vpReviewViewOne.setCurrentItem(recordViewOneViewModel.startInd, true)
                loading.dismiss()
            }, 1000)

            dataList.observe(viewLifecycleOwner, { it ->
                it?.let { adapter.submitList(it) }
            })
        }
    }

    private fun setListener() {
        with(binding) {
            vpReviewViewOne.registerOnPageChangeCallback(object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        if (recordViewOneViewModel.islistUpdate.value == true) {
                            RecordViewOneViewModel.currentPosition.value?.let {
                                super.onPageSelected(it)
                                vpReviewViewOne.post {
                                    vpReviewViewOne.setCurrentItem(it, false)
                                }
                                recordViewOneViewModel.resetIsListUpdate()
                            }
                        } else {
                            super.onPageSelected(position)
                            recordViewOneViewModel.setCurrentImage(position)
                            recordViewOneViewModel.setCurrentPosition(position)
                        }
                    }
                }
            )
            ibRecordViewOne.setOnClickListener {
                val action =
                    recordViewOneViewModel.currentImage.value?.let {
                        val action = RecordViewOneFragmentDirections
                            .actionRecordViewOneFragmentToRecordViewManyFragment(it.recordImage.travelId)
                        findNavController().navigate(action)
                    }
            }
            ibRecordViewOneEditComment.setOnClickListener {
                binding.etRecordViewOne.isEnabled = !binding.etRecordViewOne.isEnabled
                binding.tvRecordViewOneSave.visibility = View.VISIBLE
                binding.ibRecordViewOneEditComment.visibility = View.GONE
            }
            tvRecordViewOneSave.setOnClickListener {
                val currentText = binding.etRecordViewOne.text.toString()
                showChangeCommentDialog(currentText)
            }
            tvRecordViewOneReduce.setOnClickListener {
                val popup = PopupMenu(requireContext(), it)
                popup.menuInflater.inflate(R.menu.menu_record_image_view, popup.menu)

                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.imageDelete -> {
                            showDeleteDialog()
                        }
                        R.id.imageAdd -> {
                            val action = RecordViewOneFragmentDirections
                                .actionRecordViewOneFragmentToRecordAddImageFragment()
                            findNavController().navigate(action)
                        }
                    }
                    false
                }
                popup.show()
            }
        }
    }

    private fun showChangeCommentDialog(currentText: String = "") {
        AlertDialog.Builder(requireContext())
            .setTitle("내용 변경")
            .setMessage("현재 내용을 저장하시겠습니까?")
            .setNegativeButton("예") { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    recordViewOneViewModel.updateComment(currentText)
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.etRecordViewOne.isEnabled = !binding.etRecordViewOne.isEnabled
                        binding.tvRecordViewOneSave.visibility = View.GONE
                        binding.ibRecordViewOneEditComment.visibility = View.VISIBLE
                    }
                }
                makeSnackBar(binding.clRecordViewOne, "현재 내용이 저장되었습니다")
            }
            .setPositiveButton("아니오") { dialog, which ->
            }.show()
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("삭제 확인")
            .setMessage("현재 이미지를 삭제하시겠습니까?")
            .setNegativeButton("예") { dialog, which ->
                if (recordViewOneViewModel.currentImage.value?.newRecordImage?.newRecordImageId != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        recordViewOneViewModel.delete()
                    }
                    makeSnackBar(binding.clRecordViewOne, "이미지가 삭제되었습니다")
                } else {
                    makeSnackBar(binding.clRecordViewOne, "삭제할 이미지가 없습니다")
                }
            }
            .setPositiveButton("아니오") { dialog, which ->
            }.show()
    }
}
