package com.thequietz.travelog.record.view

/*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.thequietz.travelog.databinding.FragmentRecordAddBinding
import com.thequietz.travelog.record.adapter.RecordPhotoAdapter
import com.thequietz.travelog.record.viewmodel.RecordAddViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordAddFragment : Fragment() {
    private lateinit var binding: FragmentRecordAddBinding

    private val navArgs by navArgs<RecordAddFragmentArgs>()
    private val viewModel by viewModels<RecordAddViewModel>()
    private val adapter by lazy { RecordPhotoAdapter(addImage = ::addImage) }

    private val getImageUri: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            viewModel.addImage(uri.toString())
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordAddBinding.inflate(inflater, container, false)

        binding.rvRecordAddImage.adapter = adapter

        binding.btnRecordAdd.setOnClickListener {
            viewModel.addRecord()
            findNavController().navigateUp()
        }

        viewModel.createRecord(navArgs.travelId, navArgs.day)

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() = with(viewModel) {
        place.observe(viewLifecycleOwner) { place ->
            binding.tvRecordAddDest.text = place
        }
        day.observe(viewLifecycleOwner) { day ->
            binding.tvRecordAddSchedule.text = day
        }
        imageList.observe(viewLifecycleOwner) { imageList ->
            adapter.submitList(imageList)
        }
    }
}
*/
