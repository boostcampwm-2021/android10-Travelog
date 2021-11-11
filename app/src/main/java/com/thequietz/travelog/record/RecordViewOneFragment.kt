package com.thequietz.travelog.record

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentRecordViewOneBinding
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
    lateinit var adapter: ImageViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_record_view_one, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ImageViewPagerAdapter()
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = recordViewOneViewModel
            vpReviewViewOne.adapter = adapter
            println("args  ${args.index}")
            vpReviewViewOne.post {
                vpReviewViewOne.setCurrentItem(args.index - 1, false)
            }
        }
        with(recordViewOneViewModel) {
            // 아이템 초기화
            // createRecord()
            // loadRecord()

            this.imageList.observe(viewLifecycleOwner, { it ->
                it?.let { adapter.submitList(it) }
            })
        }
        setListener()
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
                })
            ibRecordViewOne.setOnClickListener {
                val action = RecordViewOneFragmentDirections
                    .actionRecordViewOneFragmentToRecordViewManyFragment()
                findNavController().navigate(action)
            }
            ibRecordViewOneEditComment.setOnClickListener {
                binding.etRecordViewOne.isEnabled = !binding.etRecordViewOne.isEnabled
                binding.tvRecordViewOneSave.visibility = View.VISIBLE
                binding.ibRecordViewOneEditComment.visibility = View.GONE
            }
            tvRecordViewOneSave.setOnClickListener {
                val currentText = binding.etRecordViewOne.text.toString()
                if (recordViewOneViewModel.isCommentChanged(currentText) && currentText != "") {
                    showChangeCommentDialog(currentText)
                }
                binding.etRecordViewOne.isEnabled = !binding.etRecordViewOne.isEnabled
                binding.tvRecordViewOneSave.visibility = View.GONE
                binding.ibRecordViewOneEditComment.visibility = View.VISIBLE
            }
            tvRecordViewOneReduce.setOnClickListener {
                val popup = PopupMenu(requireContext(), it)
                popup.menuInflater.inflate(R.menu.menu_record_image_view, popup.menu)

                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.imageDelete -> {
                            showDeleteDialog()
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
                }
                Toast.makeText(requireContext(), "현재 내용이 저장되었습니다", Toast.LENGTH_SHORT)
                    .show()
            }
            .setPositiveButton("아니오") { dialog, which ->
            }.show()
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("삭제 확인")
            .setMessage("현재 이미지를 삭제하시겠습니까?")
            .setNegativeButton("예") { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    recordViewOneViewModel.delete()
                }
                Snackbar.make(binding.layoutRecordViewOne, "이미지가 삭제되었습니다", Snackbar.LENGTH_SHORT)
                    .show()
            }
            .setPositiveButton("아니오") { dialog, which ->
            }.show()
    }
}
