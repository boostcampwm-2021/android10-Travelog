package com.thequietz.travelog.record

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
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
            vpReviewViewOne.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    recordViewOneViewModel.setCurrentImage(position)
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
                val currentId = recordViewOneViewModel.getId()
                if (recordViewOneViewModel.isCommentChanged(currentText) && currentText != "") {
                    showDialog(currentText, currentId)
                }
                binding.etRecordViewOne.isEnabled = !binding.etRecordViewOne.isEnabled
                binding.tvRecordViewOneSave.visibility = View.GONE
                binding.ibRecordViewOneEditComment.visibility = View.VISIBLE
            }
        }
    }
    private fun showDialog(currentText: String, currentId: Int?) {
        AlertDialog.Builder(requireContext())
            .setTitle("변경사항 확인")
            .setMessage("변경사항을 저장하시겠습니까?")
            .setNegativeButton("예") { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    recordViewOneViewModel.updateComment(currentText, currentId)
                }
                Toast.makeText(requireContext(), "변경사항이 적용되었습니다.", Toast.LENGTH_SHORT)
                    .show()
            }
            .setPositiveButton("아니오") { dialog, which ->
                recordViewOneViewModel.resetComment()
            }.show()
    }
}
