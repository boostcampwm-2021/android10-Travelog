package com.thequietz.travelog.record.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentRecordAddImageBinding
import com.thequietz.travelog.record.adapter.RecordAddImageAdapter
import com.thequietz.travelog.record.viewmodel.NewImage
import com.thequietz.travelog.record.viewmodel.RecordAddImageViewModel
import com.thequietz.travelog.util.requestImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordAddImageFragment : Fragment() {
    private lateinit var _binding: FragmentRecordAddImageBinding
    private val binding get() = _binding
    private val recordAddImageViewModel by viewModels<RecordAddImageViewModel>()
    private val adapter by lazy { RecordAddImageAdapter() }
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
                Toast.makeText(requireContext(), "저장 완료", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setListener() {
        binding.btnAddImage.setOnClickListener {
            Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                startActivityForResult(this, requestImage)
                return@setOnClickListener
            }
        }
        binding.tbRecordAddImage.setNavigationOnClickListener {
            val action = RecordAddImageFragmentDirections
                .actionRecordAddImageFragmentToRecordViewOneFragment()
            findNavController().navigate(action)
        }
        binding.tbRecordAddImage.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
                    Toast.makeText(requireContext(), "저장 완료", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == requestImage) {
                try {
                    recordAddImageViewModel.addImage(
                        NewImage().copy(
                            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data?.data),
                            place = binding.tvDestination.text.toString(),
                            schedule = binding.tvSchedule.text.toString()
                        )
                    )
                } catch (e: Exception) {
                    println("Something wrong")
                }
            }
        }
    }
}
