package com.thequietz.travelog.record.view

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import com.thequietz.travelog.databinding.FragmentRecordViewManyBinding
import com.thequietz.travelog.record.adapter.RecordViewManyMultiViewAdapter
import com.thequietz.travelog.record.viewmodel.RecordViewManyInnerViewModel
import com.thequietz.travelog.record.viewmodel.RecordViewManyViewModel
import com.thequietz.travelog.record.viewmodel.RecordViewOneViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class RecordViewManyFragment : Fragment() {
    private lateinit var _binding: FragmentRecordViewManyBinding
    private val binding get() = _binding
    private val recordViewManyViewModel by viewModels<RecordViewManyViewModel>()
    private val recordViewInnerViewModel by viewModels<RecordViewManyInnerViewModel>()
    private val adapter by lazy { RecordViewManyMultiViewAdapter(recordViewInnerViewModel) }
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
            dataList.observe(viewLifecycleOwner, { it ->
                it?.let { adapter.submitList(it) }
            })
        }
        setListener()
    }

    fun setListener() {
        binding.ibRecordViewMany.setOnClickListener {
            val action = RecordViewOneViewModel.currentPosition.value?.let { it ->
                RecordViewManyFragmentDirections
                    .actionRecordViewManyFragmentToRecordViewOneFragment(it + 1)
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.ibRecordCamera.setOnClickListener {
            val bm = Bitmap.createBitmap(screenShot(binding.clRecordViewMany)!!)
            val canvas = Canvas(bm)
            val bgDrawable = binding.clRecordViewMany.background
            canvas.drawColor(Color.WHITE)
            binding.clRecordViewMany.draw(canvas)
            val bytes = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

            byteList.add(bytes)
            Toast.makeText(requireContext(), "스크린샷 생성", Toast.LENGTH_SHORT).show()
        }
        binding.ibRecordViewPdf.setOnClickListener {
            val document = Document()
            PdfWriter.getInstance(
                document,
                FileOutputStream(
                    Environment.getExternalStorageDirectory().toString() + "/newPDF.pdf"
                )
            )
            document.open()
            byteList.forEachIndexed { ind, it ->
                val file = File(Environment.getExternalStorageDirectory(), "./myPDF.jpg")
                try {
                    file.createNewFile()
                    val fo = FileOutputStream(file)
                    fo.write(it.toByteArray())

                    val image = Image.getInstance(file.toString())
                    val scaler =
                        (((document.pageSize.width - document.leftMargin()) - document.rightMargin()) / image.width) * 80

                    image.scalePercent(scaler)
                    image.alignment = (Image.ALIGN_CENTER or Image.ALIGN_TOP)
                    document.add(image)
                    if (ind == (byteList.size - 1)) {
                        document.close()
                    }
                    file.delete()
                } catch (e: IOException) {
                    println("something wrong")
                }
            }
            Toast.makeText(requireContext(), "pdf파일 생성", Toast.LENGTH_SHORT).show()

            val pdfFile = File(Environment.getExternalStorageDirectory(), "/newPDF.pdf")
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.setType("application/*")

            val contentUrl = FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".fileprovider", pdfFile)
            sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUrl)
            startActivity(Intent.createChooser(sharingIntent, "파일 공유"))
        }
        binding.ibRecordDelete.setOnClickListener {
            recordViewInnerViewModel.changeDeleteState()
            binding.ibRecordDelete.visibility = View.GONE
            binding.tvRecordDelete.visibility = View.VISIBLE
        }
        binding.tvRecordDelete.setOnClickListener {
            recordViewInnerViewModel.changeDeleteState()
            binding.tvRecordDelete.visibility = View.GONE
            binding.ibRecordDelete.visibility = View.VISIBLE
            if (recordViewInnerViewModel.checkedList.value?.size != 0) {
                showDeleteDiaglog()
            }
        }
    }

    fun screenShot(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
    private fun showDeleteDiaglog() {
        AlertDialog.Builder(requireContext())
            .setTitle("삭제 확인")
            .setMessage("선택된 이미지를 삭제하시겠습니까?")
            .setNegativeButton("예") { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        recordViewInnerViewModel.deleteChecked()
                    }
                    recordViewManyViewModel.change2MyRecord()
                }
                Snackbar.make(binding.clRecordViewMany, "이미지가 삭제되었습니다", Snackbar.LENGTH_SHORT)
                    .show()
            }
            .setPositiveButton("아니오") { dialog, which ->
            }.show()
    }
}
