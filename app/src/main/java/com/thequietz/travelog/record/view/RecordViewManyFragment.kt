package com.thequietz.travelog.record.view

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import com.thequietz.travelog.databinding.FragmentRecordViewManyBinding
import com.thequietz.travelog.record.adapter.MultiViewAdapter
import com.thequietz.travelog.record.viewmodel.RecordViewManyViewModel
import com.thequietz.travelog.record.viewmodel.RecordViewOneViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class RecordViewManyFragment : Fragment() {
    private lateinit var _binding: FragmentRecordViewManyBinding
    private val binding get() = _binding
    private val recordViewManyViewModel by viewModels<RecordViewManyViewModel>()
    private val adapter by lazy { MultiViewAdapter() }
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
}
