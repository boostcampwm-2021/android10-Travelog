package com.thequietz.travelog.record.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentRecordBasicBinding
import com.thequietz.travelog.record.adapter.RecordBasicAdapter
import com.thequietz.travelog.record.viewmodel.RecordBasicViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordBasicFragment : Fragment() {
    private val navArgs by navArgs<RecordBasicFragmentArgs>()
    private val viewModel by viewModels<RecordBasicViewModel>()
    private val adapter by lazy {
        RecordBasicAdapter(
            ::navigateToRecordViewUi,
            ::navigateToRecordAddUi,
            ::showMenu
        )
    }

    // TODO("아래 코드는 ViewModel로 이동 예정")
    private var position = 0
    private val getImageUri: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            viewModel.addImage(uri, position)
        }

    private lateinit var binding: FragmentRecordBasicBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordBasicBinding.inflate(inflater, container, false)

        binding.rvRecordBasic.adapter = adapter

        viewModel.loadData(navArgs.travelId, navArgs.title, navArgs.startDate, navArgs.endDate)

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        viewModel.title.observe(viewLifecycleOwner) { title ->
            binding.tvRecordBasicTitle.text = title
        }
        viewModel.date.observe(viewLifecycleOwner) { date ->
            binding.tvRecordBasicSchedule.text = date
        }
        viewModel.recordBasicItemList.observe(viewLifecycleOwner) { recordBasicItemList ->
            adapter.submitList(recordBasicItemList)
        }
        viewModel.recordImageList.observe(viewLifecycleOwner) {
            viewModel.createData()
        }
    }

    private fun navigateToRecordViewUi(index: Int, day: String, group: Int) {
        val action =
            RecordBasicFragmentDirections.actionRecordBasicFragmentToRecordViewOneFragment(
                travelId = navArgs.travelId,
                index = index,
                day = day,
                group = group
            )

        findNavController().navigate(action)
    }

    private fun navigateToRecordAddUi(day: String) {
        val action = RecordBasicFragmentDirections.actionRecordBasicFragmentToRecordAddFragment(
            travelId = navArgs.travelId,
            day = day
        )

        findNavController().navigate(action)
    }

    private fun showMenu(view: View, position: Int) {
        PopupMenu(requireActivity(), view).apply {
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.add_image -> {
                        this@RecordBasicFragment.position = position
                        getImageUri.launch("image/*")
                    }
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
