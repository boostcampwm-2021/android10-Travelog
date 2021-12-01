package com.thequietz.travelog.record.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thequietz.travelog.LoadingDialog
import com.thequietz.travelog.R
import com.thequietz.travelog.data.db.dao.NewRecordImage
import com.thequietz.travelog.databinding.FragmentRecordAddImageBinding
import com.thequietz.travelog.makeSnackBar
import com.thequietz.travelog.record.adapter.RecordAddImageAdapter
import com.thequietz.travelog.record.viewmodel.RecordAddImageViewModel
import com.thequietz.travelog.record.viewmodel.RecordViewOneViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordAddImageFragment : Fragment() {
    private lateinit var _binding: FragmentRecordAddImageBinding
    private val binding get() = _binding
    private val recordAddImageViewModel by viewModels<RecordAddImageViewModel>()
    private val adapter by lazy { RecordAddImageAdapter() }
    lateinit var destinationSpinnerAdapter: ArrayAdapter<String>
    lateinit var scheduleSpinnerAdapter: ArrayAdapter<String>
    lateinit var loading: LoadingDialog

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val clipData = result.data?.clipData
            val res = mutableListOf<NewRecordImage>()
            CoroutineScope(Dispatchers.IO).launch {
                clipData?.let {
                    (0 until it.itemCount).forEachIndexed { ind, item ->
                        res.add(
                            NewRecordImage().copy(
                                newTravelId = RecordViewOneViewModel.currentTravleId,
                                newTitle = recordAddImageViewModel.travelName.value!!,
                                newPlace = recordAddImageViewModel.currentPlace.value!!,
                                url = clipData.getItemAt(ind).uri.toString(),
                                comment = "",
                                isDefault = false
                            )
                        )
                    }
                    recordAddImageViewModel.addImage(res)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordAddImageBinding.inflate(inflater, container, false)
        loading = LoadingDialog(requireContext())
        setHasOptionsMenu(true)
        loading.show()

        initToolbar()
        initAdapter()
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
        Handler(Looper.getMainLooper()).postDelayed({
            addDataToAdapter()
            loading.dismiss()
        }, 1000)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_record_add_image, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                CoroutineScope(Dispatchers.Main).launch {
                    recordAddImageViewModel.insertImages()
                }
                makeSnackBar(binding.clRecordAddImage, "저장 완료")
                RecordViewOneViewModel.currentJoinRecord.value?.let {
                    val action = RecordAddImageFragmentDirections
                        .actionRecordAddImageFragmentToRecordViewOneFragment(
                            it.recordImage.travelId,
                            it.recordImage.day,
                            it.recordImage.place
                        )
                    findNavController().navigate(action)
                }
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
            RecordViewOneViewModel.currentJoinRecord.value?.let {
                val action = RecordAddImageFragmentDirections
                    .actionRecordAddImageFragmentToRecordViewOneFragment(
                        it.recordImage.travelId,
                        it.recordImage.day,
                        it.recordImage.place
                    )
                findNavController().navigate(action)
            }
        }
        binding.spDestination.onItemSelectedListener = (
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    recordAddImageViewModel.placeAndScheduleList.value?.let {
                        recordAddImageViewModel.setCurrentPlaceAndSchedule(it.get(position))
                        recordAddImageViewModel.setMainImage(position)
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            )
    }

    private fun initToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.tbRecordAddImage)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.img_leftarrow)
        }
    }

    private fun initAdapter() {
        destinationSpinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            mutableListOf("일정을 선택하세요")
        )
        /*scheduleSpinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            mutableListOf("날짜를 선택하세요")
        )*/
        binding.spDestination.adapter = destinationSpinnerAdapter
        // binding.spSchedule.adapter = scheduleSpinnerAdapter
    }

    private fun addDataToAdapter() {
        destinationSpinnerAdapter.clear()
        recordAddImageViewModel.placeAndScheduleList.value?.forEach {
            destinationSpinnerAdapter.add(it.toString())
        }
        destinationSpinnerAdapter.notifyDataSetChanged()
        /*scheduleSpinnerAdapter.clear()
        recordAddImageViewModel.placeAndScheduleList.value?.forEach {
            scheduleSpinnerAdapter.add(it.day)
        }
        scheduleSpinnerAdapter.notifyDataSetChanged()*/
    }
}
