package com.thequietz.travelog.record.view

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.thequietz.travelog.addToByteList
import com.thequietz.travelog.byteListToPdf
import com.thequietz.travelog.databinding.FragmentRecordViewManyBinding
import com.thequietz.travelog.record.adapter.RecordViewManyMultiViewAdapter
import com.thequietz.travelog.record.viewmodel.RecordViewManyInnerViewModel
import com.thequietz.travelog.record.viewmodel.RecordViewManyViewModel
import com.thequietz.travelog.record.viewmodel.RecordViewOneViewModel
import com.thequietz.travelog.share2Pdf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class RecordViewManyFragment : Fragment() {
    private lateinit var _binding: FragmentRecordViewManyBinding
    private val binding get() = _binding
    private val recordViewManyViewModel by viewModels<RecordViewManyViewModel>()
    private val recordViewInnerViewModel by viewModels<RecordViewManyInnerViewModel>()
    private val adapter by lazy { RecordViewManyMultiViewAdapter(recordViewInnerViewModel) }
    private val args: RecordViewManyFragmentArgs by navArgs()
    val byteList = mutableListOf<ByteArrayOutputStream>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordViewManyBinding.inflate(inflater, container, false)
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PackageManager.PERMISSION_GRANTED
        )
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
            change2MyRecord(args)
            dataList.observe(viewLifecycleOwner, { it ->
                it?.let { adapter.submitList(it) }
            })
        }
        setListener()
    }

    fun setListener() {
        binding.ibRecordViewMany.setOnClickListener {
            val action = RecordViewOneViewModel.currentPosition.value?.let { pos ->
                RecordViewOneViewModel.currentJoinRecord.value?.let { record ->
                    RecordViewManyFragmentDirections
                        .actionRecordViewManyFragmentToRecordViewOneFragment(
                            args.travelId,
                            record.recordImage.day,
                            record.newRecordImage.newPlace
                        )
                }
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.ibRecordCamera.setOnClickListener {
            addToByteList(byteList, binding.clRecordViewMany)
            Toast.makeText(requireContext(), "스크린샷 생성", Toast.LENGTH_SHORT).show()
        }
        binding.ibRecordViewPdf.setOnClickListener {
            val fileName = "recordViewManyPdf"
            byteListToPdf(byteList, fileName)
            Toast.makeText(requireContext(), "pdf파일 생성", Toast.LENGTH_SHORT).show()

            val intent = share2Pdf(fileName, requireContext())
            startActivity(Intent.createChooser(intent, "파일 공유"))
        }
        binding.ibRecordDelete.setOnClickListener {
            recordViewInnerViewModel.changeDeleteState()
            binding.ibRecordDelete.visibility = View.GONE
            binding.tvRecordDelete.visibility = View.VISIBLE
        }
        binding.tvRecordDelete.setOnClickListener {
            binding.tvRecordDelete.visibility = View.GONE
            binding.ibRecordDelete.visibility = View.VISIBLE
            if (recordViewInnerViewModel.checkedList.value?.size != 0) {
                showDeleteDiaglog()
            }
            recordViewInnerViewModel.changeDeleteState()
        }
    }

    private fun showDeleteDiaglog() {
        AlertDialog.Builder(requireContext())
            .setTitle("삭제 확인")
            .setMessage("선택된 이미지를 삭제하시겠습니까?")
            .setNegativeButton("예") { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    recordViewInnerViewModel.deleteChecked()
                    recordViewManyViewModel.change2MyRecord(args)
                }
                Snackbar.make(binding.clRecordViewMany, "이미지가 삭제되었습니다", Snackbar.LENGTH_SHORT)
                    .show()
            }
            .setPositiveButton("아니오") { dialog, which ->
                recordViewInnerViewModel.clearChecked()
            }.show()
    }
}
