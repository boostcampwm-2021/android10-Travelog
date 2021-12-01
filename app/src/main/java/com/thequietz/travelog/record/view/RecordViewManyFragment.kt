package com.thequietz.travelog.record.view

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentRecordViewManyBinding
import com.thequietz.travelog.makePdf
import com.thequietz.travelog.makeSnackBar
import com.thequietz.travelog.record.adapter.RecordViewManyMultiViewAdapter
import com.thequietz.travelog.record.viewmodel.RecordViewManyInnerViewModel
import com.thequietz.travelog.record.viewmodel.RecordViewManyViewModel
import com.thequietz.travelog.record.viewmodel.RecordViewOneViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordViewManyFragment : Fragment() {
    private lateinit var _binding: FragmentRecordViewManyBinding
    private val binding get() = _binding
    private val recordViewManyViewModel by viewModels<RecordViewManyViewModel>()
    private val recordViewInnerViewModel by viewModels<RecordViewManyInnerViewModel>()
    private val adapter by lazy { RecordViewManyMultiViewAdapter(recordViewInnerViewModel) }
    private val args: RecordViewManyFragmentArgs by navArgs()
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
        setToolbar()
        setListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_delete) {
            item.isChecked = !item.isChecked
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        val navController = findNavController()
        val appBarConfig = AppBarConfiguration.Builder(navController.graph).build()

        binding.tbRecordViewMany.apply {
            setupWithNavController(navController, appBarConfig)
            inflateMenu(R.menu.menu_record_view_many)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_delete -> {
                        if (recordViewInnerViewModel.deleteState.value == false) {
                            recordViewInnerViewModel.changeDeleteState()
                        } else {
                            if (recordViewInnerViewModel.checkedList.value?.size != 0) {
                                showDeleteDialog()
                            }
                            recordViewInnerViewModel.changeDeleteState()
                        }
                    }
                    R.id.action_share -> {
                        recordViewManyViewModel.travelName.value?.let { str ->
                            makePdf(
                                binding.rvRecordViewMany,
                                binding.clRecordViewMany,
                                str,
                                requireContext()
                            )
                        }
                    }
                }
                return@setOnMenuItemClickListener false
            }
        }
    }

    fun setListener() {
        binding.ibRecordViewMany.setOnClickListener {
            val action = RecordViewOneViewModel.currentPosition.value?.let { pos ->
                RecordViewOneViewModel.currentJoinRecord.value?.let { record ->
                    RecordViewManyFragmentDirections
                        .actionRecordViewManyFragmentToRecordViewOneFragment(
                            args.travelId,
                            record.recordImage.day,
                            record.newRecordImage.newPlace,
                            RecordViewOneViewModel.currentJoinRecord.value!!.newRecordImage.newRecordImageId,
                            from = "viewManyGridBtn"
                        )
                }
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("삭제 확인")
            .setMessage("선택된 이미지를 삭제하시겠습니까?")
            .setNegativeButton("예") { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    recordViewInnerViewModel.deleteChecked()
                    recordViewManyViewModel.change2MyRecord(args)
                }
                makeSnackBar(binding.clRecordViewMany, "이미지가 삭제되었습니다")
            }
            .setPositiveButton("아니오") { dialog, which ->
                recordViewInnerViewModel.clearChecked()
            }.show()
    }
}
