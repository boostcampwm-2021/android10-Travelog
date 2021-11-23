package com.thequietz.travelog.record.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentRecordAddImageBinding
import com.thequietz.travelog.record.adapter.RecordAddImageAdapter
import com.thequietz.travelog.record.model.RecordImage
import com.thequietz.travelog.record.viewmodel.RecordAddImageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordAddImageFragment : Fragment() {
    private lateinit var _binding: FragmentRecordAddImageBinding
    private val binding get() = _binding
    private val recordAddImageViewModel by viewModels<RecordAddImageViewModel>()
    private val adapter by lazy { RecordAddImageAdapter() }
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val clipData = result.data?.clipData
        val res = mutableListOf<RecordImage>()
        clipData?.let {
            (0 until it.itemCount).forEachIndexed { ind, item ->
                res.add(
                    RecordImage().copy(
                        title = "제주도 여행",
                        startDate = "2021.10.27",
                        endDate = "2021.10.29",
                        day = binding.tvSchedule.text.toString(),
                        place = binding.tvDestination.text.toString(),
                        url = it.getItemAt(ind).uri.toString(),
                        comment = "test입니다~",
                        group = 6
                    )
                )
            }
            recordAddImageViewModel.addImage(res)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordAddImageBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        initToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = recordAddImageViewModel
            rvRecordAddImage.adapter = adapter
        }
        with(recordAddImageViewModel) {
            imageList.observe(viewLifecycleOwner, { it ->
                it?.let { adapter.submitList(it) }
            })
        }
        setListener()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_record_add_image, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                recordAddImageViewModel.insertImage()
                Toast.makeText(requireContext(), "저장 완료", Toast.LENGTH_SHORT).show()
                val action = RecordAddImageFragmentDirections
                    .actionRecordAddImageFragmentToRecordViewOneFragment()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setListener() {
        binding.btnAddImage.setOnClickListener {
            Intent().apply {
                type = "image/*"
                action = Intent.ACTION_PICK
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                getContent.launch(this)
            }
        }
        binding.tbRecordAddImage.setNavigationOnClickListener {
            val action = RecordAddImageFragmentDirections
                .actionRecordAddImageFragmentToRecordViewOneFragment()
            findNavController().navigate(action)
        }
    }

    private fun initToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.tbRecordAddImage)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.img_leftarrow)
        }
    }
}
